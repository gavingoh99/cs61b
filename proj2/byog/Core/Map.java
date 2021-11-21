package byog.Core;

import static byog.Core.MapGenerator.HEIGHT;
import static byog.Core.MapGenerator.WIDTH;

public class Map {
    private int[][] map = new int[HEIGHT][WIDTH];
    private boolean[][] visited = new boolean[HEIGHT][WIDTH];
    private Coordinate start;
    private Coordinate end;
    public Map(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        for (int y = 0; y < HEIGHT; y++) {
            if (y == 0 || y == HEIGHT - 1) {
                for (int x = 0; x < WIDTH; x++) {
                    map[y][x] = 1;
                }
            }
            map[y][0] = 1;
            map[y][WIDTH - 1] = 1;
        }
    }

    public int getHeight() {
        return map.length;
    }
    public int getWidth() {
        return map[0].length;
    }
    public Coordinate getStart() {
        return this.start;
    }
    public Coordinate getEnd() {
        return this.end;
    }
    public boolean isExit(int x, int y) {
        return x == end.getX() && y == end.getY();
    }
    public boolean isStart(int x, int y) {
        return x == start.getX() && y == start.getY();
    }
    public boolean isExplored(int y, int x) {
        return visited[y][x];
    }
    public boolean isWall(int y, int x) {
        return map[y][x] == 1;
    }
    public void setVisited(int y, int x, boolean value) {
        visited[y][x] = value;
    }
    public boolean isValid(int y, int x) {
        if (y < 0 || y >= this.getHeight() || x < 0 || x >= this.getWidth()) {
            return false;
        }
        return true;
    }
}
