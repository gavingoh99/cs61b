import java.util.ArrayList;
import java.util.List;

public class Word implements Cloneable {
    private List<Node> nodes;
    private String string;
    public Word() {
        this.nodes = new ArrayList<>();
        this.string = "";
    }
    public Word(Node node) {
        this();
        this.extend(node);
    }
    public void extend(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot extend null node");
        }
        if (nodes.contains(node)) {
            return;
        }
        nodes.add(node);
        this.string += node.getLetter();
    }
    public boolean contains(Node node) {
        return nodes.contains(node);
    }
    public Node tail() {
        return nodes.get(nodes.size() - 1);
    }
    public String getWord() {
        return this.string;
    }
    @Override
    public Object clone() {
        Word word = new Word();
        for (Node node: this.nodes) {
            word.nodes.add(node);
        }
        word.string = this.string;
        return word;
    }
}
