package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byog.Core.MapGenerator.HEIGHT;
import static byog.Core.MapGenerator.WIDTH;

public class UserInterface {
    public static void drawMainMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.black);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 60));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.7, "CS61B: The Game");
        StdDraw.enableDoubleBuffering();
        StdDraw.setFont(new Font("Times New Roman", Font.BOLD, 20));
        StdDraw.text(WIDTH / 2, HEIGHT * 0.4, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.35, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.3, "Quit (Q)");

        StdDraw.show();
    }

    public static long askForSeed() {
        StdDraw.clear(StdDraw.PRINCETON_ORANGE);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.6,
                "Please enter a seed, or press 'r' for a random seed. Press s to confirm: ");
        StdDraw.show();
        char c = Game.waitCommand();
        long seed = 0;
        while (c != 's') {
            if (c == 'r') {
                seed = System.currentTimeMillis();
                break;
            }
            if (Character.isDigit(c)) {
                StdDraw.clear(StdDraw.PRINCETON_ORANGE);
                StdDraw.text(WIDTH / 2, HEIGHT * 0.6,
                        "Please enter a seed, or press 'r' for a random seed. "
                                + "Press s to confirm: ");
                StdDraw.show();
                seed = 10 * seed + Long.parseLong("" + c);
                StdDraw.text(WIDTH / 2, HEIGHT * 0.5, "Seed: " + seed);
                StdDraw.show();
            } else {
                StdDraw.clear(StdDraw.PRINCETON_ORANGE);
                StdDraw.text(WIDTH / 2, HEIGHT * 0.6,
                        "Please enter a seed, or press 'r' for a random seed. "
                                + "Press s to confirm: ");
                StdDraw.show();
                StdDraw.text(WIDTH / 2, HEIGHT * 0.5, "Seed: " + seed);
                StdDraw.text( WIDTH / 2, HEIGHT * 0.4, "numbers only pls");
                StdDraw.show();
            }
            c = Game.waitCommand();
        }
        return seed;
    }
}
