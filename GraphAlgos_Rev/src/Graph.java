import java.io.InputStream;
import java.util.*;
public class Graph {
    private final Set<Edge> edgeSet;
    private final Map<String,List<Edge>> adjMap;

    public Graph(InputStream input) {
        edgeSet = new HashSet<>();
        adjMap = new HashMap<>();
        Scanner reader = new Scanner(input);
        List<String> wordList = new ArrayList<>();
        while (reader.hasNext()) {
            String next = reader.next();
            adjMap.put(next,new LinkedList<>());
            wordList.add(next);
        }
        for (int i = 0; i < wordList.size(); i++) {
            String iWord = wordList.get(i);
            for (int j = 0; j < i; j++) {
                String jWord = wordList.get(j);
                if (Edge.isAdjacent(iWord,jWord)) {
                    Edge ijEdge = new Edge(iWord,jWord);
                    adjMap.get(iWord).add(ijEdge);
                    adjMap.get(jWord).add(ijEdge);
                    edgeSet.add(ijEdge);
                }
            }
        }
        adjMap.values().forEach(e -> e.sort(new Edge.WeightComparator()));
    }

    public Map<String,List<Edge>> getAdjacency() {
        return adjMap;
    }
    public Set<Edge> getEdges() {
        return edgeSet;
    }
    public Iterator<String> breadthFirst(String start) {
        return new BreadthIterator(start);
    }
    public Iterator<String> depthFirst(String start) {
        return new DepthIterator(start);
    }
    public Set<String> findLargestConnected() { //Likely rather slow
        Set<String> available = new HashSet<>(adjMap.keySet());
        Set<String> output = new HashSet<>();
        while (!available.isEmpty()) {
            Set<String> current = new HashSet<>();
            Iterator<String> itr = breadthFirst(available.iterator().next());
            while (itr.hasNext()) {
                String next = itr.next();
                current.add(next);
                available.remove(next);
            }
            if (current.size() > output.size()) output = current;
        }
        return output;
    }

    public Set<Edge> primsAlgorithm(Set<String> subgraph) {
        Set<Edge> output = new HashSet<>(subgraph.size() - 1);
        Set<String> consumed = new HashSet<>();
        PriorityQueue<Edge> available = new PriorityQueue<>(new Edge.WeightComparator());
        consumed.add(subgraph.iterator().next());
        available.addAll(adjMap.get(subgraph.iterator().next()));
        while (!available.isEmpty()) {
            Edge e = available.poll();
            output.add(e);
            String o = consumed.contains(e.front())?e.back():e.front();
            consumed.add(o);
            available.removeIf(i -> i.contains(o));
            for (Edge oAdj : adjMap.get(o)) {
                if (!consumed.contains(oAdj.complement(o)) && !available.contains(oAdj)) available.add(oAdj);
            }
        }
        return output;
    }

    private class BreadthIterator implements Iterator<String> {
        private final Set<String> consumed;
        private final Deque<String> vQueue;
        public BreadthIterator(String start) {
            consumed = new HashSet<>();
            vQueue = new LinkedList<>();
            vQueue.add(start);
        }
        @Override
        public boolean hasNext() {
            return !vQueue.isEmpty();
        }
        @Override
        public String next() {
            if (vQueue.isEmpty()) throw new NoSuchElementException();
            String next = vQueue.poll();
            for (Edge adjE : adjMap.get(next)) {
                String adjV = adjE.complement(next);
                if (!consumed.contains(adjV) && !vQueue.contains(adjV)) vQueue.add(adjV);
            }
            consumed.add(next);

            return next;
        }
    }

    private class DepthIterator implements Iterator<String> {
        private final Set<String> consumed;
        private final Deque<String> vStack;
        public DepthIterator(String start) {
            consumed = new HashSet<>();
            vStack = new LinkedList<>();
            vStack.push(start);
        }
        @Override
        public boolean hasNext() {
            return !vStack.isEmpty();
        }
        @Override
        public String next() {
            if (vStack.isEmpty()) throw new NoSuchElementException();
            String next = vStack.poll();
            for (Edge adjE : adjMap.get(next)) {
                String adjV = adjE.complement(next);
                if (!consumed.contains(adjV) && !vStack.contains(adjV)) vStack.push(adjV);
            }
            consumed.add(next);
            return next;
        }
    }
}
