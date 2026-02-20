/* OBJ52-J. Write garbage-collection-friendly code

   The way JVM handles garbage collection can be exploited by attackers.
   Attackers can force heavy allocations or long-lived objects that can increase CPU, battery, and memory use.
   This causes the system to slow down instead of crashing.

   The recommended way to help the JVM collection is to work with short-lived objects.
   Long-lived mutable objects can cause problems with garbage collection efficiency.
   Avoidng large object allocations can help with memory fragmentation that can slow down the GC.
   Do not manually call the GC with System.gc() this only suggests a GC cycle and can be called at the wrong time which can slow the system.

*/

import java.util.ArrayList;
import java.util.List;

class StringPool {
    //As long as the pool exists, the objects are still reachable.
    //Considering that if the pool is not empty, it will stay in memory for a long time.
    private List<StringBuilder> pool = new ArrayList<>();

    public StringBuilder get() {
        if (pool.isEmpty()) {
            return new StringBuilder();
        }
        return pool.remove(pool.size() - 1);
    }

    public void release(StringBuilder sb) {
        sb.setLength(0);
        pool.add(sb);
    }

}

class AccountBalance {
    public static String outputBalance(String user, int balance) {
        return "User: " + user + " Balance: " + balance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            //Strings are quickly used then garbage collected when no longer needed.
            System.out.println(outputBalance("John", i));
        }
    }
}
