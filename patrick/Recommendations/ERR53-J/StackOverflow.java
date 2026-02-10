/**
 * RECOMMENDATION: ERR53J
 * Description: Try to gracefully recover from system errors
 * Fix: allow a class to catch a throwable to allow the system to recover from an error
 */

public class StackOverflow {
    public static void main(String[] args) {
        try {
            infiniteRun();
        } catch (Throwable t) {
            // Forward to some handler
        } finally {
            // Free cache / release resources as necessary
        }
    }

    private static void infiniteRun() {
        infiniteRun();
    }
}

