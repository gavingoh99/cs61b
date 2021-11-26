package hw3.hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        Map<Integer, Integer> buckets = new HashMap<>();
        for (Oomage oomage: oomages) {
            int bucketNum = (oomage.hashCode() & 0x7FFFFFFF) % M;
            int currNumberOfOomages = buckets.getOrDefault(bucketNum, 0);
            buckets.put(bucketNum, currNumberOfOomages + 1);
        }
        for (int number: buckets.values()) {
            if (number < oomages.size() / 50 || number > oomages.size() / 2.5) {
                return false;
            }
        }
        return true;
    }
}
