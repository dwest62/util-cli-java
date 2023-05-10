/**
 * The TablePrinter interface defines a method for processing a Table and returning a String representation of it.
 *
 * @param <T> The type of the entries in the table.
 * @param <R> The type of the row mapper to be used to map the entries of the table into rows.
 * @author James West
 * @version 1.0
 */
public interface TablePrinter<T, R extends RowMapper<T>> {
	/**
	 * Processes a table returning a String representation of it.
	 *
	 * @param table The table to be processed.
	 * @return A String representation of the table.
	 */
	String process(Table<T, R> table);
}

