import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class AutoSizeTablePrinter<T> implements TablePrinter<T> {

	private static final Map<Table.Column.Alignment, BiFunction<Table.Column, String, String>> ALIGNMENT_STRATEGY =
		Map.of(
			Table.Column.Alignment.LEFT, ((column, entry) -> entry + " ".repeat(column.getWidth() - entry.length())),
			Table.Column.Alignment.RIGHT, ((column, entry) -> " ".repeat(column.getWidth() - entry.length()) + entry),
			Table.Column.Alignment.CENTER, ((column, entry) -> {
				int leftSpaces = (column.getWidth() - entry.length()) / 2;
				int rightSpaces = column.getWidth() - entry.length() - leftSpaces;
				return  " ".repeat(leftSpaces) + entry + " ".repeat(rightSpaces);
			})
		);
	
	private static void sizeColumn(Integer width, Table.Column column) {
		column.setWidth(Math.max(column.getHeader().length(), width));
	}
	
	@Override
	public String process(Table<T> table) {
		autoSizeColumns(table);
		List<Table.Column> columns = table.getColumns();
		
		String header = processRow(columns.stream().map(Table.Column::getHeader), columns.stream());
		
		String body = table.getEntries().stream()
			              .map(table.getRowMapper()::mapRow)
			              .map(row -> processRow(row.stream(), columns.stream()))
			              .collect(Collectors.joining("\n"));
		
		String bar = generateBar(columns);
		
		return String.join("\n", bar, header, bar, body, bar);
	}
	
	private String generateBar(List<Table.Column> columns) {
		return "+"
			       + "-".repeat(
			columns.stream().mapToInt(Table.Column::getWidth).sum()
				+ columns.size() - 1
				+ 2 * columns.size()
		)
			       + "+";
	}
	
	private void autoSizeColumns(Table<T> table) {
		
		
		Stream<Integer> columns = IntStream.range(0, table.getColumns().size())
			.mapToObj(col -> table.getEntries().stream()
			                   .map(table.getRowMapper()::mapRow)
			                   .mapToInt(row -> row.get(col).length())
			                   .max().orElse(0)
			);

		Stream<Table.Column> columnStream = table.getColumns().stream();
		
		StreamUtils.zipWith(
			columns,
			columnStream,
			AutoSizeTablePrinter::sizeColumn
		);
	}
	
	private String processRow(Stream<String> rowStream, Stream<Table.Column> columnStream) {
		return StreamUtils.zip(rowStream, columnStream)
			       .map(pair -> processCell(pair.getSecond(), pair.getFirst()))
			       .collect(Collectors.joining(" | ", "| ", " |")
			       );
	}
	
	private String processCell(Table.Column column, String entry) {
		return AutoSizeTablePrinter.ALIGNMENT_STRATEGY.getOrDefault(column.getAlignment(),
			ALIGNMENT_STRATEGY.get(Table.Column.Alignment.LEFT)).apply(column, entry);
	}
}