import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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

    private static final String BOOK_DB_NAME = "book";

    private static final String BOOK_INSERT = "INSERT INTO " + BOOK_DB_NAME 
    + "(book_id, book_name, book_author, book_category) VALUES (?, ?, ?, ?);";

    /**
     * Default constructor for the ModifyBooks class.
     */
    public ModifyBooks() {
        super(BOOK_DB_NAME);
    }

    /**
     * Allows admin to add a Book to the Book database.
     * @param book The book object to be added.
     * @return True: book add was successful. False: book add failed.
     * @throws SQLException If a SQL exception takes place.
     */
    public boolean addBook(Book book) throws SQLException {
        if (book == null) {
            return false; // book add failed
        }
        openConnection();
        // Create table if necessary.
        Statement createBookDB = connection.createStatement();
        createBookDB.executeUpdate(BOOK_DB);
        createBookDB.close();

        // Then, add a book to the database
        PreparedStatement addBookToDB = connection.prepareStatement(BOOK_INSERT);
        addBookToDB.setString(1, book.getBookId().toString());
        addBookToDB.setString(2, book.getBookName());
        addBookToDB.setString(3, book.getBookAuthor());
        addBookToDB.setString(4, book.getBookCategory());
        addBookToDB.executeUpdate();

        closeConnection(); // Finally, close connection
        return true;
    }

    /**
     * 
     * @param bookID
     * @return
     * @throws SQLException
     */
    public boolean removeBook(UUID bookID) throws SQLException {
        if (bookID == null) {
            return false;
        }
        openConnection();
        // The table SHOULD exist at this point. If not, an error should be thrown.
        String removeBookString = "DELETE FROM " + BOOK_DB_NAME + " WHERE book_id = ?";

        PreparedStatement removeBookFromDB = connection.prepareStatement(removeBookString);
        removeBookFromDB.setString(1, bookID.toString());
        removeBookFromDB.executeUpdate();

        closeConnection();
        return true;
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
            pstmt.setString(4, b.getBookId().toString());

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

    public Book getBook(UUID bookID) throws SQLException {
        // Check that book exists
        if (bookID == null) {
            return null; // invalid ID
        }

        // Open connection
        openConnection();

        // Get book from database
        String getBookString = "SELECT 1 FROM ? WHERE book_id = ?";
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

    public List<Book> getBookByName(String bookName) throws SQLException {
        // Open connection
        openConnection();

        // Get book from database
        String getBookByNameString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_name = ?";
        PreparedStatement getBookByNameFromDB = connection.prepareStatement(getBookByNameString);
        getBookByNameFromDB.setString(1, bookName);
        List<Book> books = new ArrayList<>();
        ResultSet resultSet = getBookByNameFromDB.executeQuery();
        while (resultSet.next()) {
            books.add(getBook(UUID.fromString(resultSet.getString("book_id"))));
        }

        // Close connection
        closeConnection();

        // Return book object
        return books;
    }

    public List<Book> getBookByAuthor(String bookAuthor) throws SQLException {
        // Open connection
        openConnection();

        // Get book from database
        String getBookByAuthorString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_author = ?";
        PreparedStatement getBookByAuthorFromDB = connection.prepareStatement(getBookByAuthorString);
        getBookByAuthorFromDB.setString(1, bookAuthor);
        List<Book> books = new ArrayList<>();
        ResultSet resultSet = getBookByAuthorFromDB.executeQuery();
        while (resultSet.next()) {
            books.add(getBook(UUID.fromString(resultSet.getString("book_id"))));
        }

        // Close connection
        closeConnection();

        // Return book object
        return books;
    }

    public List<Book> getBookByGenre(String bookGenre) throws SQLException {
        // Open connection
        openConnection();

        // Get book from database
        String getBookByGenreString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_category = ?";
        PreparedStatement getBookByGenreFromDB = connection.prepareStatement(getBookByGenreString);
        getBookByGenreFromDB.setString(1, bookGenre);
        List<Book> books = new ArrayList<>();
        ResultSet resultSet = getBookByGenreFromDB.executeQuery();
        while (resultSet.next()) {
            books.add(getBook(UUID.fromString(resultSet.getString("book_id"))));
        }

        // Close connection
        closeConnection();

        // Return book object
        return books;
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
            addBookToDB.setString(1, curBook.getBookId().toString());
            addBookToDB.setString(2, curBook.getBookName());
            addBookToDB.setString(3, curBook.getBookAuthor());
            addBookToDB.setString(4, curBook.getBookCategory());
            addBookToDB.executeUpdate();
        }
        closeConnection();
        return true;
    }
}
