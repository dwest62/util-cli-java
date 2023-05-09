
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface TablePrinter<T> {
	String process(Table<T> table);
}
