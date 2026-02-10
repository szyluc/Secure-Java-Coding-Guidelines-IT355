import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This small program below showcases the FIO50-J recommendation 
 * which covers not making assumptions about file creation such 
 * as if the files already exists or fails to be created which is 
 * frequently overlooked at can lead to issues with your program. 
 * So that we can properly avoid this problem, we followed the 
 * solution recommended by the rule which is using the atomic method 
 * that java provides for file creation which also fails if the file 
 * already exists. This simple program will just create a file the 
 * first time that it is ran indicating that the program has been 
 * ran and will just indicate the program has ran before if the 
 * method fails because the file already exists.
 */
public class RunTracker {
    public static void main(String[] args) {
        BufferedOutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get("History.txt"), StandardOpenOption.CREATE_NEW));
            String successResponse = "This program has been run previously";
            outputStream.write(successResponse.getBytes());
            System.out.println("The flag marking that this program has ran before has been created");
            outputStream.close();
        } catch (Exception e) {
            System.out.println("The file marking that this program has ran before already exists");
        }
    }
}
