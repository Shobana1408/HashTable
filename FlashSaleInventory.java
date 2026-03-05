import java.util.*;
import java.util.concurrent.*;
class FlashSaleInventory {
    private final ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> wait = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();
    public void addProd(String prID, int units){
        stock.put(prID, units);
        wait.putIfAbsent(prID, new ConcurrentLinkedQueue<>());
        locks.putIfAbsent(prID, new Object());
    }
    public String checkStock(String prID){
        int s = stock.getOrDefault(prID, 0);
        return s + " units available";
    }
    public String prchItem(String prID, int usID){
        locks.putIfAbsent(prID, new Object());
        wait.putIfAbsent(prID, new ConcurrentLinkedQueue<>());
        stock.putIfAbsent(prID,0);
        synchronized (locks.get(prID)){
            int s = stock.get(prID);
            if(s>0){
                stock.put(prID, s-1);
                return "Success, "+(s-1)+" units remaining";
            }
            else{
                ConcurrentLinkedQueue<Integer> q = wait.get(prID);
                q.add(usID);
                return "Added to waiting list, position #"+q.size();
            }
        }
    }
    public Integer fromWaitList(String prID){
        locks.putIfAbsent(prID, new Object());
        wait.putIfAbsent(prID,new ConcurrentLinkedQueue());
        stock.putIfAbsent(prID, 0);
        synchronized ((locks.get(prID))){
            if(stock.get(prID)<=0) return null;
            Integer user = wait.get(prID).poll();
            if (user==null) return  null;
            stock.put(prID, stock.get(prID)-1);
            return user;
        }
    }
    public static void main(String[] args){
        FlashSaleInventory f = new FlashSaleInventory();
        f.addProd("IPHONE15_256GB",2);;
        System.out.println(f.checkStock("IPHONE15_256GB"));
        System.out.println(f.prchItem("IPHONE15_256GB", 12345));
        System.out.println(f.prchItem("IPHONE15_256GB", 67890));
        System.out.println(f.prchItem("IPHONE15_256GB", 99999));
    }

}
