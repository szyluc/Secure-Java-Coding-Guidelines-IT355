/**
 * Bankaccount with safe deposit and withdrawl
 * MET01-J. P9
 */
public class BankAccount {
    /** identifier for account */
    private final String accountID;
    /** bankaccounts balance  */
    private double balance;

    /**
     * Constructs bankAccount with initial balance
     * Validates input arguments
     */
    public BankAccount(String accountID, double balance) {
        // Make sure account id is not null or empty
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        // Make sure balance is not negative
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // Assign the validated values to their fields
        this.accountID = accountID;
        this.balance = balance;
    }

    /**
     * Deposits positive amount of money into account
     * @param money
     */
    public void deposit(double money) {
        // validate to make sure its positive
        if(money <= 0) {
            throw new IllegalArgumentException("Must be positive");
        }
        // update balance
        balance += money;
    }
    /**
     * 
     * @param amount
     */
    public void withdraw(double amount) {
        // Must be positive
        if(amount <= 0) {
            throw new IllegalArgumentException("Must be positive");
        }
        // Validate enough money in account
        if (amount > balance) {
            throw new IllegalArgumentException("Withdraw > amount");
        }
        balance -= amount;
    }
}
