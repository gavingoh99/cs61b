package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import static byog.Core.MapGenerator.*;

public class Room {
    private int height;
    private int width;
    private int startY;
    private int startX;

    public Room(int height, int width, int startY, int startX) {
        this.height = height;
        this.width = width;
        this.startX = startX;
        this.startY = startY;
    }
    public static void createRooms(Room[] rooms) {
        for (int index = 0; index < rooms.length; index++) {
            int height = Game.rand.nextInt(10) + 6;
            int width = Game.rand.nextInt(10) + 6;
            int startY = Game.rand.nextInt(HEIGHT - height);
            int startX = Game.rand.nextInt(WIDTH - width);
            rooms[index] = new Room(height, width, startY, startX);
        }
    }
    private static void drawRoom(TETile[][] world, Room room) {
        for (int y = 0; y < room.height; y++) {
            for (int x = 0; x < room.width; x++) {
//                if (world[room.startX + x][room.startY + y] != Tileset.FLOOR) {
//                    world[room.startX + x][room.startY + y] = Tileset.WALL;
//                }
                world[room.startX + x][room.startY + y] = Tileset.WALL;
            }
        }
    }
    private static void fillRoom(TETile[][] world, Room room) {
        for (int y = 1; y < room.height - 1; y++) {
            for (int x = 1; x < room.width - 1; x++) {
                world[room.startX + x][room.startY + y] = Tileset.FLOOR;
            }
        }
    }
    public static void drawAll(TETile[][] world, int numberOfRooms, Room[] rooms) {
        for (Room room: rooms) {
            Room.drawRoom(world, room);
        }
    }
    public static void fillAll(TETile[][] world, int numberOfRooms, Room[] rooms) {
        for (Room room: rooms) {
            Room.fillRoom(world, room);
        }
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
    public int getStartY() {
        return this.startY;
    }
    public int getStartX() {
        return this.startX;
    }
}
