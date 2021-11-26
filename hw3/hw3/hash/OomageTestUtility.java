package hw3.hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
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
