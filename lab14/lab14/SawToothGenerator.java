package lab14;
import lab14lib.Generator;
public class SawToothGenerator implements Generator {
    private double period;
    private int state;
    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }
    @Override
    public double next() {
        state = state + 1;
        if (state == period) {
            state = 0;
        }
        return normalize(state);
    }
    private double normalize(int state) {
        return ((double) state / (period / 2)) - 1;
    }
}
