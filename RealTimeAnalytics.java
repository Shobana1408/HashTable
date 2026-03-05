import com.sun.jdi.PrimitiveValue;
import java.security.PublicKey;
import java.util.*;
class RealTimeAnalytics {
    static class Event{
        String ur1, userId, src;
        Event(String ur1, String userId, String src) {
            this.ur1 = ur1;
            this.userId = userId;
            this.src = src;
        }
    }
        private final HashMap<String, Integer> pgvw = new HashMap<>();
        private final HashMap<String , HashSet<String>> unique = new HashMap<>();
        private final HashMap<String, Integer>srcs= new HashMap<>();
        public void prcEvent(Event e){
            pgvw.put(e.ur1, pgvw.getOrDefault(e.ur1,0)+1);
            unique.computeIfAbsent(e.ur1, k->new HashSet<>()).add(e.userId);
            srcs.put(e.src, srcs.getOrDefault(e.src,0)+1);
        }
        public String getDashboard() {
            List<String> pages = new ArrayList<>(pgvw.keySet());
            pages.sort((a, b) -> Integer.compare(pgvw.get(b), pgvw.get(a)));
            StringBuilder sb = new StringBuilder();
            sb.append("Top Pages:\n");
            for (int i = 0; i < Math.min(10, pages.size()); i++) {
                String u = pages.get(i);
                sb.append((i + 1)).append(". ").append(u)
                        .append(" - ").append(pgvw.get(u)).append(" views (")
                        .append(unique.getOrDefault(u, new HashSet<>()).size()).append(" unique)\n");
            }
            int total = 0;
            for (int v : srcs.values()) total += v;
            sb.append("\nTraffic Sources:\n");
            for (String s : srcs.keySet()) {
                double pct = total == 0 ? 0 : srcs.get(s) * 100.0 / total;
                sb.append(s).append(": ").append(String.format("%.1f", pct)).append("%\n");
            }
            return sb.toString();
        }
        public static void main(String[] args) {
            RealTimeAnalytics a = new RealTimeAnalytics();
            a.prcEvent(new Event("/article/breaking-news", "user_123", "google"));
            a.prcEvent(new Event("/article/breaking-news", "user_456", "facebook"));
            a.prcEvent(new Event("/sports/championship", "user_123", "direct"));
            System.out.println(a.getDashboard());
        }
}
