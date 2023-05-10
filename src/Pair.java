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
	
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	
	public A getA() {
		return this.first;
	}
	
	public B getB() {
		return this.second;
	}
}
