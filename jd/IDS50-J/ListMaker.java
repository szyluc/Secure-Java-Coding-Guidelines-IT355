import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * The program below shows how to use conservative file naming
 * according to the IDS50-J recommendation. The use of certain 
 * characters in the name of a file can lead to issues like the 
 * program not working on all platforms properly as well as 
 * other security issues that can open attack surfaces in your
 * program that bad actors can exploit. You can see that we 
 * avoid this issue by just using basi alphanumeric characters
 * in our file name and give it a clear name that indicates the
 * purpose of the file. In this case, we just take item names
 * from a user until they decide to stop which we write to a file
 * as a simple list that they can use.
 */
public class ListMaker {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        File itemList = new File("ItemList.txt");
        FileWriter listWriter;
        try {
            listWriter = new FileWriter(itemList);
            System.out.println("Enter an item or type exit to stop:");
            String response = input.nextLine();
            while (response != "stop") {
                listWriter.write(response);
                System.out.println("Enter an item or type exit to stop:");
                response = input.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        input.close();
    }
}
