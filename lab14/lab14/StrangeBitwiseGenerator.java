package lab14;
import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private double period;
    private int state;
    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
    }
    @Override
    public double next() {
        state = state + 1;
        int weirdState = state & (state >>> 3) & (int) ((state >>> 8) % period);
        return normalize(weirdState);
    }
    private double normalize(int state) {
        return ((double) state / (period / 2)) - 1;
    }
}
