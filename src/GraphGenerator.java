import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/** Generates random graphs. */
public class GraphGenerator {
    /** Generates a graph on <= MAXN vertices. */
    private static String generateGraph(int maxN) {
        int n = (int)(maxN - Math.random() * (maxN - 2));
        Set<String> graph = new TreeSet<>();
        List<Integer> included = new LinkedList<>();
        included.add(0);
        List<Integer> unincluded = new LinkedList<>();
        for (int i = 1; i < n; i++) {
            unincluded.add(i);
        }
        while (unincluded.size() != 0) {
            Collections.shuffle(included);
            Collections.shuffle(unincluded);
            int u = included.get(0);
            int v = unincluded.get(0);
            double weight = Math.round((100 - Math.random() * 100) * 1000.0) / 1000.0;
            graph.add(Math.min(u, v) + " " + Math.max(u, v) + " " + weight);
            included.add(v);
            unincluded.remove(0);
        }
        int numExtraEdges = (int)(Math.random() * n * (n - 1.0) / 2.0);
        for (int i = 0; i < numExtraEdges; i++) {
            int u = (int)(Math.random() * n);
            int v = (int)(Math.random() * n);
            while (u == v) {
                v = (int)(Math.random() * n);
            }
            double weight = Math.round((100 - Math.random() * 100) * 1000.0) / 1000.0;
            graph.add(Math.min(u, v) + " " + Math.max(u, v) + " " + weight);
        }
        return n + "\n" + String.join("\n", graph);
    }

    /** Writes graphs to files. */
    static void makeFiles(int n, int numFiles) {
        for (int i = 0; i < numFiles; i++) {
            try {
                FileWriter f = new FileWriter(Main.IN_PATH + n + "-" + i + ".in");
                f.write(generateGraph(n));
                f.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
            }
        }
    }
}
