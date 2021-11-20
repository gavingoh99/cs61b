package byog.lab5;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Hexagon {
    public static void addHexagon(TETile[][] world, int s, int x, int y) {
        int increments = s - 1;
        int rowsAtMiddle = 3 * s - 2;
        int height = 2 * s;
        TETile tile = getRandomTile();
        addRow(world, s, x + increments, y, tile);
        addIntermediateRows(world, s + 2, x + increments - 1, y + 1, height - 4, tile);
        addMiddleRows(world, rowsAtMiddle, x, y + increments, tile);
        addRow(world, s, x + increments, y + height - 1, tile);
    }
    public static void addRow(TETile[][] world, int numberOfTiles, int startX, int height, TETile tile) {
        for (int currX = startX; currX <= startX + numberOfTiles - 1; currX++) {
            world[currX][height] = tile;
        }
    }
    public static void addIntermediateRows(TETile[][] world, int s, int x, int y, int distanceBetweenIntermediates, TETile tile) {
        if (distanceBetweenIntermediates == 0) {
            return;
        }
        addRow(world, s, x, y, tile);
        addRow(world, s, x, y + distanceBetweenIntermediates + 1, tile);
        addIntermediateRows(world, s + 2, x - 1, y + 1, distanceBetweenIntermediates - 2, tile);
    }
    public static void addMiddleRows(TETile[][] world, int numberOfTiles, int x, int y, TETile tile) {
        addRow(world, numberOfTiles, x, y, tile);
        addRow(world, numberOfTiles, x, y + 1, tile);
    }
    public static TETile getRandomTile() {
        Random rand = new Random();
        int number = rand.nextInt(9);
        switch (number) {
            case 0: return Tileset.PLAYER;
            case 1: return Tileset.WALL;
            case 2: return Tileset.FLOOR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.WATER;
            case 5: return Tileset.FLOWER;
            case 6: return Tileset.UNLOCKED_DOOR;
            case 7: return Tileset.SAND;
            case 8: return Tileset.MOUNTAIN;
            default: return Tileset.TREE;
        }
    }
}
