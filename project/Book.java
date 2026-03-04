import java.util.UUID;
import java.nio.CharBuffer;

public class Book implements Cloneable{
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
        return bookID + ": " + bookName + " by " + bookAuthor + ". Category: " + bookCategory  + ".";
    }

    private void setBookDetails(String bookName, String bookAuthor, String bookCategory) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
    }

    // FIO05-J: Return a read-only buffers so untrusted code cannot modify
    // the backing char array through the buffer reference.
    public CharBuffer getTitleBuffer(){
        return CharBuffer.wrap(bookName.toCharArray()).asReadOnlyBuffer();
    }

    public CharBuffer getAuthorBuffer(){
        return CharBuffer.wrap(bookAuthor.toCharArray()).asReadOnlyBuffer();
    }

    // MET06-J: clone() only calls the private final copyFields() method.
    // Create a final method so clone can not be overwritten.
    @Override
    public Book clone() throws CloneNotSupportedException{
        Book cloned = (Book) super.clone();
        cloned.copyFields(this.bookName, this.bookAuthor, this.bookCategory);
        return cloned;
    }

    private final void copyFields(String name, String author, String category){
        this.bookName = name;
        this.bookAuthor = author;
        this.bookCategory = category;
    }

}