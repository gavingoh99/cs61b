import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        String source = args[0];
        String target = args[1];
        ObjectReader reader = new ObjectReader(source);
        BinaryTrie trie = (BinaryTrie) reader.readObject();
        BitSequence cumulative = (BitSequence) reader.readObject();
        List<Character> symbols = new ArrayList<>();
        while (cumulative.length() != 0) {
            Match curr = trie.longestPrefixMatch(cumulative);
            if (curr == null) {
                break;
            }
            char character = curr.getSymbol();
            symbols.add(character);
            int length = curr.getSequence().length();
            cumulative = cumulative.allButFirstNBits(length);
        }
        char[] array = new char[symbols.size()];
        for (int index = 0; index < symbols.size(); index++) {
            array[index] = symbols.get(index);
        }
        FileUtils.writeCharArray(target, array);
    }
}
