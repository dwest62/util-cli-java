import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * IndexedTable extends the Table class, mapping entries using an IndexedRowMapper.
 * This class is useful when the mapping of rows depends on their position in a data source.
 *
 * Uses the builder pattern to configure the table and add date, verifying a legitimate table on build.
 *
 * @param <T>   The type of the table entries.
 * @param <R>   The type of the IndexedRowMapper.
 *
 * @author James West
 * @version 1.0
 */
public class IndexedTable<T, R extends IndexedRowMapper<T>> extends Table<T, R> {
	/**
	 * Used by the IndexedBuilder class to create a new Indexed Table.
	 */
	protected IndexedTable() {
		super();
	}

	/**
	 * Constructs a new indexed table using the provided list of entries.
	 *
	 * @param entries The list of entries to be used by the table.
	 */
	protected IndexedTable(List<T> entries) {
		super(entries);
	}

	/**
	 * Uses the table's IndexedRowMapper to map the table's list of entries to their row representation, accounting
	 * for the index of the row in the table.
	 *
	 *
	 * @return The list of lists of strings representing the table rows.
	 */
	@Override
	public List<List<String>> getMappedEntries() {
		return getMappedEntries(this.entries);
	}
	
	/**
	 * Uses the table's IndexedRowMapper to map the provided list of entries to their row representation, accounting
	 * for the index of the row in the table.
	 *
	 * @param entries   The list of entries to map.
	 * @return The list of lists of strings representing the table rows.
	 */
	@Override
	public List<List<String>> getMappedEntries(List<T> entries) {
		return IntStream.range(0, entries.size()).boxed()
			       .map(i-> rowMapper.mapRow(entries.get(i), i))
			       .toList();
	}
	/**
	 * Uses the table's IndexedRowMapper to map a filtered list of this table's entries to their row representation,
	 * accounting for the index of the row in the table.
	 *
	 * The filtered entries will maintain their pre-filtered index.
	 *
	 * @return The list of lists of strings representing the table rows.
	 */
	@Override
	public List<List<String>> getMappedEntries(Predicate<T> filter)
	{
		return IntStream.range(0, entries.size()).boxed()
			       .filter(i -> filter.test(entries.get(i)))
			       .map(i -> rowMapper.mapRow(entries.get(i), i))
			       .toList();
	}
	
	/**
	 * A builder for IndexedTable that incorporates an added index column.
	 *
	 * @param <T>   The type of the entries the IndexedTable contains.
	 * @param <R>   The type of the IndexedRowMapper.
	 */
	public static class IndexedBuilder<T, R extends IndexedRowMapper<T>> extends Table.Builder<T, R> {
		/**
		 * Constructs a new IndexedBuilder with an IndexRowMapper and an index column.
		 *
		 * The added index column is used to hold the index data mapped by the IndexedRowMapper.
		 *
		 * @param rowMapper     The row mapper to use.
		 * @param indexColumn   The index column of the table.
		 */
		IndexedBuilder(R rowMapper, Column indexColumn) {
			this(rowMapper, indexColumn, new ArrayList<>());
		}

		/**
		 * Initializes a new indexed builder using the provided rowMapper, indexColumn, and entries.
		 *
		 * @param rowMapper The row mapper used by the table.
		 * @param indexColumn The index column of the table.
		 * @param entries The list of entries used by the table.
		 */
		IndexedBuilder(R rowMapper, Column indexColumn, List<T> entries) {
			super(new IndexedTable<>(entries), rowMapper);
			this.addColumn(indexColumn);
		}
		
		/**
		 * Builds an IndexedTable using the builder's current parameters.
		 *
		 * Ensures that the number of entries in each row matches the number of columns.
		 *
		 * @return The IndexTable instance.
		 * @throws IllegalArgumentException Thrown when the number of entries of any row does not match the number of
		 *                                  columns.
		 */
		@Override
		public IndexedTable<T, R> build()
		{
			if(!table.getEntries().isEmpty()) {
				int expectedSize = table.getColumns().size();
				for(int i = 0; i < (table).getMappedEntries().size(); i++)
					if(table.getMappedEntries().get(i).size() != expectedSize)
						throw new IllegalArgumentException("The number of entries in the row does not match the " +
							                                   "number of columns");
			}
			return (IndexedTable<T, R>) this.table;
		}
	
	}
}
