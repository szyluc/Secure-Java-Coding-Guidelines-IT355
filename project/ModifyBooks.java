import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.Element;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    private static final Logger logger = Logger.getLogger("ModifyBooksLogger");
    FileHandler fh;

    /**
     * A method to initialize the log file
     */
    public void init() {
        try {
            fh = new FileHandler("./logs/ModifyBooks.log");
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
     * Default constructor for the ModifyBooks class.
     */
    public ModifyBooks() {
        super("book");
    }

    /**
     * A method that creates the book database where all of the rentable books are initially stored
     * 
     * @throws Exception if an error occurs during execution
     */
    public void createBookTable() throws Exception {
        openConnection();
        PreparedStatement ifBookDBExists = connection.prepareStatement(BOOK_EXISTS);
        Statement createBookDB = connection.createStatement();

        try {
            ifBookDBExists.setString(1, BOOK_DB_NAME);
            ResultSet resultSet = ifBookDBExists.executeQuery();
            if (!resultSet.next()) {
                // table does not exist yet, we should create it
                createBookDB.executeUpdate(BOOK_DB);
            }
        } finally {
            try {
                ifBookDBExists.close();
                createBookDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for creating book table.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * Allows admin to add a Book to the Book database.
     * @param book The book object to be added.
     * @return True: book add was successful. False: book add failed.
     * @throws SQLException If a SQL exception takes place.
     * @return whether the book add succeeded
     */
    public boolean addBook(Book book) throws SQLException {
        if (book == null) {
            return false; // book add failed
        }

        openConnection();
        PreparedStatement addBookToDB = connection.prepareStatement(BOOK_INSERT);

        try {
            // Then, add a book to the database
            addBookToDB.setString(1, book.getBookId().toString());
            addBookToDB.setString(2, book.getBookName());
            addBookToDB.setString(3, book.getBookAuthor());
            addBookToDB.setString(4, book.getBookCategory());
            addBookToDB.executeUpdate();
            return true;
        } finally {
            try {
                addBookToDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for adding book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that removes a book from the book database
     * 
     * @param bookID is the unique bookID that we want to remove from the database
     * @throws SQLException if a SQL error occurs during execution, is also thrown if an error while closing the connection occurs
     * @return whether or not the removal is successful
     */
    public boolean removeBook(UUID bookID) throws SQLException {
        if (bookID == null) {
            return false;
        }
        if (getRowCount("book_id", bookID.toString()) == 0) {
            System.out.println("This book ID does not exist.");
            return false; // book does not exist
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

        try {
            removeBookFromDB.setString(1, bookID.toString());
            removeBookFromDB.executeUpdate();
            return true;
        } finally {
            try {
                removeBookFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for removing book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that updates book information in the database, checks if the book got updated, if not throw an error
     * 
     * @param book is a book object that we want to modify and update the contents of
     * @throws SQLException if a SQL error occurs during execution, is also thrown if an error while closing the connection occurs
     */
    public void updateBook(Book book) throws SQLException {
        openConnection();

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        String updateBookString = "UPDATE " + BOOK_DB_NAME
                + " SET book_name = ?, book_author = ?, book_category = ? WHERE book_id = ?;";

        PreparedStatement updateBook = getConnection().prepareStatement(updateBookString);

        try {
            updateBook.setString(1, book.getBookName());
            updateBook.setString(2, book.getBookAuthor());
            updateBook.setString(3, book.getBookCategory());
            updateBook.setString(4, book.getBookId().toString());

            // Check if the book got updated
            int rows = updateBook.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Updated Sucessfully");
            } else {
                System.out.println("Book not found.");
            }
        } finally {
            try {
                updateBook.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for updating book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that returns a book from the book database if it exists
     * 
     * @param bookID is the book that we want to see if it exists
     * @throws SQLException if a SQL error occurs during execution, also gets thrown if while closing connection there is an error
     * @return the bookID if it exists in the database
     */
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

        try {
            getBookFromDB.setString(1, bookID.toString());
            ResultSet resultSet = getBookFromDB.executeQuery();
            Book curBook = null;
            if (resultSet.next()) {
                UUID bookUUID = UUID.fromString(resultSet.getString(1));
                curBook = new Book(bookUUID, resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            }
            return curBook;
        } finally {
            try {
                getBookFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for getting book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method to handle importing the books from an XML file and put them into the book database
     * 
     * @param xmlFile is the file that contains the initial library of books for the database
     * @throws Exception if an error occurs while executing
     * @throws IllegalAccessException if the xmlFile provided is not found in the project files
     * @throws IllegalArgumentException if the provided file is not a file
     * @throws SAXException if an error occurs while parsing the xml file
     * @throws IOException if the provided xml file cannot be parsed or validated
     * @throws SQLException if while closing the connection, an error occurs
     */
    public void importBooks(File xmlFile) throws Exception {
        if (xmlFile == null || !xmlFile.exists()) {
            throw new IllegalAccessException("Xml file not found");
        } else if (!xmlFile.isFile()) {
            throw new IllegalArgumentException("Invalid XML File");
        }
        openConnection();
        PreparedStatement ifBookDBExists = connection.prepareStatement(BOOK_EXISTS);
        try {
            ifBookDBExists.setString(1, BOOK_DB_NAME);
            ResultSet resultSet = ifBookDBExists.executeQuery();
            if (resultSet.next()) {
                InputSource xmlStream = new InputSource(new File("books.xml").getAbsolutePath());
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                DefaultHandler defaultHandler = new DefaultHandler() {
                    public void warning(SAXParseException e) throws SAXException {
                        throw e;
                    }

                    public void error(SAXParseException e) throws SAXException {
                        throw e;
                    }

                    public void fatalError(SAXParseException e) throws SAXException {
                        throw e;
                    }
                };
                StreamSource streamSource = new StreamSource(new File("books.xsd"));
                try {
                    Schema schema = schemaFactory.newSchema(streamSource);
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    saxParserFactory.setSchema(schema);
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    XMLReader xmlReader = saxParser.getXMLReader();
                    xmlReader.setEntityResolver(new CustomResolver());
                    saxParser.parse(xmlStream, defaultHandler);
                } catch (ParserConfigurationException e) {
                    logger.log(Level.WARNING, "Exception caught while validating Book XML.", e);
                    throw new IOException("Unable to validate XML", e);
                } catch (SAXException e) {
                    logger.log(Level.WARNING, "Exception caught while importing Book XML.", e);
                    throw new IOException("Invalid XML", e);
                }
                // SER08-J: Rather than using deprecated AccessControlContext to restrict
                // privileges during parsing, we can configure the XML parser itself to prevent it
                // from accessing external resources or executing malicious content.
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                // Prevent external DTDs which prevents XXE attacks
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                // Prevent external general entities
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                // Prevent external parameter entities
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                // Prevent external DTD loading
                factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                factory.setXIncludeAware(false);
                factory.setExpandEntityReferences(false);

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);

                ArrayList<Book> booksToAdd = new ArrayList<>();
                NodeList booksNodeList = document.getElementsByTagName(BOOK_DB_NAME);
                for (int i = 0; i < booksNodeList.getLength(); i++) {
                    // NodeList bookInfo = document.getElementsByTagName(BOOK_DB_NAME);
                    Node curBookNode = booksNodeList.item(i);
                    if (curBookNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element bookElement = (Element) curBookNode;
                        Book curBook = new Book(
                                getTagValue("title", bookElement),
                                getTagValue("author", bookElement),
                                getTagValue("category", bookElement));
                        booksToAdd.add(curBook);
                    }

                }
                addBooks(booksToAdd);
            }
        } finally {
            try {
                ifBookDBExists.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for Book XML.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that returns a list of books with a specific book name
     * 
     * @param bookName is the name of the book that the user wants to search for
     * @throws SQLException if a SQL error occurs during execution, also is thrown if an error occurs while closing connection
     * @throws IllegalArgumentException if the provided bookName is empty or null
     * @return the list of books that match the provided bookName String value
     */
    public List<Book> getBookByName(String bookName) throws SQLException {
        if (bookName == null || bookName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Open connection
        openConnection();

        // Get book from database
        String getBookByNameString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_name = ?";
        PreparedStatement getBookByNameFromDB = connection.prepareStatement(getBookByNameString);

        try {
            getBookByNameFromDB.setString(1, bookName);
            List<Book> books = new ArrayList<>();
            ResultSet resultSet = getBookByNameFromDB.executeQuery();
            while (resultSet.next()) {
                Book curBook = new Book(
                        UUID.fromString(resultSet.getString(1)),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                books.add(curBook);
            }

            // Return book object
            return books;
        } finally {
            try {
                getBookByNameFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caugh for getting book by title.", e);
                System.out.println("SQLException caught.");
            }
        }
    }
    
    /**
     * A method that returns a list of books with a specific author
     * 
     * @param bookAuthor is the name of the author that the user wants to search for
     * @throws SQLException if a SQL error occurs during execution, also is thrown if an error occurs while closing connection
     * @throws IllegalArgumentException if the provided bookAuthor is empty or null
     * @return the list of books that match the provided bookAuthor String value
     */
    public List<Book> getBookByAuthor(String bookAuthor) throws SQLException {

        if (bookAuthor == null || bookAuthor.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Open connection
        openConnection();

        // Get book from database
        String getBookByAuthorString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_author = ?";
        PreparedStatement getBookByAuthorFromDB = connection.prepareStatement(getBookByAuthorString);

        try {
            getBookByAuthorFromDB.setString(1, bookAuthor);
            List<Book> books = new ArrayList<>();
            ResultSet resultSet = getBookByAuthorFromDB.executeQuery();

            while (resultSet.next()) {
                Book curBook = new Book(
                        UUID.fromString(resultSet.getString(1)),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                books.add(curBook);
            }

            // Return book object
            return books;
        } finally {
            try {
                getBookByAuthorFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for getting book by author.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that returns a list of books with a specific genre
     * 
     * @param bookGenre is the name of the genre that the user wants to search for
     * @throws SQLException if a SQL error occurs during execution, also is thrown if an error occurs while closing connection
     * @throws IllegalArgumentException if the provided genre is empty or null
     * @return the list of books that match the provided bookGenre String value
     */
    public List<Book> getBookByGenre(String bookGenre) throws SQLException {

        if (bookGenre == null || bookGenre.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // Open connection
        openConnection();

        // Get book from database
        String getBookByGenreString = "SELECT * FROM " + BOOK_DB_NAME + " WHERE book_category = ?";
        PreparedStatement getBookByGenreFromDB = connection.prepareStatement(getBookByGenreString);

        try {
            getBookByGenreFromDB.setString(1, bookGenre);
            List<Book> books = new ArrayList<>();
            ResultSet resultSet = getBookByGenreFromDB.executeQuery();

            while (resultSet.next()) {
                Book curBook = new Book(
                        UUID.fromString(resultSet.getString(1)),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                books.add(curBook);
            }

            // Return book object
            return books;
        } finally {
            try {
                getBookByGenreFromDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caught for getting book by genre.", e);
                System.out.println("SQLException caught.");
            }
        }
    }

    /**
     * A method that adds books to the book database
     * 
     * @param books is an ArrayList of book objects that will be added to the book database
     * @throws SQLException if a SQL error occurs during execution, also is thrown if an error occurs while closing connection
     * @return whether adding the books was successful
     */
    private boolean addBooks(ArrayList<Book> books) throws SQLException {
        if (books == null || books.size() == 0) {
            return false; // cannot add zero books
        }
        openConnection();
        // create book DB if necessary
        Statement createBookDB = connection.createStatement();

        try {
            createBookDB.executeUpdate(BOOK_DB);

            for (Book curBook : books) {
                PreparedStatement addBookToDB = connection.prepareStatement(BOOK_INSERT);
                addBookToDB.setString(1, curBook.getBookId().toString());
                addBookToDB.setString(2, curBook.getBookName());
                addBookToDB.setString(3, curBook.getBookAuthor());
                addBookToDB.setString(4, curBook.getBookCategory());
                addBookToDB.executeUpdate();
            }
            return true;
        } finally {
            try {
                createBookDB.close();
                closeConnection();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Exception caugh for adding book.", e);
                System.out.println("SQLException caught.");
            }
        }
    }
}
 
