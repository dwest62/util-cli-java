import java.util.Optional;
import java.util.function.Function;

/**
 * A utility class used to parse a String into an Optional. If the object can be parsed an optional containing the new
 * object of type {@code T} will be returned, otherwise an empty Optional will be returned.
 *
 * @param <T> The type to which the String will be parsed.
 */
public class TryParse<T>  {
	private final Function<String, T> parser;
	
	TryParse(Function<String, T> parser) {
		this.parser = parser;
	}
	
	/**
	 * Attempts to parse the input String to type {@code T} and returns an optional parsed input if successful.
	 *
	 * @param input     The input to be parsed.
	 * @return An Optional containing the parsed input of type {@code T} if successful, otherwise an empty Optional if
	 * the parsing failed.
	 */
	public Optional<T> parse(String input) {
		try {
			return Optional.ofNullable(parser.apply(input));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	/**
	 * Creates a new TryParse instance to help parse Integers.
	 *
	 * @return A new TryParse instance to help parse Integers.
	 */
	public static TryParse<Integer> forInteger() {
		return new TryParse<>(Integer::parseInt);
	}
	
	/**
	 * Creates a new TryParse instance to help parse Doubles.
	 *
	 * @return A new TryParse instance to help parse Doubles.
	 */
	public static TryParse<Double> forDouble() {
		return new TryParse<>(Double::parseDouble);
	}
	
	/**
	 * Creates a new TryParse instance to help parse Longs.
	 *
	 * @return A new TryParse instance to help parse Longs.
	 */
	public static TryParse<Long> forLong() {
		return new TryParse<>(Long::parseLong);
	}
}
