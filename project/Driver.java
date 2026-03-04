import java.sql.SQLException;
import java.io.File;

public class Driver {
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

        try {
            inputController.startMenu();
            inputController.loginMenu();
            inputController.userMainMenu();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        // Use thread.start() instead of run
        importThread.start();
        
    }
}
