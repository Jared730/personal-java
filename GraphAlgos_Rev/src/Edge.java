import java.util.Comparator;

public class Edge implements Comparable<Edge> {
    private final String front, back;
    private final int weight;

    public Edge(String s1, String s2) {
        if (s1.compareTo(s2) > 0) {
            front = s2;
            back = s1;
        } else {
            front = s1;
            back = s2;
        }
        int fMag = front.chars().map(c -> (char)c).map(Character::getNumericValue).sum();
        int bMag = front.chars().map(c -> (char)c).map(Character::getNumericValue).sum();
        weight = Integer.min(fMag,bMag);
    }

    public String front() {
        return front;
    }
    public String back() {
        return back;
    }
    public int weight() {
        return weight;
    }
    public String complement(String s) {
        if (s.equals(front)) return back;
        else if (s.equals(back)) return front;
        else throw new IllegalThreadStateException();
    }

    public boolean contains(String s) {
        return front.equals(s) || back.equals(s);
    }

    @Override
    public int compareTo(Edge o) {
        return toString().compareTo(o.toString());
    }
    @Override
    public String toString() {
        return front+"-"+back;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Edge e) {
            return front.equals(e.front) && back.equals(e.back);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static boolean isAdjacent(String s1, String s2) {
        boolean difference = false;
        for (int i = 0; i < 5; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                if (difference) return false;
                else difference = true;
            }
        }
        return true;
    }

    public static class WeightComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge o1, Edge o2) {
            return Integer.compare(o1.weight,o2.weight);
        }
    }
}
