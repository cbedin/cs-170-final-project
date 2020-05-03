import java.util.Map;

/**
 * Class for representing edges
 */
public class Edge implements Comparable<Edge> {
    /* Construct an edge between U and V of weight WEIGHT. */
    Edge(Vertex u, Vertex v, double weight) {
        _u = u;
        _v = v;
        _weight = weight;
        _u.addEdge(this);
        _v.addEdge(this);
    }

    /** Return the vertex incident to this edge that is
     *  not V, or null if V is not adjacent to this edge. */
    Vertex otherVertex(Vertex v) {
        if (v == _v) {
            return _u;
        } else if (v == _u) {
            return _v;
        } else {
            return null;
        }
    }

    /** Returns the vertex incident to this edge that
     *  corresponds to I. */
    Vertex vertex(int i) {
        if (i == _v.index()) {
            return _v;
        } else if (i == _u.index()) {
            return _u;
        } else {
            return null;
        }
    }

    /** Return the index of the vertex incident to this edge
     *  that is not V, or zero if V is not adjacent to this
     *  edge. */
    int otherIndex(int v) {
        if (v == _v.index()) {
            return _u.index();
        } else if (v == _u.index()) {
            return _v.index();
        } else {
            return -1;
        }
    }

    int otherIndex(DistanceTracker d) {
        if (d.hasVertex(_u)) {
            return _v.index();
        } else if (d.hasVertex(_v)) {
            return _u.index();
        } else {
            return -1;
        }
    }

    /** Return the weight of this edge. */
    double weight() {
        return _weight;
    }

    /** Returns the index of U. */
    int u() {
        return _u.index();
    }

    /** Returns the index of V. */
    int v() {
        return _v.index();
    }

    @Override
    public int compareTo(Edge other) {
        int u = _u.compareTo(other._u);
        if (u == 0) {
            return _v.compareTo(other._v);
        } else {
            return u;
        }
    }

    @Override
    public String toString() {
        return _u + " " + _v;
    }

    /** Vertices of this edge. */
    private Vertex _u;
    private Vertex _v;

    /** Weight of this edge. */
    private double _weight;
}
