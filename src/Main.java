import java.io.File;

/** Main class for this program. */
public class Main {
    /** Path to access input files. */
    static final String IN_PATH = "inputs/";

    /** Path to access output files. */
    static final String OUT_PATH = "outputs/";

    /** Evaluates all inputs currently in the IN directory. */
    static void evalInputs() {
        File dir = new File(IN_PATH);
        double totalCost = 0;
        int i = 0;
        for (String fileName : dir.list()) {
            if (fileName.contains(".in")) {
                GraphBuilder g = new GraphBuilder(fileName);
                totalCost += g.write();
                i++;
            }
        }
        System.out.println("Average Cost: " + totalCost / i);
    }

    public static void main(String[] args) {
        /*GraphGenerator.makeFiles(25, 100);
        GraphGenerator.makeFiles(50, 100);
        GraphGenerator.makeFiles(100, 100);*/
        evalInputs();
    }
}
