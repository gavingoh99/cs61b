import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> table = new HashMap<>();
        for (char character: inputSymbols) {
            int freq = table.getOrDefault(character, 0);
            table.put(character, freq + 1);
        }
        return table;
    }
    public static void main(String[] args) {
        String filename = args[0];
        String newFileName = filename + ".huf";
        char[] inputSymbols = FileUtils.readFile(filename);
        Map<Character, Integer> freqTable = HuffmanEncoder.buildFrequencyTable(inputSymbols);
        BinaryTrie trie = new BinaryTrie(freqTable);
        ObjectWriter writer = new ObjectWriter(newFileName);
        writer.writeObject(trie);
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> list = new ArrayList<>();
        for (char symbol: inputSymbols) {
            BitSequence bit = lookupTable.get(symbol);
            list.add(bit);
        }
        BitSequence cumulative = BitSequence.assemble(list);
        writer.writeObject(cumulative);
    }
}
