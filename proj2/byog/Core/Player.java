package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Player {
    private int x;
    private int y;
    public Player(Coordinate coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void enterWorld(TETile[][] world) {
        world[this.x][this.y] = Tileset.PLAYER;
    }
    public void moveUp(TETile[][] world) {
        if (world[x][y + 1] != Tileset.FLOOR) {
            return;
        }
        world[x][y + 1] = Tileset.PLAYER;
        world[x][y] = Tileset.FLOOR;
        this.y = y + 1;
    }
}
