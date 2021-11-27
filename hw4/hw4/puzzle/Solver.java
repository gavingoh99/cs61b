package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Stack<WorldState> path = new Stack<>();

    public Solver(WorldState initial) {
        MinPQ<SearchNode> fringe = new MinPQ<>();
        SearchNode start = new SearchNode(initial, 0, null);
        fringe.insert(start);

        SearchNode goal = null;
        while (!fringe.isEmpty()) {
            SearchNode curr = fringe.delMin();
            if (curr.worldState.isGoal()) {
                goal = curr;
                break;
            }
            for (WorldState neighbor: curr.worldState.neighbors()) {
                if (curr.prev == null || (curr != null && !neighbor.equals(curr.prev.worldState))) {
                    fringe.insert(new SearchNode(neighbor, curr.moves + 1, curr));
                }
            }
        }
        while (goal != null) {
            path.push(goal.worldState);
            goal = goal.prev;
        }
    }
    public int moves() {
        return path.size() - 1;
    }
    public Iterable<WorldState> solution() {
        return path;
    }
    private class SearchNode implements Comparable<SearchNode> {
        WorldState worldState;
        int moves;
        SearchNode prev;
        int priority;
        SearchNode(WorldState current, int moves, SearchNode prev) {
            this.worldState = current;
            this.moves = moves;
            this.prev = prev;
            this.priority = this.moves + this.worldState.estimatedDistanceToGoal();
        }
        @Override
        public int compareTo(SearchNode other) {
            return this.priority - other.priority;
        }
    }
}
