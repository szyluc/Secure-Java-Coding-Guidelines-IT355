public class RentedBook {
    private String bookID;
    private String accountID;
    private String dateRented;

    public RentedBook(String bookID, String accountID, String dateRented) {
        this.bookID = bookID;
        this.accountID = accountID;
        this.dateRented = dateRented;
    }

    public String getBookID() {
        return bookID;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getDateRented() {
        return dateRented;
    }
    
}
