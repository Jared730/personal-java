import java.util.*;

public class Driver {
    public static final boolean EASY = true, MEDIUM = true, HARD = true;
    public static void main(String[] args) {
        Graph graph = new Graph(Driver.class.getResourceAsStream("words.dat"));
        System.out.println("Number of vertices: "+graph.getAdjacency().size());
        System.out.println("Number of edges: "+graph.getEdges().size());
        if (EASY) printEasy(graph);
        Set<String> subgraph;
        if (MEDIUM) subgraph = printMedium(graph);
        else subgraph = graph.findLargestConnected();
        if (HARD) printHard(graph,subgraph);
    }

    public static void printEasy(Graph g) {
        System.out.println("\nGraphAlgos Easy:");
        Map<String, List<Edge>> adjMap = g.getAdjacency();
        double avg = 0;
        int edgeless = 0, maxVal = -1;
        String max = null;
        for (String s : adjMap.keySet()) {
            int size = adjMap.get(s).size();
            if (max == null || size > maxVal) {
                max = s;
                maxVal = size;
            }
            if (size == 0) edgeless++;
            else avg += size;
        }
        avg /= adjMap.size();
        System.out.println("Average number of connections: "+avg);
        System.out.println("Number of words with no connections: "+edgeless);
        System.out.println("Word with maximum number of edges: "+max+", with "+maxVal+" connections");
    }

    public static Set<String> printMedium(Graph g) {
        System.out.println("\nGraphAlgos Medium:");
        System.out.print("Breadth-First iterator on \"omens\":\n[");
        Iterator<String> bfs = g.breadthFirst("omens");
        while (bfs.hasNext()) {
            System.out.print(bfs.next());
            if (bfs.hasNext()) System.out.print(", ");
        }
        System.out.print("]\nDepth-First iterator on \"omens\":\n[");
        Iterator<String> dfs = g.depthFirst("omens");
        while (dfs.hasNext()) {
            System.out.print(dfs.next());
            if (dfs.hasNext()) System.out.print(", ");
        }
        System.out.println("]");
        Set<String> subgraph = g.findLargestConnected();
        System.out.println("Largest connected subgraph size: "+subgraph.size());
        System.out.println("Largest connected subgraph:");
        System.out.println(subgraph); //I can't think of a clean way to not make this roll off-screen.
        return subgraph;
    }

    public static void printHard(Graph g, Set<String> subgraph) {
        System.out.println("\nGraphAlgos Hard:");
        Set<Edge> mst = g.primsAlgorithm(subgraph);
        int totalWeight = mst.stream().mapToInt(Edge::weight).sum();
        System.out.println("Total number of edges: "+mst.size());
        System.out.println("Total weight of MST: "+totalWeight);
    }
}
