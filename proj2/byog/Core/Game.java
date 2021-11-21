package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;


public class Game {

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.setCanvasSize();
        StdDraw.clear(Color.BLACK);
        Font tileFont = new Font("Dialog", Font.PLAIN, 24);
        StdDraw.setFont(tileFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(0.5, 0.75, "CS61B: THE GAME");
        StdDraw.setFont();
        StdDraw.text(0.5, 0.54, "New Game (N) ");
        StdDraw.text(0.5, 0.50, "Load Game (L) ");
        StdDraw.text(0.5, 0.46, "Quit (Q) ");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'n') {
                    StdDraw.clear(Color.black);
                    StdDraw.setFont();
                    StdDraw.setPenColor(Color.white);
                    StdDraw.text(.5, .5, "Enter a random seed");
                    StdDraw.show();
                    break;
                }
            }
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        TETile[][] map = MapGenerator.createEmptyWorld();
        long seed = 0;
        input = input.toUpperCase();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'N') {
                continue;
            }
            if (input.charAt(i) == 'S' ) {
                MapGenerator.generateMap(seed, map);
                continue;
            }
            seed = 10 * seed + Long.parseLong("" + input.charAt(i));
        }
        return map;
    }
}
