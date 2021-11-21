package byog.Core;

import java.util.Objects;

public class Coordinate {
    int xCoord;
    int yCoord;
    Coordinate parent;
    public Coordinate(int x, int y) {
        this(x, y, null);
    }
    public Coordinate(int x, int y, Coordinate parent) {
        this.xCoord = x;
        this.yCoord = y;
        this.parent = parent;
    }
    public int getX() {
        return this.xCoord;
    }
    public int getY() {
        return this.yCoord;
    }
    public Coordinate getParent() {
        return parent;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate compared = (Coordinate) o;
        if (this.xCoord == compared.xCoord && this.yCoord == compared.yCoord) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord);
    }
}
