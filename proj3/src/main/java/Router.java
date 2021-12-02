import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long startID = g.closest(stlon, stlat);
        GraphDB.Node start = g.getNode(startID);
        long destID = g.closest(destlon, destlat);
        GraphDB.Node dest = g.getNode(destID);
        List<Long> path = new ArrayList<>();
        Queue<GraphDB.Node> fringe = new PriorityQueue<>();
        for (GraphDB.Node node: g.getNodes()) {
            if (node.equals(start)) {
                node.setDistance(0.0);
            } else {
                node.setDistance(Double.MAX_VALUE);
            }
        }
        double priority = start.getDistance() + h(start, destlon, destlat);
        start.setPriority(priority);
        fringe.add(start);
        boolean found = false;
        while (!fringe.isEmpty()) {
            GraphDB.Node node = fringe.poll();
            if (node.equals(dest)) {
                found = true;
                break;
            }
            for (long id: g.adjacent(node.id)) {
                GraphDB.Node adjNode = g.getNode(id);
                if (node.getDistance() + g.distance(node.id, id) < adjNode.getDistance()) {
                    adjNode.setDistance(node.getDistance() + g.distance(node.id, id));
                    adjNode.prev = node;
                    double adjPriority = adjNode.getDistance() + h(adjNode, destlon, destlat);
                    adjNode.setPriority(adjPriority);
                    fringe.add(adjNode);
                }
            }
        }
        if (found) {
            start.prev = null;
            GraphDB.Node pointerNode = dest;
            while (pointerNode != null) {
                path.add(pointerNode.id);
                pointerNode = pointerNode.prev;
            }
            Collections.reverse(path);
        }
        return path;
    }
    // heuristic method for distance from node to target
    private static double h(GraphDB.Node node, double destlon, double destlat) {
        return GraphDB.distance(node.lon, node.lat, destlon, destlat);
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> directions = new ArrayList<>();
        if (route.size() <= 1) {
            return directions;
        }
        // initalize the first direction instance
        NavigationDirection direction = new NavigationDirection();
        direction.direction = NavigationDirection.START;
        direction.distance = 0.0;
        direction.way = NavigationDirection.getWay(g, route.get(0), route.get(1)).name;
        // iterate through all the nodes
        for (int i = 1; i < route.size(); i++) {
            long prev = route.get(i - 1);
            long curr = route.get(i);
            direction.distance += g.distance(prev, curr);
            if (i == route.size() - 1) {
                directions.add(direction);
                break;
            }
            // if the way that connects the curr and next node
            // differs from the current way then add the curr direction
            // to the list and instantiate a new one for the next
            // round of iteration
            long next = route.get(i + 1);
            if (!direction.way.equals(NavigationDirection.getWay(g, curr, next).name)) {
                directions.add(direction);
                direction = new NavigationDirection();
                direction.direction = NavigationDirection.getDirection(g, prev, curr, next);
                direction.distance = 0.0;
                direction.way = NavigationDirection.getWay(g, curr, next).name;
            }
        }
        return directions;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }
        /**
         * Returns the change in direction between 2 highway segments determined by 3 nodes,
         * given an instance of GraphDB and the Node ids of 3 nodes. The change in direction
         * is calculated based on the bearing angle of each highway segment.
         */
        public static int getDirection(GraphDB g, long n1, long n2, long n3) {
            double angle = g.bearing(n3, n2) - g.bearing(n2, n1);
            int direction = START;
            if (-15 < angle && angle < 15) {
                direction = STRAIGHT;
            } else if (-30 < angle && angle <= -15) {
                direction = SLIGHT_LEFT;
            } else if (15 <= angle && angle < 30) {
                direction = SLIGHT_RIGHT;
            } else if (-100 < angle && angle <= -30) {
                direction = LEFT;
            } else if (30 <= angle && angle < 100) {
                direction = RIGHT;
            } else if (angle <= -100) {
                direction = g.bearing(n3, n1) < 0 ? SHARP_LEFT : SHARP_RIGHT;
            } else if (100 <= angle) {
                direction = g.bearing(n3, n1) > 0 ? SHARP_LEFT : SHARP_RIGHT;
            }
            return direction;
        }
        /**
         * Obtains the Highway that passes by 2 nodes.
         * The calculation is based on the fact that, for this model, exactly 1 highway can
         * pass by 2 given points.
         * @param g is the graph (GraphDB) to use.
         * @param n1 is the Node id of one of the nodes that the highway contains.
         * @param n2 is the Node id of one of the nodes that the highway contains.
         * @return the Highway that passes by the 2 nodes passed as parameter.
         */
        public static GraphDB.Edge getWay(GraphDB g, long n1, long n2) {
            Iterable<GraphDB.Edge> ways = g.getEdges();
            Set<GraphDB.Edge> way1 = new HashSet<>();
            Set<GraphDB.Edge> way2 = new HashSet<>();
            for (GraphDB.Edge way: ways) {
                if (way.connections.contains(n1)) {
                    way1.add(way);
                }
                if (way.connections.contains(n2)) {
                    way2.add(way);
                }
            }
            Set<GraphDB.Edge> intersectingWays = new HashSet<>();
            for (GraphDB.Edge way: way1) {
                if (way2.contains(way)) {
                    intersectingWays.add(way);
                }
            }
            return intersectingWays.iterator().next();
        }


        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
