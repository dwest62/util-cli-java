import java.util.List;

/**
 * The IndexedRowMapper interface extends RowMapper, providing a method to map an object to a row, incorporating
 * an index.
 * This interface can be useful when the mapping needs to account for the position of the object in its data
 * source.
 *
 * @param <T>   The type of Object to be mapped.
 *
 * @author James West
 * @version 1.0
 */
public interface IndexedRowMapper<T> extends RowMapper<T> {
	/**
	 * Maps an object to a list of strings representing a row, utilizing the index.
	 * The index can be incorporated to create row representations which account for the object's position in a data
	 * source.
	 * @param object    The object to map.
	 * @param index     The index representing the object's position.
	 * @return A list of strings representing a row.
	 */
	public List<String> mapRow(T object, int index);
}
