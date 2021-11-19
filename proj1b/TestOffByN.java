import org.junit.Test;
import static org.junit.Assert.*;
public class TestOffByN {
    @Test
    public void testEqualCharsForDifferentN() {
        OffByN differBy4 = new OffByN(4);
        assertTrue(differBy4.equalChars('a', 'e'));

        OffByN differBy15 = new OffByN(15);
        assertTrue(differBy15.equalChars('a', 'p'));
    }
}
