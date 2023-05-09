import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class AutoSizeTablePrinter<T> implements TablePrinter<T> {

	private enum AlignmentStrategy {
		LEFT(Table.Column.Alignment.LEFT, (column, entry) -> entry + " ".repeat(column.getWidth() - entry.length())),
		RIGHT(Table.Column.Alignment.RIGHT,
			((column, entry) -> " ".repeat(column.getWidth() - entry.length()) + entry)),
		CENTER(Table.Column.Alignment.CENTER, (column, entry) -> {
			int leftSpaces = (column.getWidth() - entry.length()) / 2;
			int rightSpaces = column.getWidth() - entry.length() - leftSpaces;
			return  " ".repeat(leftSpaces) + entry + " ".repeat(rightSpaces);
		});
	private  Table.Column.Alignment alignment;
	private BiFunction<Table.Column, String, String> strategy;
	
	AlignmentStrategy(Table.Column.Alignment alignment, BiFunction<Table.Column, String ,String> strategy) {
		this.alignment = alignment;
		this.strategy = strategy;
	}
	
	public String align(Table.Column column, String entry) {
		for (AutoSizeTablePrinter.AlignmentStrategy strategy : values())
			if(strategy.alignment == alignment)
				return this.strategy.apply(column, entry);
		throw new IllegalArgumentException("No AlignmentStrategy for Alignment " + alignment);
	}
	public static AutoSizeTablePrinter.AlignmentStrategy fromAlignment(Table.Column.Alignment alignment)
	{
		for (AutoSizeTablePrinter.AlignmentStrategy strategy : values())
			if(strategy.alignment == alignment)
				return strategy;
		throw new IllegalArgumentException("No AlignmentStrategy for Alignment " + alignment);
	}
}
	
	private static void sizeColumn(Integer width, Table.Column column) {
		column.setWidth(Math.max(column.getHeader().length(), width));
	}
	
	@Override
	public String process(Table<T> table) {
		
		Collector<CharSequence, ?, String> join = Collectors.joining(" | ", "| ", " |");
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
		return AutoSizeTablePrinter.AlignmentStrategy.fromAlignment(column.getAlignment()).align(column, entry);
	}
}