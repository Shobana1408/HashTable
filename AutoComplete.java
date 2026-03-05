import java.util.*;
class AutoComplete {
    private final HashMap<String, Integer> freq = new HashMap<>();
    public void addQ(String q, int ct) {
        freq.put(q, freq.getOrDefault(q, 0) + ct);
    }
    public void updFreq(String q) {
        freq.put(q, freq.getOrDefault(q, 0) + 1);
    }
    public List<String> srch(String prefix) {   // FIXED
        ArrayList<String> list = new ArrayList<>();
        for (String q : freq.keySet()) {
            if (q.startsWith(prefix)) list.add(q);
        }
        list.sort((a, b) -> Integer.compare(freq.get(b), freq.get(a)));
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < Math.min(10, list.size()); i++) {
            String q = list.get(i);
            out.add(q + " (" + freq.get(q) + ")");
        }
        return out;
    }
    public static void main(String[] args) {
        AutoComplete a = new AutoComplete();
        a.addQ("java tutorial", 5);
        a.addQ("javascript", 3);
        a.addQ("java download", 2);
        System.out.println(a.srch("jav"));
        a.updFreq("java tutorial");
        System.out.println(a.srch("jav"));
    }
}
