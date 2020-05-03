import java.util.*;

/** Second attempt at a class that represents a network. */
public class Network2 implements Comparable<Network2> {
    /** Constructs a new network on this graph. */
    Network2(Graph g, int source) {
        _g = g;
        _edges = new TreeMap<>();
        for (Edge e : g.edges()) {
            putEdge(e);
        }
        _distances = new TreeMap<>();
        _myEdges = new TreeSet<>();
        addDistance(source, source, 0.0);
        updateEdgeCosts();
    }

    /** Constructs an expanded version of N. */
    Network2(Network2 n) {
        _g = n._g;
        _edges = new TreeMap<>();
        for (Double d : n._edges.keySet()) {
            _edges.put(d, new TreeSet<>(n._edges.get(d)));
        }
        _distances = new TreeMap<>();
        for (int i : n._distances.keySet()) {
            _distances.put(i, new TreeMap<>(n._distances.get(i)));
        }
        _myEdges = new TreeSet<>(n._myEdges);
        addMinEdge();
        updateEdgeCosts();
    }

    /** Puts edge E, into EDGES with cost infinity. */
    private void putEdge(Edge e) {
        putEdge(Double.POSITIVE_INFINITY, e);
    }

    /** Puts edge E, corresponding to COST, into EDGES. */
    private void putEdge(double cost, Edge e) {
        putEdge(_edges, cost, e);
    }

    /** Puts edge E, corresponding to COST, into EDGES. */
    private void putEdge(TreeMap<Double, TreeSet<Edge>> edges, double cost, Edge e) {
        if (!edges.keySet().contains(cost)) {
            edges.put(cost, new TreeSet<>());
        }
        edges.get(cost).add(e);
    }

    /** Return true iff this network covers its graph. */
    boolean covers() {
        return _g.coveredBy(vertices());
    }

    /** Returns true iff adding another edge will
     *  improve our overall cost. */
    boolean improvement() {
        if (_edges.size() == 0) {
            return false;
        }
        return _edges.firstKey() < 0;
    }

    /** Adds the minimum-cost edge in our tracker to this network. */
    void addMinEdge() {
        Edge e = _edges.get(_edges.firstKey()).first();
        int from = tracking(e);
        int to = e.otherIndex(from);
        addDistance(to, to, 0);
        addDistance(from, to, 0);
        for (int v : vertices()) {
            addDistance(v, to, getDistance(from, v) + e.weight());
        }
        _myEdges.add(e);
        updateEdgeCosts();
    }

    /** Updates the costs associated with each edge. */
    private void updateEdgeCosts() {
        TreeMap<Double, TreeSet<Edge>> newCosts = new TreeMap<>();
        for (double cost : _edges.keySet()) {
            for (Edge e : _edges.get(cost)) {
                if (!containsBoth(e)) {
                    putEdge(newCosts, addedCost(e), e);
                }
            }
        }
        _edges = newCosts;
    }

    /** Adds information to our map that U is DIST units
     *  away from V. */
    private void addDistance(int u, int v, double dist) {
        int from = Math.min(u, v);
        int to = Math.max(u, v);
        if (!_distances.keySet().contains(from)) {
            _distances.put(from, new TreeMap<>());
            _distances.get(from).put(from, 0.0);
        }
        _distances.get(from).put(to, dist);
    }

    /** Returns some edge on the path between u and v. */
    private Edge alternant(Edge e) {
        if (!containsBoth(e)) {
            return null;
        }
        List<Edge> edges = pathBetween(e.u(), e.v());
        if (edges.size() == 0) {
            return null;
        }
        Collections.shuffle(edges);
        return edges.get(0);
    }

    /** Return a list of edges that are the simple path
     *  from U to V in our network. */
    private List<Edge> pathBetween(int u, int v) {
        boolean[] visited = new boolean[_g.n()];
        for (int i = 0; i < _g.n(); i++) {
            visited[i] = false;
        }
        return pathBetween(u, v, visited);
    }

    /** Returns a list of edges that are the path between U and V,
     *  assuming that vertices in VISITED have already been seen. */
    private List<Edge> pathBetween(int u, int v, boolean[] visited) {
        if (visited[u]) {
            return null;
        }
        if (u == v) {
            return new LinkedList<>();
        }
        visited[u] = true;
        for (Edge e : incidentEdges(u)) {
            List<Edge> restOfPath = pathBetween(e.otherIndex(u), v, visited);
            if (restOfPath != null) {
                restOfPath.add(0, e);
                return restOfPath;
            }
        }
        return null;
    }

    /** Return a list of all edges incident to
     *  V in the current network. */
    private List<Edge> incidentEdges(int v) {
        List<Edge> edges = new LinkedList<>();
        for (Edge e : _myEdges) {
            if (e.u() == v || e.v() == v) {
                edges.add(e);
            }
        }
        return edges;
    }

    /** Returns true iff there is a path in this network
     *  between U and V. */
    private boolean containsBoth(int u, int v) {
        if (u == v) {
            return true;
        }
        return tracking(u) && tracking(v);
    }

    /** Returns true iff there is a path in this network
     *  between the endpoints of E. */
    private boolean containsBoth(Edge e) {
        return containsBoth(e.u(), e.v());
    }

    /** Returns the distance between U and V. */
    private double getDistance(int u, int v) {
        if (u == v) {
            return 0;
        }
        int from = Math.min(u, v);
        int to = Math.max(u, v);
        if (!containsBoth(from, to)) {
            return Double.POSITIVE_INFINITY;
        }
        return _distances.get(from).get(to);
    }

    /** Returns the cost of this network. */
    double cost() {
        if (_myEdges.size() == 0) {
            return 0;
        }
        double cost = 0;
        for (int u : vertices()) {
            for (int v : vertices()) {
                cost += getDistance(u, v);
            }
        }
        return cost / (numVertices() * (numVertices() - 1));
    }

    /** Returns the added cost of integrating E into the network. */
    private double addedCost(Edge e) {
        if (!tracking(e.u()) && !tracking(e.v())) {
            return Double.POSITIVE_INFINITY;
        }
        int w = tracking(e);
        double newCost = 0;
        for (int u : vertices()) {
            for (int v : vertices()) {
                newCost += getDistance(u, v);
            }
            newCost += 2 * (getDistance(u, w) + e.weight());
        }
        return newCost / (numVertices() * (numVertices() + 1)) - cost();
    }

    /** Returns the difference in cost if we add
     *  TOADD and remove TOREMOVE. */
    private double addedCost(Edge toAdd, Edge toRemove) {
        Set<Integer> sCut = cut(toRemove.u(), toRemove.v());
        Set<Integer> tCut = cut(toRemove.v(), toRemove.u());
        int s = tracking(toAdd, sCut);
        int t = tracking(toAdd, tCut);
        double newCost = 0;
        for (int u : sCut) {
            for (int v : sCut) {
                newCost += getDistance(u, v);
            }
        }
        for (int u : tCut) {
            for (int v : tCut) {
                newCost += getDistance(u, v);
            }
        }
        for (int u : sCut) {
            for (int v : tCut) {
                newCost += 2 * (getDistance(s, u) + getDistance(t, v) + toAdd.weight());
            }
        }
        return newCost - cost();
    }

    /** Return the set of vertices in Ss side of the cut
     *  if we remove the edge (s, t). */
    private Set<Integer> cut(int s, int t) {
        Set<Integer> cut = new TreeSet<>();
        for (int v : vertices()) {
            if (getDistance(v, s) < getDistance(v, t)) {
                cut.add(v);
            }
        }
        return cut;
    }

    /** Returns the vertex incident to E that is
     *  being tracked by this network. */
    private int tracking(Edge e) {
        int u = e.u();
        int v = e.v();
        if (tracking(u)) {
            return u;
        } else if (tracking(v)) {
            return v;
        } else {
            return -1;
        }
    }

    /** Returns the vertex incident to E that is in CUT. */
    private int tracking(Edge e, Set<Integer> cut) {
        if (cut.contains(e.u())) {
            return e.u();
        } else if (cut.contains(e.v())) {
            return e.v();
        } else {
            return -1;
        }
    }

    /** Returns true iff V is in this network. */
    private boolean tracking(int v) {
        return vertices().contains(v);
    }

    /** Returns the set of vertices being tracked. */
    private Set<Integer> vertices() {
        return _distances.keySet();
    }

    /** Returns the number of vertices being tracked. */
    private int numVertices() {
        return vertices().size();
    }

    @Override
    public String toString() {
        String str = "";
        for (int i : vertices()) {
            str += i + " ";
        }
        str = str.substring(0, str.length() - 1);
        for (Edge e : _myEdges) {
            str += "\n" + e;
        }
        return str;
    }

    @Override
    public int compareTo(Network2 other) {
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
    public boolean equals(Object other) {
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /** Graph on which this network is made. */
    private Graph _g;

    /** Maps distances between vertices. */
    private TreeMap<Integer, TreeMap<Integer, Double>> _distances;

    /** Maps the cost of adding an edge to that edge. */
    private TreeMap<Double, TreeSet<Edge>> _edges;

    /** The set of edges contained within this network. */
    private TreeSet<Edge> _myEdges;
}
