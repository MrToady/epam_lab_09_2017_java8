package part1.exercise;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private long startInclusive;
    private long endExclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, checkArrayAndCalcEstimatedSize(array));
    }

    public RectangleSpliterator(int[][] array, long start, long end) {
        super(end - start,
                Spliterator.IMMUTABLE
                        | Spliterator.ORDERED
                        | Spliterator.SIZED
                        | Spliterator.SUBSIZED
                        | Spliterator.NONNULL);
        this.array = array;
        this.startInclusive = start;
        this.endExclusive = end;
    }

    private static long checkArrayAndCalcEstimatedSize(int[][] array) {
        if (Arrays.stream(array)
                .allMatch(o -> o.length == array[0].length)) {
            return array.length * array[0].length;
        }
        throw new IllegalStateException("Not rectangular.");
    }

    @Override
    public OfInt trySplit() {
        long len = endExclusive - startInclusive;
        if (len < 2) {
            return null;
        }
        long middle = startInclusive + len / 2;
        RectangleSpliterator result = new RectangleSpliterator(array, startInclusive, middle);
        startInclusive = middle;
        return result;
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive < endExclusive) {
            startInclusive++;
            action.accept(array
                    [(int) (startInclusive / array[0].length)]
                    [(int) (startInclusive % array[0].length)]);
            return true;
        }
        return false;
    }
}
