import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
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
	
	public static <A, B> void zipWith(Stream<A> streamA, Stream<B> streamB, BiConsumer<A, B> action) {
		Iterator<A> iteratorA = streamA.iterator();
		Iterator<B> iteratorB = streamB.iterator();
		
		while(iteratorA.hasNext() && iteratorB.hasNext()){
			action.accept(iteratorA.next(), iteratorB.next());
		}
	}
}
