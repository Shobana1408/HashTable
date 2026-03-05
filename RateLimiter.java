import java.util.*;
class RateLimiter {
    static class Bucket {
        int tokens;
        long lRefill;
        Bucket(int tokens, long lRefill) {
            this.tokens = tokens;
            this.lRefill = lRefill;
        }
    }
    private final HashMap<String, Bucket> map = new HashMap<>();
    private final int maxT;
    private final int refpHr;
    public RateLimiter(int maxTokens) {
        this.maxT = maxTokens;
        this.refpHr = maxTokens;
    }
    public synchronized String checkRateLimit(String clientId) {
        long now = System.currentTimeMillis();
        Bucket b = map.get(clientId);
        if (b == null) {
            b = new Bucket(maxT, now);
            map.put(clientId, b);
        }
        long elapsed = now - b.lRefill;
        if (elapsed >= 3600_000L) {
            b.tokens = maxT;
            b.lRefill = now;
        }
        if (b.tokens > 0) {
            b.tokens--;
            return "Allowed (" + b.tokens + " remaining)";
        }
        else {
            long retry = 3600_000L - (now - b.lRefill);
            return "Denied (0 remaining, retry after " + (retry / 1000) + "s)";
        }
    }
    public static void main(String[] args) {
        RateLimiter r = new RateLimiter(5);
        for (int i = 0; i < 7; i++) System.out.println(r.checkRateLimit("abc123"));
    }
}
