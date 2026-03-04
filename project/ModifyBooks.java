import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

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

    private static final String BOOK_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name = ?";
    

    /**
     * Default constructor for the ModifyBooks class.
     */
    public ModifyBooks() {
        super("book");
    }

    public void createBookTable() throws Exception {
        openConnection();
        PreparedStatement ifBookDBExists = connection.prepareStatement(BOOK_EXISTS);
        ifBookDBExists.setString(1, BOOK_DB_NAME);
        ResultSet resultSet = ifBookDBExists.executeQuery();
        if (!resultSet.next()) {
            // table does not exist yet, we should create it
            Statement createBookDB = connection.createStatement();
            createBookDB.executeUpdate(BOOK_DB);
            createBookDB.close();
        }
        closeConnection();
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

        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        if (modifyRentedBooks.isRented(bookID)) {
            System.out.println("All rentals must be returned before a book can be deleted.");
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
    public void updateBook(Book book) {
        String updateBookString = "UPDATE " + BOOK_DB_NAME + " SET book_name = ?, book_author = ?, book_category = ? WHERE book_id = ?;";

        try (PreparedStatement updateBook = getConnection().prepareStatement(updateBookString)) {

            updateBook.setString(1, book.getBookName());
            updateBook.setString(2, book.getBookAuthor());
            updateBook.setString(3, book.getBookCategory());
            updateBook.setString(4, book.getBookId().toString());

            // Check if the book got updated
            int rows = updateBook.executeUpdate();

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
        String getBookString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_id = ?";
        PreparedStatement getBookFromDB = connection.prepareStatement(getBookString);
        getBookFromDB.setString(1, bookID.toString());
        ResultSet resultSet = getBookFromDB.executeQuery();
        Book curBook = null;
        if (resultSet.next()) {
            UUID bookUUID = UUID.fromString(resultSet.getString(1));
            curBook = new Book(bookUUID, resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
        }

        // Close connection
        closeConnection();

        // Return book object
        return curBook;
    }

    public void importBooks(File xmlFile) throws Exception {
        openConnection();
        PreparedStatement ifBookDBExists = connection.prepareStatement(BOOK_EXISTS);
        ifBookDBExists.setString(1, BOOK_DB_NAME);
        ResultSet resultSet = ifBookDBExists.executeQuery();
        closeConnection();
        if (!resultSet.next()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);


            ArrayList<Book> booksToAdd = new ArrayList<>();
            NodeList booksNodeList = document.getElementsByTagName(BOOK_DB_NAME);
            for (int i=0; i<booksNodeList.getLength(); i++) {
                // NodeList bookInfo = document.getElementsByTagName(BOOK_DB_NAME);
                Node curBookNode = booksNodeList.item(i);
                if (curBookNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) curBookNode;
                    Book curBook = new Book(
                        getTagValue("title", bookElement),
                        getTagValue("author", bookElement),
                        getTagValue("category", bookElement)
                    );
                    booksToAdd.add(curBook);
                }
                // then, we should add books in bulk to avoid multiple opens and closes
            }
            addBooks(booksToAdd);
        }
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
            Book curBook = new Book(
                UUID.fromString(resultSet.getString(1)),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
            );
            books.add(curBook);
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
            Book curBook = new Book(
                UUID.fromString(resultSet.getString(1)),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
            );
            books.add(curBook);
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
            Book curBook = new Book(
                UUID.fromString(resultSet.getString(1)),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
            );
            books.add(curBook);
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
