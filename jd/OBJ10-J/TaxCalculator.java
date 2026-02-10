import java.util.Scanner;

/**
 * This simple program is an example of the OBJ10-J rule which covers
 * that you should avoid using public static nonfinal fields as they
 * can lead to a wide variety of issues including problems when you 
 * are using multiple threads as well as introduces some serious 
 * security issues that can be easily exploited by bad actors. You 
 * can see in the program below that in order to avoid that we simply 
 * just use a public static final field where we store our tax rate 
 * which solves the forementioned issues. The program then uses that
 * cosntant tax rate to calculate the tax and total price for a given
 * subtotal provided by the user.
 */
public class TaxCalculator {
    public static final double TAX_RATE = 0.09;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the subtotal of a purchase: ");
        double subtotal = input.nextDouble();
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;
        System.out.println("Tax: " + tax);
        System.out.println("Total: " + total);
        input.close();
    }    
}
