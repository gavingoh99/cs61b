import java.util.HashSet;
import java.util.Set;

public class Node {
    private char letter;
    private Set<Node> neighbours;
    public Node(char letter) {
        this.letter = letter;
        this.neighbours = new HashSet<>();
    }
    public char getLetter() {
        return this.letter;
    }
    public void addNeighbour(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot add null neighbour");
        }
        if (this.equals(node)) {
            return;
        }
        this.neighbours.add(node);
    }
    public Set<Node> getNeighbours() {
        return this.neighbours;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
