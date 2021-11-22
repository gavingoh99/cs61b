package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class PathGenerator {
    public static void drawPath(TETile[][] world, Room first, Room second) {
        Coordinate cFirst = randomCoordinate(first);
        Coordinate cSecond = randomCoordinate(second);
        int diffX = Math.abs(cFirst.getX() - cSecond.getX());
        int diffY = Math.abs(cFirst.getY() - cSecond.getY());
        int incrementX = 0;
        int incrementY = 0;
        if (diffX != 0) {
            incrementX = (cSecond.getX() - cFirst.getX()) / diffX;
        }
        if (diffY != 0) {
            incrementY = (cSecond.getY() - cFirst.getY()) / diffY;
        }
        int xCoord = cFirst.getX();
        for (int x = 0; x < diffX; x++) {
            xCoord += incrementX;
            world[xCoord][cFirst.getY()] = Tileset.FLOOR;
        }
        int yCoord = cFirst.getY();
        for (int y = 0; y < diffY; y++) {
            yCoord += incrementY;
            world[cFirst.getX() + incrementX * diffX][yCoord] = Tileset.FLOOR;
        }
    }
    public static Coordinate randomCoordinate(Room room) {
        int randomX = Game.rand.nextInt(room.getWidth() - 2) + room.getStartX() + 1;
        int randomY = Game.rand.nextInt(room.getHeight() - 2) + room.getStartY() + 1;
        return new Coordinate(randomX, randomY);
    }
    public static void drawAll(TETile[][] world, int numberOfRooms, Room[] rooms) {
        for (int index = 0; index < numberOfRooms - 1; index++) {
            PathGenerator.drawPath(world, rooms[index], rooms[index + 1]);
        }
    }
}
