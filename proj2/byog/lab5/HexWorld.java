package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


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
    private static final int WIDTH = 75;
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

        HexagonStructureBuilder builder = new HexagonStructureBuilder(world, 4, 5, 1);
        builder.tesselateHex();
        ter.renderFrame(world);
    }
    private static class HexagonStructureBuilder {
        private TETile[][] world;
        private int sizeOfHexagon;
        private int numberOfHexInCenter;
        private int numberOfHexAtSides;
        HexagonStructureBuilder(TETile[][] world, int sizeOfHexagon, int numberOfHexInCenter, int numberOfHexAtSides) {
            this.world = world;
            this.sizeOfHexagon = sizeOfHexagon;
            this.numberOfHexInCenter = numberOfHexInCenter;
            this.numberOfHexAtSides = numberOfHexAtSides;
        }
        public void tesselateHex() {
            int distanceFromEdgeToStartX = 0;
            int heightOfOneHex = 2 * sizeOfHexagon;
            int numberOfStacks = 1 + (numberOfHexInCenter - numberOfHexAtSides) * 2;
            int widthOfStructure = 0;
            int heightOfStructure = numberOfHexInCenter * heightOfOneHex;
            for (int stack = 0; stack < numberOfStacks; stack++) {
                if (stack == (int) ((numberOfStacks - 1) / 2)) {
                    distanceFromEdgeToStartX = widthOfStructure;
                }
                if (stack % 2 == 0) {
                    widthOfStructure += 3 * sizeOfHexagon - 2;
                } else {
                    widthOfStructure += sizeOfHexagon;
                }
            }
            int emptySpaceAtSides = (int) ((WIDTH - widthOfStructure) / 2);
            int emptySpaceAtTopBottom = (int) ((HEIGHT - heightOfStructure) / 2);
            int startX = distanceFromEdgeToStartX + emptySpaceAtSides;
            int startY = emptySpaceAtTopBottom;
            addCentralHexagonStack(startX, startY, numberOfHexInCenter);
            addSideHexagonStacks( startX - 2 * sizeOfHexagon + 1, startY + sizeOfHexagon, numberOfHexInCenter - 1, numberOfHexAtSides, 1);
        }
        public void addCentralHexagonStack(int x, int y, int numberOfHex) {
            addHexagonStack(x, y, numberOfHex);
        }
        public void addSideHexagonStacks(int x, int y, int numberOfHex, int numberOfHexAtSides, int rowFromCenter) {
            if (numberOfHex < numberOfHexAtSides) {
                return;
            }
            addHexagonStack(x, y, numberOfHex);
            addHexagonStack(x + (4 * sizeOfHexagon - 2) * rowFromCenter, y, numberOfHex);
            addSideHexagonStacks(x - 2 * sizeOfHexagon + 1, y + sizeOfHexagon, numberOfHex - 1, numberOfHexAtSides, rowFromCenter + 1);
        }
        public void addHexagonStack(int x, int y, int numberOfHex) {
            for (int currNumberHex = 0; currNumberHex < numberOfHex; currNumberHex++) {
                Hexagon.addHexagon(world, sizeOfHexagon, x, y);
                y += 2 * sizeOfHexagon;
            }
        }
    }
}
