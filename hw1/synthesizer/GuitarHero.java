package synthesizer;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    private static GuitarString[] strings;
    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public GuitarHero() {
        this.strings = new GuitarString[37];
        for (int index = 0; index < 37; index++) {
            double frequency = 440 * java.lang.Math.pow(2, (index - 24) / 12);
            strings[index] = new synthesizer.GuitarString(frequency);
        }
    }
    public static void main(String[] args) {
        GuitarHero data = new GuitarHero();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index > -1) {
                    strings[index].pluck();
                }
            }
            double sample = 0;
            for (GuitarString string: strings) {
                sample += string.sample();
            }
            StdAudio.play(sample);
            for (GuitarString string: strings) {
                string.tic();
            }
        }
    }
}
