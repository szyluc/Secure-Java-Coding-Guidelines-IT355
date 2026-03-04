import java.util.UUID;

/**
 * Reprsents a book entity in the library system holding information on
 * the books ID and metadaate
 * The class is used to manage book records in the system which allows books to be created
 * as new entrie or import existing record using the unique ID
 */
public final class Book {
    /** Unique identifier for book */
    private final UUID bookID;
    /** Book name */
    private String bookName;
    /** Author of the book */
    private String bookAuthor;
    /** Category of the book */
    private String bookCategory;

    /**
     * Constructs a new book with unique ID
     * @param bookName the name of the book
     * @param bookAuthor the author of the book
     * @param bookCategory the category or genre of the book
     * @throws IllegalArgumentException is any parameter is null or empty
     */
    public Book(String bookName, String bookAuthor, String bookCategory) {
        if(bookName == null || bookName.isEmpty()) {
            throw new IllegalArgumentException();
        }
         if(bookAuthor == null || bookAuthor.isEmpty()) {
            throw new IllegalArgumentException();
        }
         if(bookCategory == null || bookCategory.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.bookID = UUID.randomUUID(); // assigns an identifier to the book
        setBookDetails(bookName, bookAuthor, bookCategory);
    }
     /**
     * Constructs a Book with a specific ID
     * Used when loading book data from xml file
     * @param bookID the unique ID of the book
     * @param bookName the name of the book
     * @param bookAuthor the author of the book
     * @param bookCategory the category or genre of the book
     * @throws IllegalArgumentException is any parameter is null or empty
     */
    public Book(UUID bookID, String bookName, String bookAuthor, String bookCategory) {
        if(bookName == null || bookName.isEmpty()) {
            throw new IllegalArgumentException();
        }
         if(bookAuthor == null || bookAuthor.isEmpty()) {
            throw new IllegalArgumentException();
        }
         if(bookCategory == null || bookCategory.isEmpty()) {
            throw new IllegalArgumentException();
        }
         if(bookID == null) {
            throw new IllegalArgumentException();
        }
        this.bookID = bookID;
        setBookDetails(bookName, bookAuthor, bookCategory);
    }

    /**
     * Return the name of the book
     * @return the book name
     */
    public String getBookName() {
        return bookName;
    }
    /**
     * Return the author of the book
     * @return the author of book
     */
    public String getBookAuthor() {
        return bookAuthor;
    }
    /**
     * Return the category of book
     * @return the book category
     */
    public String getBookCategory() {
        return bookCategory;
    }
    /**
     * Return the unique identifier of book
     * @return the book ID
     */
    public UUID getBookId() {
        return bookID;
    }

    /**
     * Print out the book information
     * @return bookID, bookName, bookAuthor and bookCategory
     */
    public String toString() {
        return bookID + " | " + bookName + ", by " + bookAuthor + ". Category: " + bookCategory + ".";
    }

    /**
     * Prints out the book name and its author
     * @return bookName and bookAuthor
     */
    public String transactionString() {
        return bookName + ", by " + bookAuthor + ".";
    }

    /**
     * Private method to set parameters to the book object
     * @param bookName the book name
     * @param bookAuthor the book author
     * @param bookCategory the book category
     */
    private void setBookDetails(String bookName, String bookAuthor, String bookCategory) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
    }

}