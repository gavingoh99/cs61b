import java.lang.Math;
public class OffByN implements CharacterComparator {
    private int diff;
    public OffByN(int diff) {
        this.diff = diff;
    }
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == this.diff) {
            return true;
        }
        return false;
    }
}
