import java.security.Key;
import java.util.Arrays;

/**
 * Simple Key compare class 
 */
public class KeyCompare {
    
    /**
     * Safely compares two cryptographic keys
     * 
     * @param key1
     * @param key2
     * @return
     */
    public static boolean keysEqual(Key key1, Key key2) {
        // if both references point to same object they are equal
        if (key1 == key2) return true;

        // if either key is null they cant be equal
        if (key1 == null || key2 == null) return false;

        // Compare encoded bytes
        // getEncode() returns the byte array representing the key
        // Arrays.equal compares the byte arrays element by element 
        // This ensures equality is based on the actualy key content instead of object indentity
        return Arrays.equals(key1.getEncoded(), key2.getEncoded());
    }
}
