import java.util.UUID;

public final class Book {
    private final UUID bookID;
    private String bookName;
    private String bookAuthor;
    private String bookCategory;

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

    public String transactionString() {
        return bookName + ", by " + bookAuthor + ".";
    }

    private void setBookDetails(String bookName, String bookAuthor, String bookCategory) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
    }

}