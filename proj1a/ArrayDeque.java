public class ArrayDeque<T> {
    private T[] array;
    private int size;
    private int first;
    private int last;
    public ArrayDeque() {
        this.array = (T[]) new Object[8];
        this.size = 0;
        this.first = 3;
        this.last = 4;
    }
    public void addFirst(T item) {
        if (size == this.array.length) {
            resize();
        }
        if (first < 0) {
            first = this.array.length - 1;
        }
        this.array[first] = item;
        size++;
        first--;
    }
    public void addLast(T item) {
        if (size == this.array.length) {
            resize();
        }
        if (last > this.array.length - 1) {
            last = 0;
        }
        this.array[last] = item;
        size++;
        last++;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return this.size;
    }
    //consider l l f f l l l l or f f f f l l f f, preferrably during resizing we should try to rearrange
    // our elements to be f f l l l l l l and f f f f f f l l respectively
    // we would want something like this with no overflows so that our methods still work - - f f l l l l l l - -
    // to do so we have to iterate through the filled array like l l f f l l l l, here first = 1, first element is
    // at index first + 1, iterate from index 2 to index 1: 2, 3, 4, 5, 6, 7, 0, 1 using modulo
    // and at to position starting at resize.length - array.length / 2

    // 20 15 10 5 40 35 30 25, first = 7, last = 0
    // - -  - -, first = 1, last = 10
    public void resize() {
        T[] resized = (T[]) new Object[(int) (this.array.length * 1.5)];
        int startPosition = (int) (resized.length - array.length) / 2;
        int temp = startPosition;
        for (int index = first + 1; index <= first + this.size; index++) {
            resized[startPosition] = array[index % array.length];
            startPosition++;
        }
        first = temp - 1;
        last = temp + this.size;
        this.array = resized;
    }
    //how do we iterate through this array? think circular how do we maintain the bounds within the valid indices?
    // modulo operator dividing by this.array.length - 1 keeps everything within the valid index 0 - this.array.length-1
    // l l f f l l l l <- index must take the following values 2 3 4 5 6 7 0 1, - > 2 3 4 5 6 7 8 9...using for loop with modulo
    // what about f f f f l l x f <- index must take  7 0 1 2 3 4 5, -> 7 8 9 10 11 12 13..using for loop with modulo gives what we want
    // what about something more normal x x f f l l l x <- index must take 2 3 4 5 6 this is fine we can just use a for loop
    public void printDeque() {
        // x x x x x x x x empty
        // l l x x l l l l all lasts or all firsts f f f f x x f f  -> 4 5 6 7 8 9
        // f f f f l x f f first overflow, last = 5, first = 5, 6 7 8 9 10 11 12
        // l l x f l l l l last overflow 3 4 5 6 7 8 9

        //empty
        if (this.isEmpty()) {
            return;
        }
        //every other case
        for(int index = first + 1; index <= first + size; index++) {
            System.out.println(array[index % array.length] + " ");
        }
    }
    public T removeFirst() {
        if (this.size == 0) {
            return null;
        }
        int indexFirst = (first + 1) % this.array.length;
        T item = this.array[indexFirst];
        this.array[indexFirst] = null;
        size--;
        first++;
        return item;
    }
    public T removeLast() {
        if (this.size == 0) {
            return null;
        }
        int indexLast = (last - 1 + this.array.length) % this.array.length;
        T item = this.array[indexLast];
        this.array[indexLast] = null;
        size--;
        last--;
        return item;
    }
    // x x x f l x x x
    // f f f f l l f f, first = 5, position of first = 6, index = 2
    public T get(int index) {
        if (this.size == 0) {
            return null;
        }
        if (index >= this.array.length) {
            return null;
        }
        int indexWithRespectToFirst = (first + 1 + index) % this.array.length;
        return this.array[indexWithRespectToFirst];
    }
}
