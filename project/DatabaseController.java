import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TO COMPILE:
 * javac -cp ".;lib/sqlite-jdbc-3.51.2.0.jar" DatabaseController.java
 * java -cp ".;lib/sqlite-jdbc-3.51.2.0.jar" DatabaseController
 */
class DatabaseController {
    private static final String BOOK_DB = """
            CREATE TABLE IF NOT EXIST book (
                book_id TEXT PRIMARY KEY,
                book_name TEXT NOT NULL,
                book_author TEXT NOT NULL,
                book_category TEXT NOT NULL
            );
            """;
    private static final String ACCOUNT_DB = """
            CREATE TABLE IF NOT EXIST account (
                account_id TEXT PRIMARY KEY,
                account_name TEXT NOT NULL,
                account_dob DATE NOT NULL,
                account_role TEXT NOT NULL
            );
            """;
    private static final String BOOK_ACCOUNT = """
            CREATE TABLE IF NOT EXIST book_account (
                b_id TEXT NOT NULL,
                a_id TEXT NOT NULL,
                checkout_date DATE NOT NULL,
                PRIMARY KEY (b_id, a_id),
                FOREIGN KEY (b_id) REFERENCES book(book_id),
                FOREIGN KEY (a_id) REFERENCES account(account_id)
            );
            """;
    private String databaseName;
    private Connection databaseConnection;



    private boolean openDB(String dbName) {
        
    }


    public DatabaseController(String databaseName) {
        // databaseDriver = some arbitrary driver yet to be installed
        this.databaseName = databaseName;
    }

    // // CRUD methods
    // public boolean addBook(Book book) {
        
    // }

    // public boolean removeBook(Book book) {
    //     // should first check if this book exists.
    //     // if book does not exist, cannot be deleted.
    // }

    // public boolean readBook() {

    // }

    // public boolean modifyBook() {

    // }

    private boolean openConnection() throws SQLException {
        if (databaseConnection == null) {
            databaseConnection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".db");
            return true;
        }
        // let the consumer know that the open failed (already open or SQLException)
        return false;
    }

    private boolean closeConnection() throws SQLException {
        if (databaseConnection != null) {
            databaseConnection.close();
            return true;
        }
        return false;
    }

    private void createLibraryDatabase() throws SQLException {
        Statement stmt = databaseConnection.createStatement();
    }

    private void addToBookTest() throws SQLException {
        openConnection();
        String sqlString = """
                INSERT INTO book (book_id, book_name, book_author, book_category)
                VALUES ("abc", "IT355 Class Book", "Illinois State", "Education");
                """;
        Statement stmt = databaseConnection.createStatement();
        int result = stmt.executeUpdate(sqlString);
        System.out.println(result);
        
    }

    
}