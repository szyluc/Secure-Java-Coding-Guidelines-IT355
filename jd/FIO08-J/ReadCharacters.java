import java.io.FileReader;

/**
 * This simple program demonstrates the FIO08-J rule by showing how to 
 * properly distinguish the characters or bytes you are reading from a 
 * stream and -1. The program shown simply reads in an input file and 
 * outputs the contents to the console to be displayed to the user. In 
 * this example below, we first capture the input from the stream as an 
 * integer and check that it is not -1 to ensure it is not the end of 
 * the stream. After that, we cast it to the actual type which is a 
 * character in this case and then perform the actions we need to like 
 * printing the character out here. This ensures you don't encounter any 
 * problems such as missing the end of stream character or prematurely 
 * ending the stream.
 */
public class ReadCharacters {
    public static void main(String[] args) {
        try {
            FileReader file = new FileReader("Input.txt");
            int buffer;
            char data;
            while ((buffer = file.read()) != -1) {
                data = (char) buffer;
                System.out.print(data);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}
