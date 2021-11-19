public class LinkedListDeque<Item> implements Deque<Item> {
    private class Node {
        Node prev;
        Item item;
        Node next;
        Node(Node prev, Item item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }
    private Node sentinel;
    private int size;
    public LinkedListDeque() {
        this.sentinel = new Node(null, null, null);
        this.sentinel.next = this.sentinel;
        this.sentinel.prev = this.sentinel;
        this.size = 0;
    }
    @Override
    public void addFirst(Item item) {
        Node toBeAdded = new Node(this.sentinel, item, this.sentinel.next);
        this.sentinel.next.prev = toBeAdded;
        this.sentinel.next = toBeAdded;
        this.size++;
    }
    @Override
    public void addLast(Item item) {
        Node toBeAdded = new Node(this.sentinel.prev, item, this.sentinel);
        this.sentinel.prev.next = toBeAdded;
        this.sentinel.prev = toBeAdded;
        this.size++;
    }
    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }
    @Override
    public int size() {
        return this.size;
    }
    @Override
    public void printDeque() {
        Node pointer = this.sentinel.next;
        while (pointer != this.sentinel) {
            System.out.println(pointer.item + " ");
            pointer = pointer.next;
        }
    }
    @Override
    public Item removeFirst() {
        if (this.size == 0) {
            return null;
        }
        Node toBeRemoved = this.sentinel.next;
        Item item = toBeRemoved.item;
        toBeRemoved.next.prev = this.sentinel;
        this.sentinel.next = toBeRemoved.next;
        size--;
        return item;
    }
    @Override
    public Item removeLast() {
        if (this.size == 0) {
            return null;
        }
        Node toBeRemoved = this.sentinel.prev;
        Item item = toBeRemoved.item;
        toBeRemoved.prev.next = this.sentinel;
        this.sentinel.prev = toBeRemoved.prev;
        size--;
        return item;
    }
    @Override
    public Item get(int index) {
        if (this.size == 0) {
            return null;
        }
        if (index >= size) {
            return null;
        }
        Node pointer = this.sentinel.next;
        while (pointer != this.sentinel) {
            if (index == 0) {
                return pointer.item;
            }
            pointer = pointer.next;
            index--;
        }
        return null;
    }
    private Item getHelper(int index, Node current) {
        if (current == this.sentinel) {
            return null;
        }
        if (index == 0) {
            return current.item;
        }
        return getHelper(index - 1, current.next);
    }
    public Item getRecursive(int index) {
        return getHelper(index, this.sentinel.next);
    }
}
