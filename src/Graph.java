import java.util.Set;
import java.util.Map;
import java.util.TreeSet;
import java.util.TreeMap;

/** Represents a graph. */
public class Graph {
    /** Constructs a new graph of size N. */
    Graph(int n) {
        _n = n;
        _vertices = new TreeMap<>();
        _edges = new TreeSet<>();
        for (int i = 0; i < _n; i++) {
            _vertices.put(i, new Vertex(i));
        }
    }

    /** Return true iff there is an edge in this
     *  graph between U and V. */
    private boolean hasEdgeBetween(int u, int v) {
        return vertex(u).adjacent().contains(vertex(v));
    }

    /** Return true iff there is an edge in this
     *  graph between U and V. */
    private boolean hasEdgeBetween(Set<Integer> us, int v) {
        for (int u : us) {
            if (hasEdgeBetween(u, v)) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff the set of vertices in VS is
     *  sufficient to cover G. */
    boolean coveredBy(Set<Integer> vs) {
        for (int u : _vertices.keySet()) {
            if (!vs.contains(u) && !hasEdgeBetween(vs, u)) {
                return false;
            }
        }
        return true;
    }

    /** Adds an edge between U and V of weight WEIGHT. */
    void addEdgeBetween(int u, int v, double weight) {
        _edges.add(new Edge(vertex(u), vertex(v), weight));
    }

    /** Return the vertex corresponding to i. */
    Vertex vertex(int i) {
        return _vertices.get(i);
    }

    /** Return the map of vertices associated with
     *  this graph. */
    Map<Integer, Vertex> vertices() {
        return _vertices;
    }

    /** Return the shortest edge from a vertex in FROM
     *  to vertex TO. */
    Set<Edge> edgesBetween(Network from, Vertex to) {
        Set<Edge> between = new TreeSet<>();
        for (Integer i : from.vertices()) {
            Edge e = to.edgeTo(vertex(i));
            if (e != null) {
                between.add(e);
            }
        }
        return between;
    }

    /** Return the set of edges in this graph. */
    Set<Edge> edges() {
        return _edges;
    }

    /** Return the number of vertices in this graph. */
    int n() {
        return _n;
    }

    /** Size of this graph. */
    private int _n;

    /** The set of vertices in this graph (maps integers
     *  to corresponding vertices. */
    private Map<Integer, Vertex> _vertices;

    /** The set of edges in this graph. */
    private Set<Edge> _edges;
}
