import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;

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
        super("book");
    }
    

    /**
     * Allows admin to add a Book based on Book object
     * Gets added to the database
     * @param book
     */
    public boolean addBook(Book book) {

        if (book == null) {
            return false;
        }
        String addBookSQL = """
            INSERT INTO book (book_id, book_name, book_author, book_category)
            VALUES (?, ?, ?, ?);
        """;

        // Think I haft to fix this try statement
        try(PreparedStatement addBook = getConnection().prepareStatement(addBookSQL)) {
            addBook.setString(1, book.getBookId());
            addBook.setString(2, book.getBookName());
            addBook.setString(3, book.getBookAuthor());
            addBook.setString(4, book.getBookCategory());

            addBook.executeUpdate();
            System.out.println("Book added Successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding book.");
            e.printStackTrace();
            return false;
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
    public void updateBook(Book book) {
        String updateBookSQL = """
            UPDATE book
            SET book_name = ?, book_author = ?, book_category = ?
            WHERE book_id = ?;
        """;

        try (PreparedStatement updateBook = getConnection().prepareStatement(updateBookSQL)) {

            updateBook.setString(1, book.getBookName());
            updateBook.setString(2, book.getBookAuthor());
            updateBook.setString(3, book.getBookCategory());
            updateBook.setString(4, book.getBookId());

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

    /**
     * Deletes  a book from the database
     * Checks to see if book got deleted
     * Otherwise throw error 
     * @param book book object
     */
    public void deleteBook(Book book) {
        String deleteBookSQL = "DELETE FROM book WHERE book_id = ?;";

        try(PreparedStatement deleteBook = getConnection().prepareStatement(deleteBookSQL)) {
            deleteBook.setString(1, book.getBookId());

            // Check if book row got deleted
            int rows = deleteBook.executeUpdate();

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

    public List<Book> getBookByName(String bookName) throws SQLException {
        // Open connection
        openConnection();

        // Get book from database
        String getBookByNameString = "SELECT * FROM ? WHERE book_name = ?";
        PreparedStatement getBookByNameFromDB = connection.prepareStatement(getBookByNameString);
        getBookByNameFromDB.setString(1, BOOK_DB_NAME);
        getBookByNameFromDB.setString(2, bookName);
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
        String getBookByAuthorString = "SELECT * FROM ? WHERE book_author = ?";
        PreparedStatement getBookByAuthorFromDB = connection.prepareStatement(getBookByAuthorString);
        getBookByAuthorFromDB.setString(1, BOOK_DB_NAME);
        getBookByAuthorFromDB.setString(2, bookAuthor);
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
        String getBookByGenreString = "SELECT * FROM ? WHERE book_category = ?";
        PreparedStatement getBookByGenreFromDB = connection.prepareStatement(getBookByGenreString);
        getBookByGenreFromDB.setString(1, BOOK_DB_NAME);
        getBookByGenreFromDB.setString(2, bookGenre);
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
}
