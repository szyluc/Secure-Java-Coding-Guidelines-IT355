import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;

// MET12-J: Implements Closeable so the caller can explicitly release
// resources with close() rather than a finalizer.
public class ModifyRentedBooks extends DatabaseController implements Closeable {
    private static final String RENTED_BOOKS_DB = """
        CREATE TABLE IF NOT EXISTS rented_books (
            b_id TEXT NOT NULL,
            a_id TEXT NOT NULL,
            checkout_date DATE NOT NULL,
            PRIMARY KEY (b_id, a_id),
            FOREIGN KEY (b_id) REFERENCES book(book_id),
            FOREIGN KEY (a_id) REFERENCES account(account_id)
        );
        """;

    private static final String RENTED_BOOKS_DB_NAME = "rented_books";
    private boolean closed = false;

    public ModifyRentedBooks() {
        super(RENTED_BOOKS_DB_NAME);
    }

    public void createRentedBooksTable() throws Exception {
        openConnection();
        Statement createRentedBookDB = connection.createStatement();

        try {
            createRentedBookDB.executeUpdate(RENTED_BOOKS_DB);
        } finally {
            try {
                createRentedBookDB.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("SQLException caught.");
            }
        }
    }

    public LocalDateTime rentBook(Account account, Book book) throws Exception {
       if(account == null) {
        throw new IllegalArgumentException();
       }
       if(book == null) {
        throw new IllegalArgumentException();
       }

       // Open connection
        openConnection();

        // Add rented book to database
        LocalDateTime nowDate = LocalDateTime.now(); // time to be passed to receipt
        String addRentedBookString = "INSERT INTO " + RENTED_BOOKS_DB_NAME + " (b_id, a_id, checkout_date) VALUES (?, ?, ?);";
        PreparedStatement addRentedBookToDB = connection.prepareStatement(addRentedBookString);

        try {
            addRentedBookToDB.setString(1, book.getBookId().toString());
            addRentedBookToDB.setString(2, account.getAccountId().toString());
            addRentedBookToDB.setString(3, nowDate.toString());
            addRentedBookToDB.executeUpdate();

            // Print receipt
            makeReceipt(book, nowDate);

            // Return boolean indicating success
            return nowDate;
        } finally {
            try {
                addRentedBookToDB.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("SQLException caught.");
            }
        }
    }

    public boolean returnBook(UUID accountID, UUID bookID) throws SQLException {
        if(accountID == null) {
            throw new IllegalArgumentException();
        }

        if(bookID == null) {
            throw new IllegalArgumentException();
        }


        
        // Open connection
        openConnection();

        // Remove rented book from database
        String removeAccountString = "DELETE FROM " + RENTED_BOOKS_DB_NAME + " WHERE b_id = ? AND a_id = ?";
        PreparedStatement removeRentedBookFromDB = connection.prepareStatement(removeAccountString);

        try {
            removeRentedBookFromDB.setString(1, bookID.toString());
            removeRentedBookFromDB.setString(2, accountID.toString());
            removeRentedBookFromDB.executeUpdate();

            // Return boolean indicating success
            return true;
        } finally {
            try {
                removeRentedBookFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("SQLException caught.");
            }
        }
    }

    public RentedBook getRentedBook(UUID accountID, UUID bookID) throws SQLException {
        
        if(accountID == null) {
            throw new IllegalArgumentException();
        }

        if(bookID == null) {
            throw new IllegalArgumentException();
        }
        // Open connection
        openConnection();

        // Get rented book from database
        String getRentedBookString = "SELECT * FROM " + RENTED_BOOKS_DB_NAME + " WHERE b_id = ? AND a_id = ?";
        PreparedStatement getRentedBookFromDB = connection.prepareStatement(getRentedBookString);

        try {
            getRentedBookFromDB.setString(1, bookID.toString());
            getRentedBookFromDB.setString(2, accountID.toString());
            ResultSet resultSet = getRentedBookFromDB.executeQuery();
            RentedBook curRentedBook = null;
            if (resultSet.next()) {
                UUID bookUUID = UUID.fromString(resultSet.getString(1));
                UUID accountUUID = UUID.fromString(resultSet.getString(2));
                LocalDateTime rentDate = LocalDateTime.parse(resultSet.getString(3));
                curRentedBook = new RentedBook(bookUUID, accountUUID, rentDate);
            }

            // Return rented book object
            return curRentedBook;
        } finally {
            try {
                getRentedBookFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("SQLException caught.");
            }
        }
    }

    public boolean isRented(UUID bookID) throws SQLException {

        if(bookID == null) {
            throw new IllegalArgumentException();
        }
        // Open connection
        openConnection();

        // Get rented books from database
        String getRentedBookString = "SELECT * FROM " + RENTED_BOOKS_DB_NAME + " WHERE b_id = ?";
        PreparedStatement getRentedBookFromDB = connection.prepareStatement(getRentedBookString);

        try {
            getRentedBookFromDB.setString(1, bookID.toString());
            ResultSet resultSet = getRentedBookFromDB.executeQuery();

            // Check if book is rented
            boolean isRented = false;
            if (resultSet.next()) {
                isRented = true;
            }

            // Return boolean
            return isRented;
        } finally {
            try {
                getRentedBookFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("SQLException caught.");
            }
        }
    }

    public List<RentedBook> getRentedBooks(UUID accountID) throws SQLException {
       
        if(accountID == null) {
            throw new IllegalArgumentException();
        }
        // Open connection
        openConnection();

        // Get rented book from database
        String getRentedBookString = "SELECT * FROM " + RENTED_BOOKS_DB_NAME + " WHERE a_id = ?";
        PreparedStatement getRentedBookFromDB = connection.prepareStatement(getRentedBookString);

        try {
            getRentedBookFromDB.setString(1, accountID.toString());
            List<RentedBook> rentedBooks = new ArrayList<>();
            ResultSet resultSet = getRentedBookFromDB.executeQuery();
            while (resultSet.next()) {
                UUID bookUUID = UUID.fromString(resultSet.getString(1));
                UUID accountUUID = UUID.fromString(resultSet.getString(2));
                LocalDateTime rentDate = LocalDateTime.parse(resultSet.getString(3));
                RentedBook curRentedBook = new RentedBook(bookUUID, accountUUID, rentDate);
                rentedBooks.add(curRentedBook);
            }

            // Return rented book object
            return rentedBooks;
        } finally {
            try {
                getRentedBookFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("SQLException caught.");
            }
        }
    }

    private void makeReceipt(Book curBook, LocalDateTime nowDate) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("books");
        document.appendChild(root);

        Element book = document.createElement("book");

        Element title = document.createElement("title");
        Element author = document.createElement("author");
        Element category = document.createElement("category");
        Element date = document.createElement("date");

        title.appendChild(document.createTextNode(curBook.getBookName()));
        author.appendChild(document.createTextNode(curBook.getBookAuthor()));
        category.appendChild(document.createTextNode(curBook.getBookCategory()));
        date.appendChild(document.createTextNode(nowDate.toString()));

        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(category);
        book.appendChild(date);

        root.appendChild(book);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        // first, we should ensure the receipts directory exists. if not, we need to make it
        File receiptsDir = new File("receipts");

        if (!receiptsDir.exists()) {
            receiptsDir.mkdirs();
        }

        StreamResult result = new StreamResult("receipts/" + nowDate.toString() + ".xml");
        transformer.transform(source, result);
    }

    // MET12-J: Explicit cleanup, release any held resources
    @Override
    public void close() {
        if (!closed) {
            closed = true;
            try {
                closeConnection();
            } catch (Exception e) {
                System.out.println("Exception caught.");
            }
        }
    }
    
}
