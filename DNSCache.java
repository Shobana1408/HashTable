import java.util.*;
class DNSCache {
    static class Entry{
        String ip;
        long expMs;
        Entry(String ip, long expMs){
            this.ip = ip;
            this.expMs = expMs;
        }
    }
    private final int cap;
    private final LinkedHashMap<String, Entry> cache;
    private long hits =0, misses=0;
    public DNSCache(int cap){
        this.cap = cap;
        this.cache = new LinkedHashMap(16, 0.75f, true){
            protected boolean remEldest(Map.Entry<String, Entry> e){
                return size() > DNSCache.this.cap;
            }
        };
    }
    public synchronized String resolve(String domain, int ttlSec){
        long now = System.currentTimeMillis();
        Entry e = cache.get(domain);
        if(e != null && now <= e.expMs){
            hits++;
            return "Cache HIT -> "+e.ip;
        }
        if(e!=null) cache.remove(domain);
        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new Entry(ip, now+ttlSec*1000L));
        return "Cache MISS -> "+ip+" (TTL: "+ttlSec+"s)";
    }
    private String queryUpstream(String domain){
        int h = Math.abs(domain.hashCode());
        return (172+(h%50))+"."+((h/50)%255)+"."+((h/200)%255)+"."+((h/500)%255);
    }
    public synchronized String getCache(){
        long ttl = hits+misses;
        double hitRate = ttl == 0? 0.0:(hits*100.0/ttl);
        return "Hit Rate: "+String.format("%.2f", hitRate)+"%(hits="+hits+", misses="+misses+")";
    }
    public synchronized void cleanupExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Entry>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Entry> x = it.next();
            if (now > x.getValue().expMs) it.remove();
        }
    }
    public static void main(String[] args) throws Exception {
        DNSCache dns = new DNSCache(3);
        System.out.println(dns.resolve("google.com", 3));
        System.out.println(dns.resolve("google.com", 3));
        Thread.sleep(3100);
        dns.cleanupExpired();
        System.out.println(dns.resolve("google.com", 3));
        System.out.println(dns.getCache());
    }
}
