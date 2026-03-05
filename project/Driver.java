import java.sql.SQLException;
import java.io.File;

/**
 * Entry point for Library Management System
 * 
 * Driver class is responsible for initializing core system components,
 * importing data from xml files, and launching user interface menus.
 * 
 * During startup, book and administrator data are imported in seperate background thread
 * to support multithreading. The main thread then launches the menu
 */
public class Driver {
    /**
     * Main entry of application
     * Initializes controllers and starts background data import
     * Launches user menus
     * @param args not used
     */
    public static void main(String [] args) {
        InputController inputController = new InputController();
        ModifyBooks books = new ModifyBooks();
        ModifyAccounts accounts = new ModifyAccounts();
        File booksXML = new File("./books.xml");
        File adminsXML = new File("./admins.xml");

         // Rule TH100-J – Background XML import thread and menu
        Thread importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    books.importBooks(booksXML); // import pre-specified books
                    accounts.importAdmins(adminsXML); // import pre-specified admins
                } catch (SQLException sqlE) {
                    sqlE.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
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
            sqlE.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputController.cleanUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
    }
}
