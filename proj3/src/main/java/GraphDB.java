import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;
import java.util.Iterator;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    static class Node implements Comparable<Node> {
        long id;
        String name = null;
        double lat;
        double lon;
        List<Long> adj = new ArrayList<>();
        double priority;
        Node prev = null;
        double distance;

        Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }
        void setName(String name) {
            this.name = name;
        }
        String getName() {
            return this.name;
        }
        void addConnection(long adjID) {
            adj.add(adjID);
        }
        double getPriority() {
            return this.priority;
        }
        void setPriority(double priority) {
            this.priority = priority;
        }
        double getDistance() {
            return this.distance;
        }
        void setDistance(double distance) {
            this.distance = distance;
        }
        boolean hasName() {
            return this.name != null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return id == node.id && lat == node.lat && lon == node.lon && name == node.name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, lat, lon, adj);
        }

        @Override
        public int compareTo(Node o) {
            double cmp = this.priority - o.priority;
            if (cmp < 0) {
                return -1;
            } else if (cmp > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    static class Edge {
        String id;
        String name;
        boolean isValid;
        List<Long> connections = new ArrayList<>();

        Edge(String id) {
            this.id = id;
        }
        void setName(String name) {
            this.name = name;
        }
        void setValid(boolean value) {
            this.isValid = value;
        }
        boolean isValid() {
            return this.isValid;
        }
        void addConnections(List<Long> list) {
            for (long node: list) {
                this.connections.add(node);
            }
        }
        List<Long> getConnections() {
            return this.connections;
        }
    }
    private Map<Long, Node> nodes = new HashMap<>();
    private List<Edge> ways = new ArrayList<>();
    private Trie locationTrie;
    private Map<String, List<Node>> locationMap;
    public Node getNode(long id) {
        return nodes.get(id);
    }
    public Collection<Node> getNodes() {
        return nodes.values();
    }
    public List<Edge> getEdges() {
        return ways;
    }
    public void addNode(Node node) {
        nodes.put(node.id, node);
    }
    public void addEdge(List<Long> connections, Edge way) {
        for (int index = 0; index < connections.size(); index++) {
            long key = connections.get(index);
            if (index - 1 > -1) {
                nodes.get(key).addConnection(connections.get(index - 1));
            }
            if (index + 1 < connections.size()) {
                nodes.get(key).addConnection(connections.get(index + 1));
            }
        }
        way.addConnections(connections);
        ways.add(way);
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
            initializeTrieAndLocationMap();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterator<Map.Entry<Long, Node>> iter = nodes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, Node> entry = iter.next();
            if (entry.getValue().adj.isEmpty()) {
                iter.remove();
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return nodes.get(v).adj;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double shortestDistance = -1;
        long closest = -1;
        for (long id: nodes.keySet()) {
            double vLon = nodes.get(id).lon;
            double vLat = nodes.get(id).lat;
            double distance = distance(lon, lat, vLon, vLat);
            if (shortestDistance < 0 || distance < shortestDistance) {
                shortestDistance = distance;
                closest = id;
            }
        }
        return closest;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    // initialize trie and map
    // iterates through all nodes and adds those with names
    // to the trie and the location map
    private void initializeTrieAndLocationMap() {
        locationTrie = new Trie();
        locationMap = new HashMap<>();
        for (Node node: nodes.values()) {
            if (node.hasName()) {
                String cleanedName = cleanString(node.getName());
                locationTrie.addWord(cleanedName);
                if (!locationMap.containsKey(cleanedName)) {
                    locationMap.put(cleanedName, new ArrayList<>());
                }
                locationMap.get(cleanedName).add(node);
            }
        }
    }
    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A List of the full names of locations whose cleaned name matches the
     * cleaned prefix.
     */
    List<String> getLocationsByPrefix(String prefix) {
        prefix = prefix.toLowerCase();
        List<String> cleanNames = locationTrie.getWordsByPrefix(prefix);
        List<String> locationNames = new ArrayList<>();
        for (String name: cleanNames) {
            List<Node> locations = locationMap.get(name);
            for (Node node: locations) {
                locationNames.add(node.getName());
            }
        }
        return locationNames;
    }
    /**
     * Collect all locations that match a cleaned locationName, and return information about
     * each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned locationName, and each location is a map of parameters for the Json response
     * as specified:
     * "lat" : Number, The latitude of the node.
     * "lon" : Number, The longitude of the node.
     * "name" : String, The actual name of the node.
     * "id" : Number, The id of the node.
     * NOTE: A particular location may span many Location instances. I.e.: may occupy many nodes.
     */
    List<Map<String, Object>> getLocations(String locationName) {
        locationName = cleanString(locationName);
        List<Map<String, Object>> result = new ArrayList<>();
        List<Node> locations = locationMap.get(locationName);
        for (Node node: locations) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", node.id);
            data.put("lon", node.lon);
            data.put("lat", node.lat);
            data.put("name", node.name);
            result.add(data);
        }
        return result;
    }
}
