import java.util.Date;

/**
 * Demonstrates secure defensive copying of untrusted Date input.
 */
public class SafeDateStorage {

    /**
     * Validates that the provided time is not in the past.
     */
    private static boolean validateValue(long time) {
        return time >= System.currentTimeMillis();
    }

    /**
     * Securely stores a Date by creating a new Date
     */
    public static void storeDate(Date date) {
        Date copy = new Date(date.getTime());

        if (validateValue(copy.getTime())) {
            System.out.println("Storing time: " + copy.getTime());
        } else {
            System.out.println("Invalid time.");
        }
    }
}

