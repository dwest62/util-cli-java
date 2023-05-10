import java.util.function.Predicate;

/**
 * A rule for validating an input value of type {@code T}.
 * @param <T> The type of input value to validate.
 *
 * @author James West
 * @version 1.0
 */
public class Rule<T> {
	private final Predicate<T> predicate;
	private final InputErrorHandler onErr;
	
	/**
	 * @param predicate The predicate that tests the validity of an input.
	 * @param onErr The error handler to be called when the input does not satisfy the rule.
	 */
	public Rule(Predicate<T> predicate, InputErrorHandler onErr) {
		this.predicate = predicate;
		this.onErr = onErr;
	}
	
	/**
	 * Returns the predicate that tests the validity of an input.
	 *
	 * @return The predicate that tests the validity of an input.
	 */
	public Predicate<T> getPredicate() {
		return predicate;
	}
	
	/**
	 * Returns the error handler to be called when the input does not satisfy the rule.
	 *
	 * @return The error handler to be called when the input does not satisfy the rule.
	 */
	public InputErrorHandler getOnErr() {
		return onErr;
	}
}
