/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    private Palindrome palindrome = new Palindrome();
    private int minLength = 4;
    public static void main(String[] args) {
        PalindromeFinder finder = new PalindromeFinder();
        int greatestNumberOfWords = 0;
        int greatestN = 0;
        for(int diff = 0; diff < 26; diff++) {
            int currNumber = finder.wordsDifferByN(diff);
            if (currNumber > greatestNumberOfWords) {
                greatestNumberOfWords = currNumber;
                greatestN = diff;
            }
        }
        System.out.println("Greatest number of palindromes: " + greatestNumberOfWords);
        System.out.println("Occurs when N is: " + greatestN);

    }
    public int wordsDifferByN(int N) {
        In in = new In("../library-sp18/data/words.txt");
        CharacterComparator cc = new OffByN(N);
        int sum = 0;
        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
                sum++;
            }
        }
        return sum;
    }
}
