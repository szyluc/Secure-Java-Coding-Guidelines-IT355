/**
 * RULE: MET51-J
 * DESCRIPTION: Do not use overloaded methods to differentiate between runtime types
 * FIX: Implement across the global type. If subtype, print. If another subtype, print.
 */

import java.util.*;

public class Overloader {
    private static String display(List<?> list) {
        return (
            list instanceof ArrayList ? "ArrayList" : (
                list instanceof LinkedList ? "LinkedList" : (
                    "List is not recognized."
                )
            )
        );
    }

    public static void main(String[] args) {
        System.out.println(display(new ArrayList<Integer>()));

        List<?>[] invokeAll = new List<?>[] {
            new ArrayList<Integer>(),
            new LinkedList<Integer>(),
            new Vector<Integer>()
        };

        for (List<?> list : invokeAll) {
            System.out.println(display(list));  
        }
    }
}



