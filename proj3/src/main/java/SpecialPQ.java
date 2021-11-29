public class SpecialPQ {
    private GraphDB.Node[] array;
    private int size;
    public SpecialPQ(int capacity) {
        this.array = new GraphDB.Node[capacity + 1];
        this.array[0] = null;
        this.size = 0;
    }
    public int parentIndex(int index) {
        return index / 2;
    }
    public int leftIndex(int index) {
        return index * 2;
    }
    public int rightIndex(int index) {
        return index * 2 + 1;
    }
    public GraphDB.Node getNode(int index) {
        return this.array[index];
    }
    public void swap(int index1, int index2) {
        GraphDB.Node node1 = getNode(index1);
        GraphDB.Node node2 = getNode(index2);
        this.array[index1] = node2;
        this.array[index2] = node1;
    }
    public int min(int index1, int index2) {
        GraphDB.Node node1 = getNode(index1);
        GraphDB.Node node2 = getNode(index2);
        if (node1 == null) {
            return index2;
        } else if (node2 == null) {
            return index1;
        }
        if (node1.getPriority() < node2.getPriority()) {
            return index1;
        } else {
            return index2;
        }
    }
    public void insert(GraphDB.Node node) {
        int index = size + 1;
        this.array[index] = node;
        swim(index);
        size++;
    }
    public void swim(int index) {
        while (index > 1 && min(index, index / 2) == index) {
            swap(index, index / 2);
            index = index / 2;
        }
    }
    public void sink(int index) {
        while (2 * index <= size) {
            int largerIndex = leftIndex(index);
            if (largerIndex < size && min(largerIndex, largerIndex + 1) == largerIndex + 1) {
                largerIndex++;
            }
            if (min(index, largerIndex) == index) {
                break;
            }
            swap(index, largerIndex);
            index = largerIndex;
        }
    }
    public void changePriority(GraphDB.Node node, double priority) {
        double prevPriority = -1;
        int indexAt = 0;
        for (int index = 1; index <= size; index++) {
            if (this.array[index].equals(node)) {
                indexAt = index;
                break;
            }
        }
        prevPriority = node.getPriority();
        node.setPriority(priority);
        if (prevPriority == -1) {
            return;
        }
        if (priority > prevPriority) {
            sink(indexAt);
        } else {
            swim(indexAt);
        }
    }
    public GraphDB.Node removeMin() {
        GraphDB.Node min = this.array[1];
        this.array[1] = null;
        swap(1, size);
        size--;
        sink(1);
        return min;
    }
    public int size() {
        return this.size;
    }
    public boolean isEmpty() {
        return this.size == 0;
    }

}
