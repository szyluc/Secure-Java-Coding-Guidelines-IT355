/**
 * RULE: OBJ01
 * Description: Limit accessibility of fields
 * Fix: Make instance variables private and implement getters/setters.
 */

public class Widget {
    private int total; // declared private

    public int getTotal() { 
        return total;
    }

    void add() {
        if (total < Integer.MAX_VALUE) {
            total++;
        } else {
            throw new ArithmeticException("Overflow");
        }
    }

    void remove() {
        if (total > 0) {
            total--;
        } else {
            throw new ArithmeticException("Overflow");
        }
    }
}