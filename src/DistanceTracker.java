import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/** Class to track the distances between all vertices in a network. */
public class DistanceTracker {
    /** Makes a new distance tracker based at SOURCE. */
    DistanceTracker(Vertex source) {
        _distances = new TreeMap<>();
        addDist(source.index(), source.index(), 0);
    }

    /** Makes a new distance tracker that is a copy of SOURCE,
     *  but with EDGE added. */
    DistanceTracker(DistanceTracker source, Edge e) {
        _distances = new TreeMap<>();
        int v = e.otherIndex(source); // Vertex being added
        int u = e.otherIndex(v); // Vertex being extended from
        double weight = e.weight();
        _distances.put(v, new TreeMap<>());
        addDist(v, v, 0);
        for (int from : source._distances.keySet()) {
            for (int to : source._distances.get(from).keySet()) {
                addDist(from, to, source._distances.get(from).get(to));
            }
            addDist(from, v, getDist(from, u) + weight);
        }
        _lastAdded = e.vertex(v);
    }

    /** Adds a distance from U to V. */
    private void addDist(int u, int v, double dist) {
        int from = Math.min(u, v);
        int to = Math.max(u, v);
        if (!_distances.keySet().contains(from)) {
            _distances.put(from, new TreeMap<>());
        }
        _distances.get(from).put(to, dist);
    }

    /** Returns the distance from U to V. */
    private double getDist(int u, int v) {
        int from = Math.min(u, v);
        int to = Math.max(u, v);
        return _distances.get(from).get(to);
    }

    /** Return the set of integers corresponding to tracked vertices. */
    Set<Integer> vertices() {
        return _distances.keySet();
    }

    /** Return the most recent vertex added to be tracked. */
    Vertex last() {
        return _lastAdded;
    }

    /** Return the total cost of distances. */
    double cost() {
        double cost = 0.0;
        for (int from : _distances.keySet()) {
            for (int to : _distances.get(from).keySet()) {
                cost += _distances.get(from).get(to);
            }
        }
        return 2 * cost / (n() * (n() - 1));
    }

    /** Return the number of vertices being tracked. */
    int n() {
        return _distances.keySet().size();
    }

    /** Return true iff this tracker is tracking V. */
    boolean hasVertex(Vertex v) {
        return _distances.keySet().contains(v.index());
    }

    /** Tracks the distances between all pairs of vertices. */
    private Map<Integer, Map<Integer, Double>> _distances;

    /** The most recent vertex added. */
    private Vertex _lastAdded;
}
