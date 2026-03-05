import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;


public class Err02J {
    private static final Logger logger = Logger.getLogger(Err02J.class.getName());

    public static void main(String[] args) {

        Path filepath = Paths.get("filetooread.txt");

        try {
            String fileContent = Files.readString(filepath);
            System.out.println(fileContent);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Read file error occurred for file" + filepath, e);
            System.out.println("This file could not be read, make sure it exists.");
        }

    }
}