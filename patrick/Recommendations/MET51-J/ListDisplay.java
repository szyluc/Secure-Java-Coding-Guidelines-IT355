
import java.util.*;

/**
 * Class to implement recommendation MET51-J. 
 * Do not use overloaded methods to differentiate between runtime types
 */
public class ListDisplay {
    /**
     * Displays the type of list provided
     * @param list The list
     * @return A string representation of this list
     */
    private static String display(List<?> list) {
        if (list instanceof ArrayList) {
            return "ArrayList";
        } if (list instanceof LinkedList) {
            return "LinkedList";
        }
        return "List is not recognized";
    }

    /**
     * Main method
     * @param args console args
     */
    public static void main(String[] args) {
        // initial check
        System.out.println(display(new ArrayList<Character>()));

        List<?>[] lists = new List<?>[] {
            new ArrayList<Float>(),
            new LinkedList<String>(),
            new Vector<Double>()
        };

        // iterate
        for (List<?> list : lists) {
            System.out.println(display(list));  
        }
    }
}



