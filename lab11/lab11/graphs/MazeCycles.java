package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private boolean hasCycle = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }
    /*
         a -> b -> c -> d
               \        /
                 f  -  e
     */
    @Override
    public void solve() {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int s = 0;
        dfs(s, stack);
    }

    // Helper methods go here
    private void dfs(int vertex, Stack<Integer> stack) {
        if (hasCycle) {
            return;
        }
        marked[vertex] = true;
        int parent = stack.peek();
        announce();
        stack.push(vertex);

        for (int w: maze.adj(vertex)) {
            if (hasCycle) {
                return;
            }
            if (!marked[w]) {
                dfs(w, stack);
            } else if (marked[w] && parent != w) {
                edgeTo[w] = vertex;
                int parentOfVertex = stack.pop();
                int grandparent;
                while (parentOfVertex != w) {
                    grandparent = stack.pop();
                    edgeTo[parentOfVertex] = grandparent;
                    parentOfVertex = grandparent;
                }
                hasCycle = true;
                announce();
                return;
            }
        }
    }
}

