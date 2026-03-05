import java.util.*;
class PlagiarismDetector {
    private final int n;
    private final HashMap<String, HashSet<String>> index = new HashMap<>();
    private final HashMap<String, Integer> dgCount = new HashMap<>();
    public PlagiarismDetector(int n){
        this.n = n;
    }
    public void addDocument(String docID, String text){
        List<String> wds = tokenize(text);
        HashSet<String> seen = new HashSet<>();
        for(int i=0; i+n<=wds.size(); i++){
            String gram = String.join(" ",wds.subList(i, i+n));
            if(seen.add(gram)){
                index.computeIfAbsent(gram, k->new HashSet<>()).add(docID);
            }
        }
        dgCount.put(docID, Math.max(1, seen.size()));
    }
    public List<String> findMostSimilar(String queryDocID, String querytxt, int topK){
        List<String> wds = tokenize(querytxt);
        HashMap<String, Integer> matchCT = new HashMap<>();
        HashSet<String> grams = new HashSet<>();
        for (int i = 0; i + n <= wds.size(); i++) {
            grams.add(String.join(" ", wds.subList(i, i + n)));
        }
        for (String g : grams) {
            HashSet<String> docs = index.get(g);
            if (docs == null) continue;
            for (String d : docs) {
                matchCT.put(d, matchCT.getOrDefault(d, 0) + 1);
            }
        }
        ArrayList<String> docs = new ArrayList<>(matchCT.keySet());
        docs.sort((a, b) -> Integer.compare(matchCT.get(b), matchCT.get(a)));
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, docs.size()); i++) {
            String d = docs.get(i);
            int matches = matchCT.get(d);
            int base = dgCount.getOrDefault(d, 1);
            double sim = matches * 100.0 / base;
            res.add(d + " → matches=" + matches + ", similarity=" + String.format("%.1f", sim) + "%");
        }
        return res;
    }
    private List<String> tokenize(String text) {
        String[] arr = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").trim().split("\\s+");
        ArrayList<String> out = new ArrayList<>();
        for (String s : arr) if (!s.isBlank()) out.add(s);
        return out;
    }
    public static void main(String[] args) {
        PlagiarismDetector p = new PlagiarismDetector(5);
        p.addDocument("essay_089", "this is a sample essay text about networks and security concepts in university");
        p.addDocument("essay_092", "this is a sample essay text about networks and security concepts in university with extra lines");
        System.out.println(p.findMostSimilar("essay_123", "this is a sample essay text about networks and security concepts in university", 2));
    }
}
