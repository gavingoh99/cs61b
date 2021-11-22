package byog.Core;

import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

import static byog.Core.MapGenerator.HEIGHT;
import static byog.Core.MapGenerator.WIDTH;


public class Game {
    static Random rand;
    static Player player;
    static TETile[][] world;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     * @return
     */
    public void playWithKeyboard() {
        UserInterface.drawMainMenu();

        while (true) {
            char keyPressed = waitCommand();
            if (keyPressed == 'n') {
                long seed = UserInterface.askForSeed();
                world = playWithInputString(Long.toString(seed));
                World.renderWorld(world);
                play(world);
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
        world = World.createEmptyWorld();
        Game.rand = getSeed(input);
        int numberOfRooms = rand.nextInt(15) + 5;
        Room[] rooms = new Room[numberOfRooms];
        MapGenerator.generateRooms(world, numberOfRooms, rooms);
        PathGenerator.drawAll(world, numberOfRooms, rooms);
        player = new Player(PathGenerator.randomCoordinate(rooms[0]));
        player.enterWorld(world);
        MapGenerator.fillAll(world);
        return world;
    }

    public static Random getSeed(String input) {
        long seed = 0;
        input = input.toUpperCase();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                seed = 10 * seed + Long.parseLong("" + input.charAt(i));
            }
        }
        return new Random(seed);
    }

    public static char waitCommand() {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(10);
        }
        return StdDraw.nextKeyTyped();
    }

    public static char waitForControlKey() {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(10);
            mouseTile();
        }
        return StdDraw.nextKeyTyped();
    }
    public static void mouseTile() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int w = (int) Math.floorDiv((long) x, 1);
        int h = (int) Math.floorDiv((long) y, 1);
        if (h >= HEIGHT) {
            h = HEIGHT - 1;
        }
        if (w >= WIDTH) {
            w = WIDTH - 1;
        }
        TETile tile = world[w][h];
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT, WIDTH / 2, 1);
        StdDraw.setPenColor(Color.PINK);
        StdDraw.textLeft(1, HEIGHT, tile.description());
        StdDraw.show();
    }
    private void play(TETile[][] world) {
        while (true) {
            char command = waitForControlKey();
            if (command == 'w') {
                MapGenerator.moveUp(world, player);
                System.out.println("hello");
            }
            if (command == 'a') {
                MapGenerator.moveLeft(world, player);
            }
            if (command == 's') {
                MapGenerator.moveDown(world, player);
            }
            if (command == 'd') {
                MapGenerator.moveRight(world, player);
            }
        }
    }
}
