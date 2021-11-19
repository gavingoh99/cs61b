import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }
    @Test
    public void testIsPalindrome() {
        //test a palindrome
        assertTrue(palindrome.isPalindrome("noon"));
        //test isPalindrome ignores upper and lower case
        assertTrue(palindrome.isPalindrome("RaCecAr"));
        //test a word that is not a palindrome
        assertFalse(palindrome.isPalindrome("horse"));
        //test word with one character
        assertTrue(palindrome.isPalindrome("a"));
        //test word with no characters
        assertTrue(palindrome.isPalindrome(""));
    }
    @Test
    public void testIsPalindromeWithComparator() {
        CharacterComparator cc = new OffByOne();
        //test a palindrome that is off by one
        assertTrue(palindrome.isPalindrome("flake", cc));
        //test isPalindrome ignores upper and lower case
        assertTrue(palindrome.isPalindrome("FlaKe", cc));
        //test a word that is not a palindrome off by one
        assertFalse(palindrome.isPalindrome("noon", cc));
        //test word with one character
        assertTrue(palindrome.isPalindrome("a", cc));
        //test word with no characters
        assertTrue(palindrome.isPalindrome("", cc));
    }
}
