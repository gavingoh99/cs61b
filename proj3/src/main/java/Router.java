import java.util.*;
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
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> directions = new ArrayList<>();
        int index = 0;
        while (index < route.size() - 1) {
            NavigationDirection direction = new NavigationDirection();
            double distance = 0;
            if (index == 0) {
                direction.direction = 0;
            } else {
                double bearing = g.bearing(route.get(index - 1), route.get(index));
                direction.direction = setDirection(bearing);
            }
            GraphDB.Edge currEdge = null;
            //[53076845, 53076852, 58443169, 4226524585, 53082187, 4260281369, 4260281370, 53058046, 240404706, 4225207008, 53050596, 4225207004, 4621431267, 53082185, 4225207003, 4225206998, 53047322, 4225206995, 53047324]
            while (index != route.size() - 1) {
                if (currEdge != null) {
                    int pos1 = currEdge.connections.indexOf(route.get(index));
                    int pos2 = currEdge.connections.indexOf(route.get(index + 1));
                    if (pos2 == -1) {
                        break;
                    } else if (Math.abs(pos1 - pos2) == 1) {
                        distance += g.distance(route.get(index), route.get(index + 1));
                        index++;
                    } else {
                        break;
                    }
                } else {
                    for (GraphDB.Edge edge : g.getEdges()) {
                        if (edge.connections.contains(route.get(index))) {
                            int pos1 = edge.connections.indexOf(route.get(index));
                            int pos2 = edge.connections.indexOf(route.get(index + 1));
                            if (pos2 == -1) {
                                continue;
                            } else if (Math.abs(pos1 - pos2) == 1) {
                                distance += g.distance(route.get(index), route.get(index + 1));
                                index++;
                                currEdge = edge;
                                break;
                            }
                        }
                    }
                }
            }
            direction.distance = distance;
            if (currEdge != null && currEdge.name != null) {
                direction.way = currEdge.name;
            }
            directions.add(direction);
        }
        return directions;
    }

    private static int setDirection(double bearing) {
        if (bearing >= -15 && bearing <= 0) {
            return 1;
        } else if (bearing < 0) {
            if (bearing >= -30) {
                return 2;
            } else if (bearing >= -100) {
                return 4;
            } else {
                return 6;
            }
        } else {
            if (bearing <= 30) {
                return 3;
            } else if (bearing <= 100) {
                return 5;
            } else {
                return 7;
            }
        }
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
