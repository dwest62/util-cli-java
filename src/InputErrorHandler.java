/**
 * Functional interface which can be used to define custom input error handling.
 */
public interface InputErrorHandler {
	/**
	 * Handle the input error.
	 *
	 * @param input The input String which caused the error.
	 */
	void handle(String input);
}
