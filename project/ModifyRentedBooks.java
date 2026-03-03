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

public class ModifyRentedBooks extends DatabaseController {
    private static final String RENTED_BOOKS = """
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

    public ModifyRentedBooks() {
        super(RENTED_BOOKS_DB_NAME);
    }

    public boolean rentBook(Account account, Book book) throws Exception {
       // Open connection
        openConnection();

         // Create table if necessary
        Statement createRentedBookDB = connection.createStatement();
        createRentedBookDB.executeUpdate(RENTED_BOOKS);
        createRentedBookDB.close();

        // Add rented book to database
        LocalDate nowDate = LocalDate.now(); // time to be passed to receipt
        String addRentedBookString = "INSERT INTO " + RENTED_BOOKS_DB_NAME + " (b_id, a_id, checkout_date) VALUES (?, ?, ?);";
        PreparedStatement addRentedBookToDB = connection.prepareStatement(addRentedBookString);
        addRentedBookToDB.setString(1, book.getBookId().toString());
        addRentedBookToDB.setString(2, account.getAccountId().toString());
        addRentedBookToDB.setString(3, java.time.LocalDate.now().toString());
        addRentedBookToDB.executeUpdate();

        // Close connection
        closeConnection();

        // Print receipt
        makeReceipt(book, nowDate);

        // Return boolean indicating success
        return true;
    }

    public boolean returnBook(UUID accountID, UUID bookID) throws SQLException {
        // Open connection
        openConnection();

        // Remove rented book from database
        String removeAccountString = "DELETE FROM " + RENTED_BOOKS_DB_NAME + " WHERE b_id = ? AND a_id = ?";
        PreparedStatement removeRentedBookFromDB = connection.prepareStatement(removeAccountString);
        removeRentedBookFromDB.setString(1, bookID.toString());
        removeRentedBookFromDB.setString(2, accountID.toString());
        removeRentedBookFromDB.executeUpdate();

        // Close connection
        closeConnection();

        // Return boolean indicating success
        return true;
    }

    public RentedBook getRentedBook(UUID accountID, UUID bookID) throws SQLException {
        // Open connection
        openConnection();

        // Get rented book from database
        String getRentedBookString = "SELECT 1 FROM " + RENTED_BOOKS_DB_NAME + " WHERE b_id = ? AND a_id = ?";
        PreparedStatement getRentedBookFromDB = connection.prepareStatement(getRentedBookString);
        getRentedBookFromDB.setString(1, bookID.toString());
        getRentedBookFromDB.setString(2, accountID.toString());
        Object rentedBookObject = getRentedBookFromDB.executeQuery().getObject(1);
        RentedBook rentedBook = (RentedBook)rentedBookObject;

        // Close connection
        closeConnection();

        // Return rented book object
        return rentedBook;
    }

    public boolean isRented(UUID bookID) throws SQLException {
        // Open connection
        openConnection();

        // Get rented books from database
        String getRentedBookString = "SELECT * FROM " + RENTED_BOOKS_DB_NAME + " WHERE b_id = ?";
        PreparedStatement getRentedBookFromDB = connection.prepareStatement(getRentedBookString);
        getRentedBookFromDB.setString(1, bookID.toString());
        ResultSet resultSet = getRentedBookFromDB.executeQuery();

        // Check if book is rented
        boolean isRented = false;
        if (resultSet.next()) {
            isRented = true;
        }

        // Close connection
        closeConnection();

        // Return boolean
        return isRented;
    }

    public List<RentedBook> getRentedBooks(UUID accountID) throws SQLException {
        // Open connection
        openConnection();

        // Get rented book from database
        String getRentedBookString = "SELECT * FROM " + RENTED_BOOKS_DB_NAME + " WHERE a_id = ?";
        PreparedStatement getRentedBookFromDB = connection.prepareStatement(getRentedBookString);
        getRentedBookFromDB.setString(1, accountID.toString());
        List<RentedBook> rentedBooks = new ArrayList<>();
        ResultSet resultSet = getRentedBookFromDB.executeQuery();
        while (resultSet.next()) {
            rentedBooks.add(getRentedBook(accountID, UUID.fromString(resultSet.getString("b_id"))));
        }
        // Close connection
        closeConnection();

        // Return rented book object
        return rentedBooks;
    }

    private LocalDate makeReceipt(Book curBook, LocalDate nowDate) throws Exception {
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
        StreamResult result = new StreamResult(nowDate.toString() + ".xml");
        transformer.transform(source, result);

        return nowDate;
    }
    
}
