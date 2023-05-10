import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class functions to create a string representation of a table with auto-sized columns.
 *
 * @param <T> The type of data in the table.
 * @param <R> The type of the RowMapper used to map data into a list of Strings representing a row.
 *
 * @author James West
 * @version 1.0
 */
class AutoSizeTablePrinter<T, R extends RowMapper<T>> implements TablePrinter<T, R> {
	
	/**
	 * Map of strategies for aligning text within a cell.
	 */
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
	
	
	/**
	 * Processes a table, auto-sizing columns, and returns a String representation.
	 *
	 * @param table The table to process.
	 *
	 * @return The String representation of the table.
	 */
	@Override
	public String process(Table<T, R> table) {
		return process(table, (t)->true);
	}
	
	/**
	 * Processes a table, auto-sizing columns and filtering entries, and returns a String representation.
	 *
	 * @param table The table to process.
	 * @param filter The filter applied to the table entries.
	 *
	 * @return The String representation of the filtered table.
	 */
	public String process(Table<T, R> table, Predicate<T> filter) {
		if(table.getColumns().isEmpty() && table.getMappedEntries(filter).isEmpty())
			return "Table is empty...";
		
		final List<List<String>> entries = table.getMappedEntries(filter);
		final List<Table.Column> columns = table.getColumns();
		
		autoSizeColumns(entries, columns);
		
		String header = getHeader(columns);
		
		String body = getBody(entries, columns);
		
		String bar = getBar(columns);
		
		String footer = getFooter(entries.size());
		
		return String.join("\n", bar, header, bar, body, bar, footer);
	}
	
	/**
	 * Adjusts the widths of the columns to match the longest entry in each column.
	 *
	 * @param rows The rows in the table.
	 * @param columns The columns in the table to be sized.
	 */
	protected void autoSizeColumns(List<List<String>> rows, List<Table.Column> columns) {
		
		Stream<Integer> columnWidths = IntStream.range(0, columns.size())
			                               .mapToObj(col -> rows.stream()
				                                                .mapToInt(row -> row.get(col).length())
				                                                .max().orElse(0)
			                               );
		
		Stream<Table.Column> columnStream = columns.stream();
		
		BiConsumer<Integer, Table.Column> sizeColumn =  (Integer width, Table.Column column) ->
			                                                column.setWidth(Math.max(column.getHeader().length(), width));
		
		StreamUtils.zipWith(
			columnWidths,
			columnStream,
			sizeColumn
		);
	}
	
	/**
	 * Returns a sized, horizontal table bar used to outline the table.
	 *
	 * @param columns The columns of the table used to size to the bar.
	 * @return A sized, horizontal table bar used to outline the table.
	 */
	protected String getBar(List<Table.Column> columns) {
		return "+"
			       + "-".repeat(
			columns.stream().mapToInt(Table.Column::getWidth).sum()
				+ columns.size() - 1
				+ 2 * columns.size()
		)
			       + "+";
	}
	
	/**
	 * Returns a header for the table which displays the title for each column.
	 *
	 * @param columns The list of columns in the table.
	 * @return A String representation of the table header.
	 */
	protected String getHeader(List<Table.Column> columns) {
		return processRow(columns.stream().map(Table.Column::getHeader), columns.stream());
	}
	
	/**
	 * Returns a String representation of the table body.
	 *
	 * @param rows The rows in the table.
	 * @param columns The columns in the table.
	 * @return The String representation of the table body.
	 */
	protected String getBody(List<List<String>> rows, List<Table.Column> columns) {
		return rows.stream()
	       .map(row -> processRow(row.stream(), columns.stream()))
	       .collect(Collectors.joining("\n"));
	}
	
	/**
	 * Returns a footer for the table which indicates the number of rows.
	 *
	 * @param size The number of rows in the table.
	 * @return The String representation of the table footer.
	 */
	protected String getFooter(int size) {
		return size + " rows in the set.";
	}
	
	/**
	 * Processes a row, adding borders to separate cells, and returns a String representation.
	 *
	 * @param rowStream The stream of cells in a row to be processed.
	 * @param columnStream The stream of columns in the table used to size each cell and align text within each cell.
	 * @return A String representation of a single table row.
	 */
	protected String processRow(Stream<String> rowStream, Stream<Table.Column> columnStream) {
		return StreamUtils.zip(rowStream, columnStream)
			       .map(pair -> processCell(pair.getB(), pair.getA()))
			       .collect(Collectors.joining(" | ", "| ", " |")
			       );
	}
	
	/**
	 * Processes a text, sizing the cell, aligning the text within the cell, and returning a String representation
	 * of the table cell containing the text.
	 *
	 * @param column The column of the cell to be used to size a cell and align the text within the cell.
	 * @param text The text to process.
	 * @return A String representation of a single table cell containing the text.
	 */
	protected String processCell(Table.Column column, String text) {
		return AutoSizeTablePrinter.ALIGNMENT_STRATEGY.getOrDefault(column.getAlignment(),
			ALIGNMENT_STRATEGY.get(Table.Column.Alignment.LEFT)).apply(column, text);
	}
}