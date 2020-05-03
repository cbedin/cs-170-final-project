import java.util.Set;
import java.util.Map;
import java.util.TreeSet;
import java.util.TreeMap;

/**
 * Class for representing vertices
 */
public class Vertex implements Comparable<Vertex> {
    /** Makes a new vertex with index INDEX. */
    Vertex (int index) {
        _index = index;
        _adjacentVertices = new TreeSet<>();
        _incidentEdges = new TreeMap<>();
    }

    /** Adds an edge E incident to this vertex. */
    void addEdge(Edge e) {
        _incidentEdges.put(e.otherVertex(this), e);
        _adjacentVertices.add(e.otherVertex(this));
    }

    /** Return the index associated with this vertex. */
    int index() {
        return _index;
    }

    /** Returns a list of all vertices adjacent to this one,
     *  as integers. */
    Set<Vertex> adjacent() {
        return _adjacentVertices;
    }

    /** Returns the edge that connects this vertex to OTHER. */
    Edge edgeTo(Vertex other) {
        return _incidentEdges.get(other);
    }

    @Override
    public int compareTo(Vertex other) {
        return index() - other.index();
    }

    @Override
    public String toString() {
        return "" + _index;
    }

    /** Integer index associated with this vertex. */
    private int _index;

    /** Set of vertices adjacent to this vertex. */
    private Set<Vertex> _adjacentVertices;

    /** Set of edges adjacent to this vertex (arranged
     *  by their opposing vertex). */
    private Map<Vertex, Edge> _incidentEdges;
}
