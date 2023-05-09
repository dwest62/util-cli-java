import java.util.List;

@FunctionalInterface
public interface RowMapper<T> {
	public List<String> mapRow(T object);
}
