/**
 * RULE: MET09-J
 */

import java.util.Map;
import java.util.HashMap;

public final class CreditCard {
    private final int number;

    public CreditCard(int number) {
        this.number = number;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CreditCard)) {
            return false;
        }
        CreditCard cc = (CreditCard)o;
        return cc.number == this.number;
    }

    public int hashCode() {
        int result = 17;
        result = 31*result + number;
        return result;
    }
}

public static void main(String[] args) {
    Map<CreditCard, String> m = new HashMap<>();
    m.put(new CreditCard(100), "1234123412341234");
}