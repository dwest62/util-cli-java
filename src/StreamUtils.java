import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for working with Streams.
 *
 * @author James West
 * @version 1.0
 */
public class StreamUtils {
	/**
	 * Zips two streams int a single stream of pairs.
	 *
	 * The resulting stream maintains the order of the input streams and ends when either input stream ends.
	 *
	 * @param streamA The first stream to be zipped.
	 * @param streamB The second stream to be zipped.
	 * @param <A> The type of the elements in the first stream.
	 * @param <B> The type of the elements in the second stream.
	 * @return A stream of pairs, each containing one element from each input stream.
	 */
	public static <A, B> Stream<Pair<A, B>> zip(Stream<A> streamA, Stream<B> streamB) {
		Iterator<A> iteratorA = streamA.iterator();
		Iterator<B> iteratorB = streamB.iterator();
		
		Iterable<Pair<A,B>> iterable = () -> new Iterator<>() {
			@Override
			public boolean hasNext() {
				return iteratorA.hasNext() && iteratorB.hasNext();
			}
			@Override
			public Pair<A,B> next() {
				return new Pair<>(iteratorA.next(), iteratorB.next());
			}
		};
		
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	
	/**
	 * Applies the specified action to pairs of elements from two streams.
	 * These element pairs are processed in order and the process ends when either input stream ends.
	 *
	 * @param streamA The first stream.
	 * @param streamB The second stream.
	 * @param action The action that will be taken on the element pairs.
	 * @param <A> The type of the elements in the first stream.
	 * @param <B> The type of the elements in the second stream.
	 */
	public static <A, B> void zipWith(Stream<A> streamA, Stream<B> streamB, BiConsumer<A, B> action) {
		Iterator<A> iteratorA = streamA.iterator();
		Iterator<B> iteratorB = streamB.iterator();
		
		while(iteratorA.hasNext() && iteratorB.hasNext()){
			action.accept(iteratorA.next(), iteratorB.next());
		}
	}
}
