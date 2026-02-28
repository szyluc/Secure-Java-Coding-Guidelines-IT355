import java.sql.*;
import java.util.UUID;
/**
 * Class extends DatabaseController and simplifies the calls.
 */
public class ModifyBooks extends DatabaseController {

    private static final String BOOK_DB = """
        CREATE TABLE IF NOT EXISTS book (
            book_id TEXT PRIMARY KEY,
            book_name TEXT NOT NULL,
            book_author TEXT NOT NULL,
            book_category TEXT NOT NULL
        );
        """;

    private final String BOOK_DB_NAME = "book";


    /**
     * Default constructor for the ModifyBooks class.
     */
    public ModifyBooks(String dbName) {
        super(dbName);
    }
    

    /**
     * Allows admin to add a Book based on Book object
     * Gets added to the database
     * @param b
     */
    public void addBook(Book b) {
        String sql = """
            INSERT INTO book (book_id, book_name, book_author, book_category)
            VALUES (?, ?, ?, ?);
        """;

        // Think I haft to fix this try statement
        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, b.getBookId());
            pstmt.setString(2, b.getBookName());
            pstmt.setString(3, b.getBookAuthor());
            pstmt.setString(4, b.getBookCategory());

            pstmt.executeUpdate();
            System.out.println("Book added Successfully");
        } catch (SQLException e) {
            System.out.println("Error adding book.");
            e.printStackTrace();
        }
                
                
    }
    /**
     * Allows admin to add a book with XML
     * @param xmlLocation
     */
    public void addBook(String xmlLocation) {

    }

    /**
     * Updates book information in the database
     * Checks if book got updated
     * If not throw error
     */
    public void updateBook(Book b) {
        String sql = """
            UPDATE book
            SET book_name = ?, book_author = ?, book_category = ?
            WHERE book_id = ?;
        """;

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {

            pstmt.setString(1, b.getBookName());
            pstmt.setString(2, b.getBookAuthor());
            pstmt.setString(3, b.getBookCategory());
            pstmt.setString(4, b.getBookId());

            // Check if the book got updated
            int rows = pstmt.executeUpdate();

            if(rows > 0) {
                System.out.println("Book Updated Sucessfully");
            } else {
                System.out.println("Book not found.");
            } 
        }
            catch (SQLException e) {
                e.printStackTrace();;
            }
        

    }

    /**
     * Deletes  a book from the database
     * Checks to see if book got deleted
     * Otherwise throw error 
     * @param b
     */
    public void deleteBook(Book b) {
        String sql = "DELETE FROM book WHERE book_id = ?;";

        try(PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, b.getBookId());

            // Check if book row got deleted
            int rows = pstmt.executeUpdate();

            if(rows> 0) {
                System.out.println("Book deleted Successfully.");
            } else {
                System.out.println("Book not found.");
            } 
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBook(UUID bookID) throws SQLException {
        // Check that book exists
        if (bookID == null) {
            return null; // invalid ID
        }

        // Open connection
        openConnection();

        // Get book from database
        String getBookString = "SELECT 1 FROM ? WHERE book_id =?";
        PreparedStatement getBookFromDB = connection.prepareStatement(getBookString);
        getBookFromDB.setString(1, BOOK_DB_NAME);
        getBookFromDB.setString(2, bookID.toString());
        Object bookObject = getBookFromDB.executeQuery().getObject(1);
        Book book = (Book)bookObject;

        // Close connection
        closeConnection();

        // Return book object
        return book;
    }


}
