import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars() {
        char char1 = 'x';
        char char2 = 'y';
        char char3 = 'x';
        char char4 = 'Y';
        //test two characters of same char
        assertFalse(offByOne.equalChars(char1, char3));
        //test two characters of different char, separated by one in ascii
        assertTrue(offByOne.equalChars(char1, char2));
        //test two characters of different char, separated by one in ascii but upper case
        assertFalse(offByOne.equalChars(char1, char4));

        assertTrue(offByOne.equalChars('%', '&'));
    }

}
