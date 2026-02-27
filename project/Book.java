import java.util.UUID;

class Book {
    private final UUID bookID;
    private String bookName;
    private String bookAuthor;
    private String bookCategory;

    public Book(String bookName, String bookAuthor, String bookCategory) {
        this.bookID = UUID.randomUUID(); // assigns an identifier to the book
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
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

    public String getBookId() {
        return bookID.toString();
    }

}