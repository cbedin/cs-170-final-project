public class HeuristicNetworkBuilder {
    /** Creates a new network builder and assembles a network. */
    HeuristicNetworkBuilder(Graph g) {
        _g = g;
        _c = new Cache();
        _n = findNetwork();
    }

    /** Finds a network using heuristic methods. */
    Network2 findNetwork() {
        Network2 guess = findNetwork(0);
        for (int i = 1; i < _g.n(); i++) {
            Network2 newGuess = findNetwork(i);
            if (newGuess.cost() < guess.cost()) {
                guess = newGuess;
            }
        }
        return guess;
    }

    /** Finds a network rooted at SOURCE. */
    Network2 findNetwork(int source) {
        return findNetwork(new Network2(_g, source));
    }

    /** Finds the minimum-cost expansion of N. */
    Network2 findNetwork(Network2 n) {
        if (n.covers() && !n.improvement()) {
            return n;
        } else if (_c.cached(n)) {
            return _c.get(n);
        } else {
            Network2 next = findNetwork(new Network2(n));
            _c.cache(n, next);
            return next;
        }
    }

    /** Return the network created by this builder. */
    Network2 network() {
        return _n;
    }

    /** Graph associated with this builder. */
    private Graph _g;

    /** Network associated with this builder. */
    private Network2 _n;

    /** Cache associated with this builder. */
    private Cache _c;
}
