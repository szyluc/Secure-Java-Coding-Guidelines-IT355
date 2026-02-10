import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * The program shown here is a simple display of the EXP02-J rule 
 * which covers not using the Object.equals() method when comparing
 * arrays. The reason for this is it does not compare the contents 
 * of the array but instead the reference to the array. You can see
 * we avoid that problem in the program below by using the 
 * Array.equals() method which will properly compare the contents of
 * the arrays to see if they are equal or not. In order to showcase 
 * this, we generate an array of 5 random numbers between 1 and 10 
 * and then let the user pick 5 and compare them for equality for a 
 * sort of fun little guessing game.
 */
public class RandomNumberGuesser {
    public static void main(String[] args) {
        int[] array = new int[5];
        int[] userArray = new int[5];
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            array[i] = random.nextInt(10) + 1;
        }

        System.out.println("Enter five numbers of your choice between 1 and 10");
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < 5; i++) {
            System.out.println("Enter number " + (i + 1) + ": ");
            int number = input.nextInt();
            while (number < 1 || number > 10) {
                System.out.println("Please provide a number between 1 and 10: ");
                number = input.nextInt();
            }
            userArray[i] = number;
        }
        if (Arrays.equals(array, userArray)) {
            System.out.println("The numbers you guessed matched the random numbers generated");
        } else {
            System.out.println("The numbers you guessed did not match the random numbers generated");
        }
        input.close();
    }    
}
