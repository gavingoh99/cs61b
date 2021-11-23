package byog.lab5;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Hexagon {
    private static TETile tile;
    public static void addHexagon(int s, int x, int y) {
        int increments = s - 1;
        int rowsAtMiddle = 3 * s - 2;
        int height = 2 * s;
        tile = getRandomTile();
        addRow(s, x + increments, y, height - 2);
        addIntermediateRows(s + 2, x + increments - 1, y + 1, height - 4);
        addMiddleRows(rowsAtMiddle, x, y + increments);
    }
    public static void addRow(int numberOfTiles, int startX, int height, int distBtwRows) {
        for (int currX = startX; currX <= startX + numberOfTiles - 1; currX++) {
            HexWorld.world[currX][height] = tile;
            HexWorld.world[currX][height + distBtwRows + 1] = tile;
        }
    }
    public static void addIntermediateRows(int s, int x, int y, int distBtwInter) {
        if (distBtwInter == 0) {
            return;
        }
        addRow(s, x, y, distBtwInter);
        addIntermediateRows(s + 2, x - 1, y + 1, distBtwInter - 2);
    }
    public static void addMiddleRows(int numberOfTiles, int x, int y) {
        addRow(numberOfTiles, x, y, 0);
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
