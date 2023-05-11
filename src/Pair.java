/**
 * Represents a pair of objects of types A and B.
 *
 * @param <A> Type A.
 * @param <B> Type B.
 *
 * @author James West
 * @version 1.0
 */
public class Pair<A, B> {
	private final A first;
	private final B second;

	/**
	 * Constructs a Pair which groups two objects of types {@code A} and {@code B}.
	 *
	 * @param first The first object in the pair
	 * @param second The second object in the pair
	 */
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns the first object of type {@code A} of the pair.
	 *
	 * @return The first object of type {@code A} of the pair.
	 */
	public A getA() {
		return this.first;
	}

	/**
	 * Returns the second object of type {@code B} of the pair.
	 *
	 * @return The second object of type {@code B} of the pair.
	 */
	public B getB() {
		return this.second;
	}
}
