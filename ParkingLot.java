import java.util.*;
class ParkingLot {
    static class Spot {
        String plate;
        long inTime;
        Spot(String plate, long inTime) {
            this.plate = plate;
            this.inTime = inTime;
        }
    }
    private final Spot[] table;
    private final int n;
    public ParkingLot(int spots) {
        this.n = spots;
        this.table = new Spot[spots];
    }
    private int h(String plate) {
        return Math.abs(plate.hashCode()) % n;
    }
    public int parkVehicle(String plate) {
        int idx = h(plate);
        for (int i = 0; i < n; i++) {
            int j = (idx + i) % n;
            if (table[j] == null) {
                table[j] = new Spot(plate, System.currentTimeMillis());
                return j;
            }
        }
        return -1;
    }
    public String exitVehicle(String plate) {
        int idx = h(plate);
        for (int i = 0; i < n; i++) {
            int j = (idx + i) % n;
            if (table[j] == null) return "Not found";
            if (table[j].plate.equals(plate)) {
                long durMs = System.currentTimeMillis() - table[j].inTime;
                table[j] = null;
                long mins = durMs / 60000;
                return "Freed spot #" + j + ", Duration: " + mins + " min";
            }
        }
        return "Not found";
    }
    public static void main(String[] args) {
        ParkingLot p = new ParkingLot(5);
        System.out.println("Spot: " + p.parkVehicle("ABC-1234"));
        System.out.println("Spot: " + p.parkVehicle("ABC-1235"));
        System.out.println(p.exitVehicle("ABC-1234"));
    }
}
