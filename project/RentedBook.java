import java.time.LocalDateTime;
import java.util.UUID;

public class RentedBook {
    private final UUID bookID;
    private final UUID accountID;
    private LocalDateTime dateRented;

    public RentedBook(UUID bookID, UUID accountID, LocalDateTime dateRented) {
        
        if(accountID == null) {
            throw new IllegalArgumentException();
        }

        if(bookID == null) {
            throw new IllegalArgumentException();
        }
        if (dateRented == null || dateRented.isAfter(dateRented.now())) {
            throw new IllegalArgumentException("Account holder birth date cannot be null or in the future");
        }
        
        this.bookID = bookID;
        this.accountID = accountID;
        this.dateRented = dateRented;
    }

    public UUID getBookID() {
        return bookID;
    }

    public UUID getAccountID() {
        return accountID;
    }

    public LocalDateTime getDateRented() {
        return dateRented;
    }
    
}
