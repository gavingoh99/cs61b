package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    static final int HEIGHT = 40;
    static final int WIDTH = 80;
    static List<Coordinate> roomsCoordinates = new ArrayList<>();
    static List<Coordinate> allCoordinates = new ArrayList<>();

    public static void createRooms(TETile[][] map, String seed) {
        new Room(map);
        Random rand = new Random(Integer.parseInt(seed));
        for (int roomCount = 0; roomCount < 30; roomCount++) {
            int randomY = rand.nextInt(HEIGHT);
            int randomX = rand.nextInt(WIDTH);
            Room.buildRoom(randomX, randomY, seed);
        }
    }
    public static void generatePaths(TETile[][] map) {
        for (int index = 1; index < roomsCoordinates.size(); index++) {
            generatePath(roomsCoordinates.get(0), roomsCoordinates.get(index), map);
        }

    }
    private static void generatePath(Coordinate start, Coordinate end, TETile[][] map) {
        Map pathBetweenRooms = new Map(start, end);
        List<Coordinate> coordinates = PathGenerator.solve(pathBetweenRooms);
        for (Coordinate coordinate: coordinates) {
            allCoordinates.add(coordinate);
            map[coordinate.getX()][coordinate.getY()] = Tileset.FLOOR;
            List<Coordinate> peripherals = new ArrayList<>();
            for (int y = coordinate.getY() - 1; y <= coordinate.getY() + 1; y++) {
                for (int x = coordinate.getX() - 1; x <= coordinate.getX() + 1; x++) {
                    if (x != coordinate.getX() && y != coordinate.getY()) {
                        peripherals.add(new Coordinate(x, y));
                    }
                }
            }
            for (Coordinate currCoord: peripherals) {
                if (!coordinates.contains(currCoord) && !allCoordinates.contains(currCoord)) {
                    map[currCoord.getX()][currCoord.getY()] = Tileset.WALL;
                }
            }
        }
    }
    public static TETile[][] createEmptyWorld() {
        TETile[][] map = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }
        return map;
    }
    public static TETile[][] generateMap(String seed, TETile[][] map) {
        createRooms(map, seed);
        generatePaths(map);
        return map;
    }
}
