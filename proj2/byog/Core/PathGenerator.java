package byog.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//Uses DFS as underlying implementation
//To visit a room from another room
public class PathGenerator {
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public static List<Coordinate> solve(Map map) {
        LinkedList<Coordinate> nextToVisit = new LinkedList<>();
        Coordinate start = map.getStart();
        nextToVisit.add(start);
        while (!nextToVisit.isEmpty()) {
            Coordinate curr = nextToVisit.remove();
            if (!map.isValid(curr.getY(), curr.getX())) {
                continue;
            }
            if (map.isExplored(curr.getY(), curr.getX())) {
                continue;
            }
            if (map.isExit(curr.getX(), curr.getY())) {
                return backtrackPath(curr);
            }
            for (int[] direction : DIRECTIONS) {
                int newX = curr.getX() + direction[0];
                int newY = curr.getY() + direction[1];
                Coordinate coordinate = new Coordinate(newX, newY, curr);
                nextToVisit.add(coordinate);
                map.setVisited(curr.getY(), curr.getX(), true);
            }
        }
        return Collections.emptyList();
    }

    private static List<Coordinate> backtrackPath(Coordinate curr) {
        List<Coordinate> path = new ArrayList<>();
        Coordinate iter = curr;
        while (iter != null) {
            path.add(iter);
            iter = iter.parent;
        }
        return path;
    }
}
