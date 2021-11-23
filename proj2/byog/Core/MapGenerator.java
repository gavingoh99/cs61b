package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class MapGenerator {
    static final int HEIGHT = 40;
    static final int WIDTH = 80;

    public static void generateRooms(TETile[][] world, int numberOfRooms, Room[] rooms) {
        Room.createRooms(rooms);
        Room.drawAll(world, numberOfRooms, rooms);
        Room.fillAll(world, numberOfRooms, rooms);
    }
    public static void fillAll(TETile[][] world) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (world[x][y] == Tileset.FLOOR) {
                    if (world[x + 1][y] == Tileset.NOTHING) {
                        world[x + 1][y] = Tileset.WALL;
                    }
                    if (world[x - 1][y] == Tileset.NOTHING) {
                        world[x - 1][y] = Tileset.WALL;
                    }
                    if (world[x][y + 1] == Tileset.NOTHING) {
                        world[x][y + 1] = Tileset.WALL;
                    }
                    if (world[x][y - 1] == Tileset.NOTHING) {
                        world[x][y - 1] = Tileset.WALL;
                    }
                    if (world[x + 1][y + 1] == Tileset.NOTHING) {
                        world[x + 1][y + 1] = Tileset.WALL;
                    }
                    if (world[x - 1][y + 1] == Tileset.NOTHING) {
                        world[x - 1][y + 1] = Tileset.WALL;
                    }
                    if (world[x + 1][y - 1] == Tileset.NOTHING) {
                        world[x + 1][y - 1] = Tileset.WALL;
                    }
                    if (world[x - 1][y - 1] == Tileset.NOTHING) {
                        world[x - 1][y - 1] = Tileset.WALL;
                    }
                }
            }
        }
    }
    public static void moveUp(TETile[][] world, Player player, boolean isKeyboard) {
        int x = player.getX();
        int y = player.getY();
        if (world[x][y + 1].description().equals("floor")) {
            world[x][y] = Tileset.FLOOR;
            world[x][y + 1] = Tileset.PLAYER;
            if (isKeyboard) {
                world[x][y].draw(x, y);
                world[x][y + 1].draw(x, y + 1);
            }
            player.setY(y + 1);
            System.out.println(player.getY());
        }
    }
    public static void moveLeft(TETile[][] world, Player player, boolean isKeyboard) {
        int x = player.getX();
        int y = player.getY();
        if (world[x - 1][y].description().equals("floor")) {
            world[x][y] = Tileset.FLOOR;
            world[x - 1][y] = Tileset.PLAYER;
            if (isKeyboard) {
                world[x][y].draw(x, y);
                world[x - 1][y].draw(x - 1, y);
            }
            player.setX(x - 1);
        }
    }
    public static void moveDown(TETile[][] world, Player player, boolean isKeyboard) {
        int x = player.getX();
        int y = player.getY();
        if (world[x][y - 1].description().equals("floor")) {
            world[x][y] = Tileset.FLOOR;
            world[x][y - 1] = Tileset.PLAYER;
            if (isKeyboard) {
                world[x][y].draw(x, y);
                world[x][y - 1].draw(x, y - 1);
            }
            player.setY(y - 1);
        }
    }
    public static void moveRight(TETile[][] world, Player player, boolean isKeyboard) {
        int x = player.getX();
        int y = player.getY();
        if (world[x + 1][y].description().equals("floor")) {
            world[x][y] = Tileset.FLOOR;
            world[x + 1][y] = Tileset.PLAYER;
            if (isKeyboard) {
                world[x][y].draw(x, y);
                world[x + 1][y].draw(x + 1, y);
            }
            player.setX(x + 1);
        }
    }
}
