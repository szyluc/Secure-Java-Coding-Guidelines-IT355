import java.time.LocalDate;
import java.util.UUID;

public class RentedBook {
    private UUID bookID;
    private UUID accountID;
    private LocalDate dateRented;

    public RentedBook(UUID bookID, UUID accountID, LocalDate dateRented) {
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

    public LocalDate getDateRented() {
        return dateRented;
    }
    
}
