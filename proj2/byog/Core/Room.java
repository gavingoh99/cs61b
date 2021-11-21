package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byog.Core.MapGenerator.*;

public class Room {
    private static TETile[][] map;
    public Room(TETile[][] map) {
        this.map = map;
    }
    public static void buildRoom(int startX, int startY, String seed) {
        Random rand = new Random(Integer.parseInt(seed));
        int randomWidth = rand.nextInt(6) + 5;
        int randomHeight = rand.nextInt(9) + 5;
        if (startX + randomWidth >= WIDTH || startY + randomHeight >= HEIGHT) {
            return;
        }
        List<Coordinate> roomCoordinates = new ArrayList<>();
        for (int y = startY; y < startY + randomHeight; y++) {
            for (int x = startX; x < startX + randomHeight; x++) {
                roomCoordinates.add(new Coordinate(x, y));
            }
        }
        for (Coordinate coordinate: roomCoordinates) {
            if (allCoordinates.contains(coordinate)) {
                return;
            }
        }
        roomsCoordinates.add(new Coordinate(startX + 1, startY + 1));
        for (int y = startY; y < startY + randomHeight; y++) {
            if (y == startY || y == startY + randomHeight - 1) {
                for (int x = startX; x < startX + randomWidth; x++) {
                    map[x][y] = Tileset.WALL;
                    allCoordinates.add(new Coordinate(x, y));
                }
            } else {
                for (int x = startX + 1; x < startX + randomWidth - 1; x++) {
                    map[x][y] = Tileset.FLOOR;
                    allCoordinates.add(new Coordinate(x, y));
                }
                map[startX][y] = Tileset.WALL;
                map[startX + randomWidth - 1][y] = Tileset.WALL;
                allCoordinates.add(new Coordinate(startX, y));
                allCoordinates.add(new Coordinate(startX + randomWidth - 1, y));
            }
        }
    }
}
