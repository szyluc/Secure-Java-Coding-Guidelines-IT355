import java.sql.SQLException;
import java.io.File;

public class Driver {
    public static void main(String[] args) {
        // IDS07-J: Use File.list() to verify the import directory exists and
        // is accessible - never pass user-controlled paths to Runtime.exec()
        File importDir = new File(".");
        String[] availableFiles = importDir.list();
        if (availableFiles == null) {
            System.out.println("Import directory is not accessible");
            return;
        }

        


        InputController inputController = new InputController();
        ModifyBooks books = new ModifyBooks();
        ModifyAccounts accounts = new ModifyAccounts();
        // IDS07-J: Only load files by constructing safe File objects directly.
        // The filenames are hardcoded so there is no attack surface for command injection.
        File booksXML = new File("./books.xml");
        File adminsXML = new File("./admins.xml");
        // We can validate to see if both files exist and are files (not directories)
        if (!booksXML.isFile() || !adminsXML.isFile()) {
            System.out.println("Required import files are missing.");
            return;
        }

         // Rule TH100-J – Background XML import thread and menu
        Thread importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    books.importBooks(booksXML); // import pre-specified books
                    accounts.importAdmins(adminsXML); // import pre-specified admins
                } catch (SQLException sqlE) {
                    System.out.println("SQLException caught.");
                } catch(Exception e) {
                    System.out.println("Exception caught.");
                }
            }
        });

         // Use thread.start() instead of run before menu starts
        importThread.start();
        
        try {
            inputController.startMenu();
            inputController.loginMenu();
            inputController.userMainMenu();
        } catch (SQLException sqlE) {
            System.out.println("SQLException caught.");
        } catch (Exception e) {
            System.out.println("Exception caught.");
        } finally {
            try {
                inputController.cleanUp();
            } catch (Exception e) {
                System.out.println("Exception caught.");
            }
        }
        
        
    }
}
