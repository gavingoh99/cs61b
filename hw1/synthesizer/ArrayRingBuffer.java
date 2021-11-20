package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.rb = (T[]) new Object[capacity];
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (this.isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        this.rb[this.last] = x;
        this.fillCount++;
        this.last++;
        if (this.last == this.capacity) {
            this.last = 0;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T itemToBeRemoved = this.rb[this.first];
        this.rb[this.first] = null;
        this.fillCount--;
        this.first++;
        if (this.first == this.capacity) {
            this.first = 0;
        }
        return itemToBeRemoved;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring buffer empty");
        }
        return this.rb[this.first];
    }
    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator<>();
    }

    private class ArrayRingBufferIterator<T> implements Iterator<T> {
        private int pointer;
        ArrayRingBufferIterator() {
            this.pointer = first;
        }
        @Override
        public boolean hasNext() {
            return this.pointer < fillCount + first;
        }
        @Override
        public T next() {
            T item = (T) rb[this.pointer % capacity];
            this.pointer++;
            return item;
        }
    }
}
