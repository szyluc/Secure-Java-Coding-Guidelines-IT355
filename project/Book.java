import java.util.UUID;

public class Book {
    private final UUID bookID;
    private String bookName;
    private String bookAuthor;
    private String bookCategory;

    public Book(String bookName, String bookAuthor, String bookCategory) {
        this.bookID = UUID.randomUUID(); // assigns an identifier to the book
        setBookDetails(bookName, bookAuthor, bookCategory);
    }

    public Book(UUID bookID, String bookName, String bookAuthor, String bookCategory) {
        this.bookID = bookID;
        setBookDetails(bookName, bookAuthor, bookCategory);
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public UUID getBookId() {
        return bookID;
    }

    public String toString() {
        return bookID + " | " + bookName + ", by " + bookAuthor + ". Category: " + bookCategory + ".";
    }

    private void setBookDetails(String bookName, String bookAuthor, String bookCategory) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
    }

}