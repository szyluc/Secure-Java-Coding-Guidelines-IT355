import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represent a record of a rented book within the library system
 * The class stored information on what book was rented, the account that rented the book
 * and the timestamp of when it was rented
 */
public class RentedBook {
    /** Unique identifier of the rented book */
    private final UUID bookID;
    /** Unique identifier of an account */
    private final UUID accountID;
    /** The timestamp when the book was rented */
    private LocalDateTime dateRented;

    /**
     * Constructs a RentedBook record
     * @param bookID the unique ID of the rented book
     * @param accountID the unique ID of the account thats renting book
     * @param dateRented the timestamp when the book was rented
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public RentedBook(UUID bookID, UUID accountID, LocalDateTime dateRented) {
        
        if(accountID == null) {
            throw new IllegalArgumentException();
        }

        if(bookID == null) {
            throw new IllegalArgumentException();
        }
        if (dateRented == null || dateRented.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Account holder birth date cannot be null or in the future");
        }
        
        this.bookID = bookID;
        this.accountID = accountID;
        this.dateRented = dateRented;
    }

    /**
     * Returns the rented book id
     * @return rented book id
     */
    public UUID getBookID() {
        return bookID;
    }

    /**
     * Returns the account id that rented the book
     * @return account ID that rented the book
     */
    public UUID getAccountID() {
        return accountID;
    }
    /**
     * The timestamp of when the book was rented
     * @return timestamp of rented book
     */
    public LocalDateTime getDateRented() {
        return dateRented;
    }
    
}
