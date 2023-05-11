
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


/**
 * Represents a table containing row and column data and a corresponding row mapper.
 *
 * This class uses the builder strategy to configure the table before instantiation.
 *
 * @param <T> The type of the entries in the table.
 * @param <R> The type of the row mapper.
 *
 * @author James West
 * @version 1.0
 */
public class Table<T, R extends RowMapper<T>> {
	protected R rowMapper;
	protected List<T> entries;
	protected final List<Column> columns;
	
	/**
	 * Constructs a Table with empty entries and columns. Used by the builder to begin table configuration.
	 */
	protected Table() {
		entries = new ArrayList<>();
		columns = new ArrayList<>();
	}
	
	/**
	 * Returns the table's row mapper.
	 *
	 * @return The row mapper.
	 */
	public R getRowMapper() {
		return rowMapper;
	};
	
	/**
	 * Sets the table's row mapper.
	 *
	 * @param rowMapper The new row mapper.
	 */
	protected void setRowMapper(R rowMapper)
	{
		this.rowMapper = rowMapper;
	}
	
	/**
	 * Returns the table's current entries.
	 *
	 * @return The list containing the entries.
	 */
	public List<T> getEntries() {
		return entries;
	}
	
	/**
	 * Sets the table's entries to a new list.
	 *
	 * @param entries The new list of entries.
	 */
	protected void setEntries(List<T> entries) {
		this.entries = entries;
	}
	
	/**
	 * Returns the table's columns.
	 *
	 * @return The table's columns.
	 */
	public List<Column> getColumns() {
		return columns;
	}
	
	/**
	 * Returns a list of lists of strings representing entries in the table mapped to their corresponding rows using
	 * the table's row mapper.
	 *
	 * @return A list of lists of strings representing entries in the table mapped to their corresponding rows using
	 * the table's row mapper.
	 */
	public List<List<String>> getMappedEntries() {
		return getMappedEntries(this.entries);
	}
	
	/**
	 * Returns a list of lists of strings representing input entries mapped to their corresponding rows
	 * using the table's row mapper.
	 *
	 * @param entries The entries to map.
	 * @return A list of lists of strings representing entries in the table mapped to their corresponding rows using
	 * the table's row mapper.
	 */
	public List<List<String>> getMappedEntries(List<T> entries) {
		return entries.stream().map(rowMapper::mapRow).toList();
	}
	
	/**
	 * Returns a list of lists of strings representing the filtered entries mapped to their corresponding rows using
	 * the table's row mapper.
	 *
	 * @param filter The filter applied to the table entries.
	 * @return A list of lists of strings representing the filtered set of entries in the tabled mapped to their
	 * corresponding rows using the table's row mapper.
	 */
	public List<List<String>> getMappedEntries(Predicate<T> filter) {
		return entries.stream().filter(filter).map(rowMapper::mapRow).toList();
	}
	
	/**
	 * A builder used to configure the table and build it.
	 * @param <T> The type of the table to build.
	 * @param <R> The type of the table's row mapper.
	 */
	public static class Builder<T, R extends RowMapper<T>> {
		protected final Table<T, R> table;
		
		/**
		 * Constructs a builder using the provided row mapper.
		 *
		 * @param rowMapper The row mapper to be used by the table.
		 */
		public Builder(R rowMapper) {
			this(new Table<>(), rowMapper);
		}
		
		/**
		 * Constructs a builder using the provided table and row mapper.
		 *
		 * Useful in initializing this table in subclasses.
		 *
		 * @param table The table which will be built.
		 * @param rowMapper The table's row mapper.
		 */
		public Builder(Table<T, R> table, R rowMapper) {
			this.table = table;
			table.setRowMapper(rowMapper);
		}
		
		/**
		 * Adds an entry to the table.
		 *
		 * @param entry The entry to add.
		 * @return The Builder instance for method chaining.
		 */
		public Builder<T, R> addEntry(T entry) {
			table.getEntries().add(entry);
			return this;
		}
		/**
		 * Adds an entry to the table at the specified row index.
		 *
		 * @param entry The entry to add.
		 * @param rowNumber The row number to place the entry indexed at 0.
		 * @return The Builder instance for method chaining.
		 */
		public Builder<T, R> addEntry(T entry, int rowNumber) {
			table.getEntries().add(rowNumber, entry);
			return this;
		}
		
		/**
		 * Adds a list of entries to the table.
		 *
		 * @param entries The list of entries to add.
		 * @return The Builder instance for method chaining.
		 */
		public Builder<T, R> addEntries(List<T> entries) {
			table.getEntries().addAll(entries);
			return this;
		}
		
		/**
		 * Adds a column to the table.
		 *
		 * @param column The column to add.
		 * @return The Builder instance for method chaining.
		 */
		public Builder<T, R> addColumn(Column column) {
			table.getColumns().add(column);
			return this;
		}
		
		/**
		 * Adds a column to the table at a specified column index.
		 * The column list is indexed at 0.
		 *
		 * @param column The column to add.
		 * @param colNumber The number of the index of new column.
		 * @return The Builder instance for method chaining.
		 */
		public Builder<T, R> addColumn(Column column, int colNumber) {
			table.getColumns().add(colNumber, column);
			return this;
		}
		
		/**
		 * Adds a list of columns to the table.
		 *
		 * @param columns The list of columns to add.
		 * @return The Builder instance for method chaining.
		 */
		public Builder<T, R> addColumns(List<Column> columns) {
			table.getColumns().addAll(columns);
			return this;
		}
		
		/**
		 * Build a table using the current parameters.
		 * Ensures that the number of entries in each row matches the number of columns.
		 *
		 * @throws IllegalArgumentException Thrown when the number of entries of any row does not match the number of
		 *                                  columns.
		 * @return The table instance.
		 */
		public Table<T, R> build()
		{
			if(!table.getEntries().isEmpty()) {
				int expectedSize = table.getColumns().size();
				for(int i = 0; i < table.getMappedEntries().size(); i++)
					if(table.getMappedEntries().get(i).size() != expectedSize)
						throw new IllegalArgumentException("The number of entries in the row does not match the " +
							                                   "number of columns");
			}
			return this.table;
		}
	}
	
	/**
	 * The Column represents a column in a table with a given width, header, and alignment.
	 */
	public static class Column {
		/**
		 * Represents the alignment of the column.
		 */
		public enum Alignment {
			LEFT,
			RIGHT,
			CENTER
		}
		private String header;
		
		private int width;
		
		private Alignment alignment;
		
		/**
		 * Constructs a new column with the provided header and width and a default left alignment.
		 *
		 * @param header The header of the column.
		 * @param width The width of the column.
		 */
		public Column(String header, int width) {
			this.header = header;
			this.width = width;
			this.alignment = Alignment.LEFT;
		}
		
		/**
		 * Constructs a new column with the provided header, width, and alignment.
		 *
		 * @param header The header of the column.
		 * @param width The width of the column.
		 * @param alignment The alignment of the column.
		 */
		public Column(String header, int width, Alignment alignment) {
			this.header = header;
			this.width = width;
			this.alignment = alignment;
		}
		
		/**
		 * Returns the alignment of the column.
		 *
		 * @return The alignment of the column.
		 */
		public Alignment getAlignment() {
			return alignment;
		}
		
		/**
		 * Sets the alignment of the column.
		 *
		 * @param alignment The alignment of the column.
		 */
		public void setAlignment(Alignment alignment) {
			this.alignment = alignment;
		}
		
		
		/**
		 * Returns the header of the column.
		 *
		 * @return The header of the column.
		 */
		public String getHeader() {
			return header;
		}
		
		/**
		 * Sets the header of the column.
		 *
		 * @param header The header of the column.
		 */
		public void setHeader(String header) {
			this.header = header;
		}
		
		/**
		 * Returns the width of the column.
		 *
		 * @return The width of the column.
		 */
		public int getWidth() {
			return width;
		}
		
		/**
		 * Sets the width of the column.
		 *
		 * @param width The width of the column.
		 */
		public void setWidth(int width) {
			this.width = width;
		}
		
	}
	
}

