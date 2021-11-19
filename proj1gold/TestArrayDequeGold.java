import org.junit.Test;
import static org.junit.Assert.*;
public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> test = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        String methodCalls = "\n";
        for (int count = 0; count < 100; count++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.25) {
                test.addFirst(count);
                solution.addFirst(count);
                methodCalls += "addFirst(" + count + ")\n";
            } else if (numberBetweenZeroAndOne < 0.5) {
                test.addLast(count);
                solution.addLast(count);
                methodCalls += "addLast(" + count + ")\n";
            } else if (numberBetweenZeroAndOne < 0.75 && test.size() != 0 && solution.size() != 0) {
                methodCalls += "removeFirst()\n";
                assertEquals(methodCalls, solution.removeFirst(), test.removeFirst());
            } else if (numberBetweenZeroAndOne < 1 && test.size() != 0 && solution.size() != 0) {
                methodCalls += "removeLast()\n";
                assertEquals(methodCalls, solution.removeLast(), test.removeLast());
            } else {
                continue;
            }
        }
    }
}
