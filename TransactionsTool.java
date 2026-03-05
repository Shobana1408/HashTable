import java.util.*;
class TransactionsTool {
    static class Tx{
        int id;
        int amt;
        String merch;
        long tmMs;
        String acc;
        Tx(int id, int amt, String merch, long tmMs, String acc){
            this.id = id;
            this.amt = amt;
            this.merch = merch;
            this.tmMs = tmMs;
            this.acc = acc;
        }
    }
    public List<int[]> twoSum(List<Tx> txs, int target) {
        HashMap<Integer, Integer> seen = new HashMap<>();
        ArrayList<int[]> res = new ArrayList<>();
        for (Tx t : txs) {
            int need = target - t.amt;
            if (seen.containsKey(need)) res.add(new int[]{seen.get(need), t.id});
            seen.putIfAbsent(t.amt, t.id);
        }
        return res;
    }
    public List<int[]> twoSumWithinWindow(List<Tx> txs, int target, long windowMs) {
        txs.sort(Comparator.comparingLong(a -> a.tmMs));
        HashMap<Integer, Tx> seen = new HashMap<>();
        ArrayList<int[]> res = new ArrayList<>();
        int left = 0;
        for (int right = 0; right < txs.size(); right++) {
            Tx cur = txs.get(right);
            while (cur.tmMs - txs.get(left).tmMs > windowMs) {
                Tx old = txs.get(left);
                if (seen.get(old.amt) == old) seen.remove(old.amt);
                left++;
            }
            int need = target - cur.amt;
            if (seen.containsKey(need)) res.add(new int[]{seen.get(need).id, cur.id});
            seen.put(cur.amt, cur);
        }
        return res;
    }
    public List<String> detectDuplicates(List<Tx> txs) {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        for (Tx t : txs) {
            String key = t.amt + "|" + t.merch;
            map.computeIfAbsent(key, k -> new HashSet<>()).add(t.acc);
        }
        ArrayList<String> res = new ArrayList<>();
        for (String k : map.keySet()) {
            if (map.get(k).size() >= 2) res.add(k + " accounts=" + map.get(k));
        }
        return res;
    }
    public static void main(String[] args) {
        TransactionsTool tool = new TransactionsTool();
        long base = System.currentTimeMillis();
        List<Tx> txs = Arrays.asList(
                new Tx(1, 500, "Store A", base, "acc1"),
                new Tx(2, 300, "Store B", base + 15 * 60000, "acc2"),
                new Tx(3, 200, "Store C", base + 30 * 60000, "acc3"),
                new Tx(4, 500, "Store A", base + 35 * 60000, "acc9")
        );
        System.out.println(tool.twoSum(txs, 500));
        System.out.println(tool.twoSumWithinWindow(new ArrayList<>(txs), 500, 3600_000L));
        System.out.println(tool.detectDuplicates(txs));
    }
}
