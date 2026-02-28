import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
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
    private static final String BOOK_INSERT = """
            INSERT INTO book (book_id, book_name, book_author, book_category)
            VALUES (?, ?, ?, ?);
        """;
    private static final String BOOK_DB_NAME = "book";


    /**
     * Default constructor for the ModifyBooks class.
     */
    public ModifyBooks() {
        super(BOOK_DB_NAME);
    }
    

    /**
     * Allows admin to add a Book based on Book object
     * Gets added to the database
     * @param b
     */
    public void addBook(Book b) {
        // Think I haft to fix this try statement
        try(PreparedStatement pstmt = getConnection().prepareStatement(BOOK_INSERT)) {
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

    public void importBooks(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);

        NodeList booksNodeList = document.getElementsByTagName("books");
        ArrayList<Book> booksToAdd = new ArrayList<>();
        for (int i=0; i<booksNodeList.getLength(); i++) {
            NodeList bookInfo = document.getElementsByTagName(BOOK_DB_NAME);
            Book curBook = new Book(
                bookInfo.item(0).getTextContent(),
                bookInfo.item(1).getTextContent(),
                bookInfo.item(2).getTextContent()
            );
            booksToAdd.add(curBook);
            
            // then, we should add books in bulk to avoid multiple opens and closes
        }
        addBooks(booksToAdd);
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

    private boolean addBooks(ArrayList<Book> books) throws SQLException {
        if (books == null || books.size() == 0) {
            return false; // cannot add zero books
        }
        openConnection();
        // create book DB if necessary
        Statement createBookDB = connection.createStatement();
        createBookDB.executeUpdate(BOOK_DB);
        createBookDB.close();

        for (Book curBook: books) {
            PreparedStatement addBookToDB = connection.prepareStatement(BOOK_INSERT);
            addBookToDB.setString(1, curBook.getBookId());
            addBookToDB.setString(2, curBook.getBookName());
            addBookToDB.setString(3, curBook.getBookAuthor());
            addBookToDB.setString(4, curBook.getBookCategory());
            addBookToDB.executeUpdate();
        }
        closeConnection();
        return true;
    }


}
