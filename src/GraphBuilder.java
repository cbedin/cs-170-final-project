import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/** Class that builds new graphs based off
 *  of descriptive files and writes them to new files. */
public class GraphBuilder {
    /** Builds a graph based off of FILENAME. */
    GraphBuilder(String fileName) {
        _fileName = fileName.substring(0, fileName.indexOf(".in"));
        System.out.println(_fileName);
        try {
            File f = new File(Main.IN_PATH + fileName);
            Scanner reader = new Scanner(f);
            int n = Integer.parseInt(reader.nextLine().strip());
            _g = new Graph(n);
            while (reader.hasNextLine()) {
                String[] edge = reader.nextLine().split(" ");
                int u = Integer.parseInt(edge[0]);
                int v = Integer.parseInt(edge[1]);
                double weight = Double.parseDouble(edge[2]);
                _g.addEdgeBetween(u, v, weight);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
        }
    }

    /** Writes the relevant network to a file. */
    double write() {
        try {
            Network2 n = new HeuristicNetworkBuilder(_g).network();
            FileWriter f = new FileWriter(Main.OUT_PATH + _fileName + ".out");
            f.write(n.toString());
            f.close();
            return n.cost();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            return 0;
        }
    }

    /** Graph that is ultimately made by this builder. */
    private Graph _g;

    /** Filename associated with this builder. */
    private String _fileName;
}
