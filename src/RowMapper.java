import java.util.List;

/**
 * Interface used to map an object of type T to a row.
 *
 * This is used to implement a mapping strategy for an object of type T.
 *
 * @param <T> The type of object to map.
 */
public interface RowMapper<T> {
	/**
	 * Maps an object to a list of string representing a row.
	 *
	 * @param object The object to be mapped.
	 * @return A list of strings representing a row.
	 */
	List<String> mapRow(T object);
}
