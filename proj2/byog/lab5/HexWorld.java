package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    //size 2 - start 2, middle 4, total rows = 4
    //size 3 - start 3, middle 7, total rows = 6
    //size 4 - start 4, middle 10, total rows = 8
    //notice each row differ by 2
    //2 - no. increments = 1
    //3 - '' = 2
    //4 - '' = 3
    //generally no. increments = size - 1

    //consider size 2:
    //actually a quadrilateral of width = middle,
    //height = total rows
    //start position = [0][1] stop position [0][2]
    //middle [1][0] thru [1][3] and [2][0] thru [2][3]
    //bottom row [3][1], [3][2] relative to origin [0][0]
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        tesselateHex(world, 2, 5, 2);
        ter.renderFrame(world);
    }
    public static void tesselateHex(TETile[][] world, int s, int numberOfHexInCenter, int numberOfHexAtSides) {
        int distanceFromEdgeToStartX = 0;
        int heightOfOneHex = 2 * s;
        int numberOfStacks = 1 + (numberOfHexInCenter - numberOfHexAtSides) * 2;
        int widthOfStructure = 0;
        int heightOfStructure = numberOfHexInCenter * heightOfOneHex;
        for (int stack = 0; stack < numberOfStacks; stack++) {
            if (stack == (int) ((numberOfStacks - 1) / 2)) {
                distanceFromEdgeToStartX = widthOfStructure - s + 1;
            }
            if (stack % 2 == 0) {
                widthOfStructure += 3 * s - 2;
            } else {
                widthOfStructure += s;
            }
        }
        int emptySpaceAtSides = (int) ((WIDTH - widthOfStructure) / 2);
        int emptySpaceAtTopBottom = (int) ((HEIGHT - heightOfStructure) / 2);
        int startX = distanceFromEdgeToStartX + emptySpaceAtSides;
        int startY = emptySpaceAtTopBottom;
        addCentralHexagonStack(world, s, startX, startY, numberOfHexInCenter);
        addSideHexagonStacks(world, s, startX - 2 * s + 1, startY + s, numberOfHexInCenter - 1, numberOfHexAtSides, 1);
    }
    public static void addCentralHexagonStack(TETile[][] world, int s, int x, int y, int numberOfHex) {
        addHexagonStack(world, s, x, y, numberOfHex);
    }
    public static void addSideHexagonStacks(TETile[][] world, int s, int x, int y, int numberOfHex, int numberOfHexAtSides, int rowFromCenter) {
        if (numberOfHex < numberOfHexAtSides) {
            return;
        }
        addHexagonStack(world, s, x, y, numberOfHex);
        addHexagonStack(world, s, x + (4 * s - 2) * rowFromCenter, y, numberOfHex);
        addSideHexagonStacks(world, s, x - 2 * s + 1, y + s, numberOfHex - 1, numberOfHexAtSides, rowFromCenter + 1);
    }
    public static void addHexagonStack(TETile[][] world, int s, int x, int y, int numberOfHex) {
        for (int currNumberHex = 0; currNumberHex < numberOfHex; currNumberHex++) {
            addHexagon(world, s, x, y);
            y += 2 * s;
        }
    }
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
            case 9: return Tileset.TREE;
            default: return Tileset.PLAYER;
        }
    }
}
