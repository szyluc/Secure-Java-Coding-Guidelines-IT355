import java.io.File;
import java.io.IOException;
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
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ModifyRentedBooks extends DatabaseController {
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
    /**
     * A final String variable that contains the name of the rented_books DB
     */
    private static final String RENTED_BOOKS_DB_NAME = "rented_books";

    private static final Logger logger = Logger.getLogger("ModifyRentedBooksLogger");
    FileHandler fh;

    /**
     * A method to initialize the log file
     */
    public void init() {
        try {
            fh = new FileHandler("./logs/ModifyRentedBooks.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {
            System.out.println("SecurityException caught");
        } catch (IOException e) {
            System.out.println("IOException caught");
        }
    }

    /**
     * Calls the constructor of the DatabaseController with the rented_books database name
     */
    public ModifyRentedBooks() {
        super(RENTED_BOOKS_DB_NAME);
    }

    /**
     * Creates a new rented_books database when called
     * @throws Exception if an error occurs during runtime of the method
     * @throws SQLException if an error occurs while closing the SQL database connection
     */
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
                logger.log(Level.WARNING, "Exception caught for creating RentedBooks table.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * Tracks the time that the book was rented out while adding it to the rented_books database.
     * Also adds the book to the rented_book database so that it cannot be checked out by another customer.
     * 
     * @param account is the account that is checking out the book
     * @param book is the book so that we can retrieve the data from the SQL database
     * @throws Exception by default if an error occurs during runtime that isn't already caught
     * @throws IllegalArgumentException if any of the parameters are null
     * @throws SQLException if an error occurs while closing the connection to the database
     * @return the LocalDateTime object when the book is rented out
     */
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
                // MET12-J: Do not use finalizers
                // We are inherintly following MET12-J by forceably closing connections
                // while they are no longer needed
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for renting a book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method to handle book returns by updating databases associated with the books. Removes the book from the rented_books database
     * 
     * @param accountID is the unique accountID that is returning the book, so we can remove it from their rented_books database
     * @param bookID is the unique bookID for a book so that we know what book the user is returning
     * @throws SQLException if a SQL error occurs during execution, also happens when trying to close the connection
     * @throws IllegalArgumentException if the accountID or bookID provided is null
     * @return if the book return was successful  
     */
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
                logger.log(Level.WARNING, "Exception caught for returning book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method to return a RentedBook object from the rented_books database
     * 
     * @param accountID is the unique accountID of the account that is accessing their rented_books database
     * @param bookID is the unique bookID of the book we want to return from the rented_books database
     * @throws SQLException if a SQL error occurs during execution, also gets thrown if error occurs while closing the connection
     * @throws IllegalArgumentException if either the accountID or bookID provided are null
     * @return the rented out book object
     */
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
                logger.log(Level.WARNING, "Exception caught for getting rented book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method to return if a specific bookID is rented out
     * 
     * @param bookID is the unique bookID of a book so we can find out if the books is in the rented_books database
     * @throws SQLException if a SQL error occurs during execution, also gets thrown if an error occurs while closing the connection
     * @throws IllegalArgumentException if the provided bookID parameter is equal to null
     * @return whether the provided book is rented out or not
     */
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
                logger.log(Level.WARNING, "Exception caught for checking if book is rented.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method to return the full list of rented out books for a user
     * 
     * @param accountID is the unique accountID for a user, so that we can find which books the user rented out
     * @throws SQLException if a SQL error occurs during execution, also gets thrown if an error occurs while closing the connection
     * @throws IllegalArgumentException if the parameter accountID is equal to null
     * @return a list of all rented books under the accountID provided
     */
    public List<RentedBook> getRentedBooks(UUID accountID) throws SQLException {

        if (accountID == null) {
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
                logger.log(Level.WARNING, "Exception caught for fetching rented book list.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that handles creating the receipt after a book is rented out by a User
     * @param curBook is the book object that the user is choosing to rent out
     * @param nowDate is the current local date so that we can timestamp when this transaction occured
     * @throws Exception if an error occurs during execution
     */
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

        StreamResult result = new StreamResult("receipts/" + nowDate.toString().replace(":", "-").replace(".", "-") + ".xml");
        transformer.transform(source, result);
    }
    
}
