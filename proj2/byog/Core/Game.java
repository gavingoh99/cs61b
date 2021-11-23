package byog.Core;

import byog.TileEngine.TETile;
// import the right StdDraw if not
// you'll encounter problems responding to
// keyboard inputs with hasNextKeyPressed()
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static byog.Core.MapGenerator.HEIGHT;
import static byog.Core.MapGenerator.WIDTH;


public class Game implements Serializable {
    static Random rand;
    static Player player;
    static TETile[][] world;
    static int seedSize;

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
                play();
            }
            if (keyPressed == 'l') {
                loadGameState();
                World.renderWorld(world);
                play();
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
        input = input.toLowerCase();
        if (input.charAt(0) == 'l') {
            loadGameState();
            processStringForMovement(input.substring(1));
            return world;
        }
        world = World.createEmptyWorld();
        Game.rand = getSeed(input);
        int numberOfRooms = rand.nextInt(15) + 5;
        Room[] rooms = new Room[numberOfRooms];
        MapGenerator.generateRooms(world, numberOfRooms, rooms);
        PathGenerator.drawAll(world, numberOfRooms, rooms);
        player = new Player(PathGenerator.randomCoordinate(rooms[0]));
        player.enterWorld(world);
        MapGenerator.fillAll(world);
        if (input.length() > seedSize + 2) {
            processStringForMovement(input.substring(seedSize + 2));
        }
        return world;
    }
    private static void processStringForMovement(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (input.charAt(index) == ':') {
                if (input.charAt(index + 1) == 'q') {
                    saveGameState();
                }
            }
            if (input.charAt(index) == 'w') {
                MapGenerator.moveUp(world, player, false);
            }
            if (input.charAt(index) == 'a') {
                MapGenerator.moveLeft(world, player, false);
            }
            if (input.charAt(index) == 's') {
                MapGenerator.moveDown(world, player, false);
            }
            if (input.charAt(index) == 'd') {
                MapGenerator.moveRight(world, player, false);
            }
        }
    }

    public static Random getSeed(String input) {
        long seed = 0;
        seedSize = 0;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                seed = 10 * seed + Long.parseLong("" + input.charAt(i));
                seedSize++;
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
            trackMouse();
        }
        return StdDraw.nextKeyTyped();
    }
    public static void trackMouse() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int w = (int) x;
        int h = (int) y;
        if (h >= HEIGHT) {
            h = HEIGHT - 1;
        }
        if (w >= WIDTH) {
            w = WIDTH - 1;
        }
        TETile tile = world[w][h];
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT, WIDTH / 2, 1);
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, HEIGHT, tile.description());
        StdDraw.show();
    }
    private void play() {
        while (true) {
            char command = waitForControlKey();
            if (command == ':') {
                char followUp = waitForControlKey();
                if (followUp == 'q') {
                    saveGameState();
                    playWithKeyboard();
                }
            }
            if (command == 'w') {
                MapGenerator.moveUp(world, player, true);
            }
            if (command == 'a') {
                MapGenerator.moveLeft(world, player, true);
            }
            if (command == 's') {
                MapGenerator.moveDown(world, player, true);
            }
            if (command == 'd') {
                MapGenerator.moveRight(world, player, true);
            }
        }
    }
    private static void saveGameState() {
        String fileName = "saves.txt";
        try {
            FileOutputStream fileStream = new FileOutputStream(fileName);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(rand);
            objectStream.writeObject(world);
            objectStream.writeObject(player);
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadGameState() {
        String fileName = "saves.txt";
        try {
            FileInputStream fileStream = new FileInputStream(fileName);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            rand = (Random) objectStream.readObject();
            world = (TETile[][]) objectStream.readObject();
            player = (Player) objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
