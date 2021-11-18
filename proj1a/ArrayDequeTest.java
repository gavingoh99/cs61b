import org.junit.Test;
import static org.junit.Assert.*;
public class ArrayDequeTest {
    @Test
    public void testAddSizeUpdatesSize() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        //test if initialized array is empty
        assertTrue(test.isEmpty());
        //test adding method updates size
        test.addFirst(10);
        assertEquals(1, test.size());
        test.addLast(15);
        assertEquals(2, test.size());
    }
    @Test
    public void testAddToCorrectIndex() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addFirst(10);
        test.addFirst(5);
        test.addLast(15);

        //expected: 5, 10, 15
        //test adding method adds the item into the right spot
        test.printDeque();
    }

    @Test
    public void testGetAndRemove() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addFirst(10);
        test.addFirst(5);
        test.addLast(15);

        //expected: 5 10 15
        //test get methods
        //returns correct value
        assertEquals(5, test.get(0).intValue());
        assertEquals(15, test.get(2).intValue());
        //returns null for out of index
        assertEquals(null, test.get(3));

        //test remove methods
        assertEquals(5, test.removeFirst().intValue());
        assertEquals(15, test.removeLast().intValue());
        //empty the deque
        test.removeFirst();
        assertEquals(null, test.removeFirst());
        assertEquals(null, test.removeLast());
    }
    @Test
    public void testResize() {
        //add 8 items to deque
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addFirst(5);
        test.addFirst(10);
        test.addFirst(15);
        test.addFirst(20);
        test.addFirst(25);
        test.addFirst(30);
        test.addFirst(35);
        test.addFirst(40);

        //add 9th item and check for existence
        test.addFirst(45);
        assertEquals(45, test.get(0).intValue());

        //check resize function copies values properly
        assertEquals(45, test.removeFirst().intValue());
        assertEquals(5, test.removeLast().intValue());
        //check that position 3 holds the correct item
        assertEquals(25, test.get(3).intValue());
    }
    @Test
    public void testAddLast() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addLast(5);
        test.addLast(10);
        test.addLast(15);
        test.addLast(20);
        test.addLast(25);
        test.addLast(30);
        test.addLast(35);
        //check last value
        assertEquals(35, test.removeLast().intValue());
        //check random value
        assertEquals(15, test.get(2).intValue());
        test.printDeque();
    }
    @Test
    public void testDownsize() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        int i = 0;
        while (i < 500) {
            test.addFirst(i);
            i++;
        }
        int x = 0;
        while (x < 499) {
            test.removeFirst();
            x++;
        }
    }
    @Test
    public void randomCallsTest() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addLast(0);
        test.addLast(1);
        test.addFirst(2);
        test.removeFirst();
        test.addLast(4);
        test.removeFirst();
        test.addFirst(6);
        test.removeFirst();
        test.get(1);
        test.addLast(9);
        test.removeFirst();
        test.addFirst(11);
        test.addFirst(12);
        test.removeFirst();
        test.addLast(14);
        test.removeFirst();
        test.removeFirst();
        test.get(1);
        test.addFirst(18);
        test.get(2);
        test.removeLast();
        test.removeFirst();
        test.removeLast();
    }
}
