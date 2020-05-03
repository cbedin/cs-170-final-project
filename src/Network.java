import java.util.Set;
import java.util.TreeSet;

/** Represents a network on a graph. */
public class Network implements Comparable<Network> {
    /** Creates a new network on G, assuming we are starting
     *  by placing a tower at SOURCE. */
    Network(Graph g, Vertex source) {
        _g = g;
        _edges = new TreeSet<>();
        _towers = new DistanceTracker(source);
        _adjacentCities = new TreeSet<>();
        _adjacentCities.addAll(source.adjacent());
    }

    /** Creates a new network that is a duplicate of N,
     *  but with an edge E added. */
    Network(Network n, Edge e) {
        _g = n._g;
        _edges = (TreeSet<Edge>) n._edges.clone();
        _edges.add(e);
        _towers = new DistanceTracker(n._towers, e);
        _adjacentCities = new TreeSet<>();
        for (Vertex adj : n._adjacentCities) {
            if (!_towers.hasVertex(adj)) {
                _adjacentCities.add(adj);
            }
        }
        for (Vertex adj : _towers.last().adjacent()) {
            if (!_towers.hasVertex(adj)) {
                _adjacentCities.add(adj);
            }
        }
    }

    /** Return the set of all vertices in this network. */
    Set<Integer> vertices() {
        return _towers.vertices();
    }

    /** Return the cost associated with this graph. */
    double cost() {
        return _towers.cost();
    }

    /** Return true iff this network covers G. */
    boolean covers() {
        return _towers.n() + _adjacentCities.size() == _g.n();
    }

    /** Return true iff this network includes all vertices in G. */
    boolean complete() {
        return _towers.n() == _g.n();
    }

    /** Return the set of vertices adjacent to this
     *  network. */
    Set<Vertex> adjacent() {
        return _adjacentCities;
    }

    /** Return the graph associated with this network. */
    Graph g() {
        return _g;
    }

    @Override
    public int compareTo(Network other) {
        double myCost = cost();
        double otherCost = other.cost();
        if (myCost > otherCost) {
            return 1;
        } else if (myCost < otherCost) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String otpt = "";
        for (Integer i : _towers.vertices()) {
            otpt += i + " ";
        }
        otpt = otpt.substring(0, otpt.length() - 1) + "\n";
        for (Edge e : _edges) {
            otpt += e + "\n";
        }
        return otpt.substring(0, otpt.length() - 1);
    }

    @Override
    public boolean equals(Object other) {
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /** Graph on which this network is made. */
    private Graph _g;

    /** All the nodes at which we have placed towers. */
    private DistanceTracker _towers;

    /** All cities that are adjacent to some city that
     *  contains a tower but do not have one themselves. */
    private Set<Vertex> _adjacentCities;

    /** All edges that are a part of this network. */
    private TreeSet<Edge> _edges;
}
