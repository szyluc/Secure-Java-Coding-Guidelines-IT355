import java.util.Scanner;

/**
 * This program below shows how to properly follow the EXP53-J 
 * recommendation which focuses on using parantheses to ensure
 * proper order of operations during calculations. In this simple 
 * program below, we just take in the average response time with 
 * and without the cache being hit as well as the hit rate then 
 * calculate the average response time. The use of paranthses is 
 * important here as it ensures that we start by first calculating 
 * the left and right side of the equations where we figure out 
 * the hit and miss portion of the average response time then add
 * them together for the total average response time.
 */
public class DelayCalculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the average response time in seconds when cache is hit: ");
        double hitTime = input.nextDouble();
        System.out.println("Enter the average response time in seconds when cache is missed: ");
        double missTime = input.nextDouble();
        System.out.println("Enter the cache hit percentage in decimal form: ");
        double hitRatio = input.nextDouble();
        double averageTime = (hitTime * hitRatio) + (missTime * (1 - hitRatio));
        System.out.println("The average response time is: " + averageTime + " seconds");
        input.close();
    }
}
