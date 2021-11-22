package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import static byog.Core.MapGenerator.HEIGHT;
import static byog.Core.MapGenerator.WIDTH;

public class World {
    public static TETile[][] createEmptyWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }
    public static void renderWorld(TETile[][] world) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 1);
        ter.renderFrame(world);
    }
}
