import java.util.*;

public class HashTable {

    static class UsernameChecker {
        Hashtable<String, Integer> users = new Hashtable<>();
        Hashtable<String, Integer> attempts = new Hashtable<>();

        public boolean checkAvailability(String username) {
            attempts.put(username, attempts.getOrDefault(username, 0) + 1);
            return !users.containsKey(username);
        }

        public void register(String username, int userId) {
            users.put(username, userId);
        }

        public List<String> suggestAlternatives(String username) {
            List<String> list = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                String s = username + i;
                if (!users.containsKey(s)) list.add(s);
            }
            String s = username.replace("_", ".");
            if (!users.containsKey(s)) list.add(s);
            return list;
        }

        public String getMostAttempted() {
            int max = 0;
            String name = "";
            for (String u : attempts.keySet()) {
                if (attempts.get(u) > max) {
                    max = attempts.get(u);
                    name = u;
                }
            }
            return name;
        }
    }

    public static void main(String[] args) {
        UsernameChecker u = new UsernameChecker();
        u.register("johnny", 1);
        u.register("admin", 2);

        System.out.println(u.checkAvailability("john_doe"));
        System.out.println(u.checkAvailability("jane_smith"));
        System.out.println(u.suggestAlternatives("john_doe"));

        u.checkAvailability("admin");
        u.checkAvailability("admin");
        u.checkAvailability("admin");

        System.out.println(u.getMostAttempted());
    }
}
