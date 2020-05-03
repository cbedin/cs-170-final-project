import java.util.HashMap;

/** Cache that maps smaller networks to complete
 *  networks that have already been solved. */
public class Cache {
    /** Makes a new empty cache associated with graph G. */
    Cache() {
        _cache = new HashMap<>();
    }

    /** Cache network TO from network FROM. */
    void cache(Network2 from, Network2 to) {
        _cache.put(from, to);
    }

    /** Return true iff we have already cached network N. */
    boolean cached(Network2 n) {
        return _cache.keySet().contains(n);
    }

    /** Return the network that's cached at N. */
    Network2 get(Network2 n) {
        return _cache.get(n);
    }

    /** Represents the cache. */
    private HashMap<Network2, Network2> _cache;
}
