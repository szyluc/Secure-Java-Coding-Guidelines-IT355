import java.util.Map;
import java.util.HashMap;

/**
 * Class to implement rule MET09-J. 
 * Classes that define an equals() method must also define a hashCode() method.
 */
public final class SecretInfo {
    private final int secretNum;
    
    /**
     * Main method. Creates a hashmap and loads it with one <SecretInfo, String> pair.
     * @param args Console args
     */
    public static void main(String[] args) {
        Map<SecretInfo, String> m = new HashMap<>();
        m.put(new SecretInfo(999), "secretString");
    }

    /**
     * Constructor to take a number
     * @param number The secret number of the SecretInfo object
     */
    public SecretInfo(int number) {
        this.secretNum = number;
    }

    /**
     * Equals method to compare this object with another
     * @param o Object to compare this object to
     * @return True: this object is equal to o. False: this object is not equal to o.
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SecretInfo)) {
            return false;
        }
        SecretInfo secret = (SecretInfo)o;
        return secret.secretNum == this.secretNum;
    }

    /**
     * Method to return a hashcode of this object.
     * @return A hashcode of this object.
     */
    public int hashCode() {
        int result = 17;
        result = 31*result + secretNum;
        return result;
    }
}