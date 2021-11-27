package lab11.graphs;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    Map<Integer, Integer> fringe = new HashMap<>();

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;

        for (int index = 0; index < distTo.length; index++) {
            if (index != s) {
                distTo[index] = Integer.MAX_VALUE;
            }
        }
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int currX = maze.toX(v);
        int currY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(currX - targetX) + Math.abs(currY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        int min = -1;
        int smallestVertex = -1;
        for (int vertex: fringe.keySet()) {
            if (min < 0 || fringe.get(vertex) < min) {
                min = fringe.get(vertex);
                smallestVertex = vertex;
            }
        }
        fringe.remove(smallestVertex);
        return smallestVertex;
    }

    /** Performs an A star search from vertex s. */
    private void astar(int source) {
        fringe.put(source, distTo[s] + h(source));

        while (!fringe.isEmpty()) {
            int v = findMinimumUnmarked();
            marked[v] = true;
            announce();
            if (v == t) {
                break;
            }
            for (int w: maze.adj(v)) {
                if (!marked[w]) {
                    if (distTo[w] > distTo[v] + 1) {
                        distTo[w] = distTo[v] + 1;
                        edgeTo[w] = v;
                        fringe.put(w, distTo[w] + h(w));
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

