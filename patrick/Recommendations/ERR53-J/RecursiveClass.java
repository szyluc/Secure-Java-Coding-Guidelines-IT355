/**
 * Class to implement recommendation ERR53-J. 
 * Try to gracefully recover from system errors
 */
public class RecursiveClass {
    /**
     * Main method
     * @param args Console args
     */
    public static void main(String[] args) {
        try {
            recursive(0);
        } catch (StackOverflowError e) {
            System.out.println("Stack overflow!"); // Once stackoverflow occurs, we print and exit
            return;
        }
    }

    /**
     * Recursive function to add 1.
     * @param x current integer value
     */
    private static void recursive(int x) {
        int y = x+1;
        recursive(y);
    }
}

