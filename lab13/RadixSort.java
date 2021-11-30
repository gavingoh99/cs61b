import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    public static void main(String[] args) {
        String[] test1 = new String[10];
        test1[0] = "cat";
        test1[1] = "dog";
        test1[2] = "person";
        test1[3] = "hello";
        test1[4] = "yoyo";
        test1[5] = "yoda";
        test1[6] = "teletubbies";
        test1[7] = "believe";
        test1[8] = "nutella";
        test1[9] = "arcadia";
        long start = System.nanoTime();
        String[] sorted1 = RadixSort.sort(test1);
        long end = System.nanoTime();
        System.out.println(end - start);
        for (int i = 0; i < test1.length; i++) {
            System.out.print(test1[i] + " ");
        }
        System.out.println("");
        for (int i = 0; i < sorted1.length; i++) {
            System.out.print(sorted1[i] + " ");
        }
        System.out.println("");
        String[] test2 = new String[6];
        test2[0] = "cat";
        test2[1] = "dog";
        test2[2] = "yoy";
        test2[3] = "yay";
        test2[4] = "dig";
        test2[5] = "cut";
        String[] sorted2 = RadixSort.sort(test2);
        for (int i = 0; i < test2.length; i++) {
            System.out.print(test2[i] + " ");
        }
        System.out.println("");
        for (int i = 0; i < sorted2.length; i++) {
            System.out.print(sorted2[i] + " ");
        }
    }
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // find the longest word
        String[] copy = new String[asciis.length];
        int length = 0;
        for (String string: asciis) {
            if (string.length() > length) {
                length = string.length();
            }
        }
        // pad the other words to be equal length
        for (int index = 0; index < asciis.length; index++) {
            int diff = length - asciis[index].length();
            copy[index] = asciis[index];
            for (int i = 0; i < diff; i++) {
                copy[index] += " ";
            }

        }
        for (int index = 0; index < length; index++) {
            sortHelperLSD(copy, index);
        }
//        sortHelperMSD(copy, 0, copy.length, 0);
        Arrays.stream(copy).map(String::trim).toArray(unused -> copy);

        return copy;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        String[] copy = new String[asciis.length];
        int[] counts = new int[256];
        int counter = 0;
        for (String string: asciis) {
            char character = string.charAt(string.length() - 1 - index);
            if (character == ' ') {
                counts[0]++;
            } else {
                int radix = character;
                counts[radix]++;
            }
            copy[counter] = string;
            counter++;
        }
        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < starts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }
        for (int i = 0; i < asciis.length; i++) {
            String item = copy[i];
            char character = item.charAt(item.length() - 1 - index);
            int position;
            if (character == ' ') {
                position = starts[0];
                asciis[position] = item;
                starts[0]++;
            } else {
                position = starts[character];
                asciis[position] = item;
                starts[character]++;
            }
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        if (end - start == 1) {
            return;
        }
        String[] copy = new String[end - start];
        int[] counts = new int[256];
        int counter = 0;
        for (int i = start; i < end; i++) {
            String string = asciis[i];
            char character = string.charAt(index);
            if (character == ' ') {
                counts[0]++;
            } else {
                int radix = character;
                counts[radix]++;
            }
            copy[counter] = string;
            counter++;
        }
        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < starts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }
        for (int i = 0; i < copy.length; i++) {
            String item = copy[i];
            char character = item.charAt(index);
            int position;
            if (character == ' ') {
                position = starts[0];
                asciis[position + start] = item;
                starts[0]++;
            } else {
                position = starts[character];
                asciis[position + start] = item;
                starts[character]++;
            }
        }
        int startIndex = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] != 0) {
                sortHelperMSD(asciis, startIndex, starts[i], index + 1);
                startIndex = starts[i];
            }
        }
    }
}
