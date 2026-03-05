import java.util.*;
class MultilevelCache {
    static class LRU<K, V> extends LinkedHashMap<K, V> {
        private final int cap;
        LRU(int cap) {
            super(16, 0.75f, true);
            this.cap = cap;
        }
        protected boolean remEld(Map.Entry<K, V> e) { return size() > cap; }
    }
    private final LRU<String, String> L1 = new LRU<>(3);
    private final HashMap<String, String> L2 = new HashMap<>();
    private final HashMap<String, String> L3 = new HashMap<>();
    private int l1Hit=0, l2Hit=0, l3Hit=0;
    public void putInDB(String videoId, String data) {
        L3.put(videoId, data);
    }
    public String getVideo(String vidId) {
        if (L1.containsKey(vidId)) { l1Hit++; return "L1 HIT → " + L1.get(vidId); }
        if (L2.containsKey(vidId)) {
            l2Hit++;
            String data = L2.get(vidId);
            L1.put(vidId, data);
            return "L2 HIT → Promoted to L1 → " + data;
        }
        if (L3.containsKey(vidId)) {
            l3Hit++;
            String data = L3.get(vidId);
            L2.put(vidId, data);
            return "L3 HIT → Added to L2 → " + data;
        }
        return "Not found";
    }
    public String stats() {
        int total = l1Hit + l2Hit + l3Hit;
        return "L1=" + l1Hit + ", L2=" + l2Hit + ", L3=" + l3Hit + ", Total=" + total;
    }
    public static void main(String[] args) {
        MultilevelCache c = new MultilevelCache();
        c.putInDB("video_123", "DATA123");
        c.putInDB("video_999", "DATA999");
        System.out.println(c.getVideo("video_123"));
        System.out.println(c.getVideo("video_123"));
        System.out.println(c.getVideo("video_999"));
        System.out.println(c.stats());
    }
}
