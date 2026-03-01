import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.UUID;

public class ModifyAccounts extends DatabaseController {
    private final String ACCOUNT_DB = """
        CREATE TABLE IF NOT EXIST account (
            account_id TEXT PRIMARY KEY,
            account_name TEXT NOT NULL,
            account_dob DATE NOT NULL,
            account_role TEXT NOT NULL
        );
        """;
    private static final String ACCOUNT_DB_NAME = "account";

    public ModifyAccounts() {
        super(ACCOUNT_DB_NAME);
    }

    public boolean addAccount(Account account) throws SQLException {
        if (account == null) {
            return false; // account add failed
        }
        openConnection();
        // Create table if necessary.
        Statement createAccountDB = connection.createStatement();
        createAccountDB.executeUpdate(ACCOUNT_DB);
        createAccountDB.close();

        // Then, add an account to the database.
        String addAccountString = "INSERT INTO ? (account_id, account_name, account_dob, account_role) VALUES (?, ?, ?, ?);";

        // RULE: Avoid SQL injection.
        PreparedStatement addAccountToDB = connection.prepareStatement(addAccountString);
        addAccountToDB.setString(1, account.getAccountId().toString());
        addAccountToDB.setString(2, account.getAccountHolderName());
        addAccountToDB.setString(3, account.getAccountHolderBirthDate().toString());
        addAccountToDB.setString(4, account.getAccountHolderRole().toString());
        addAccountToDB.executeUpdate();

        closeConnection(); // Finally, close connection
        return true;
    }

    public boolean removeAccount(UUID accountID) throws SQLException {
        // Create table if necessary? Should not have to create DB to remove (remove 0 user)
        if (accountID == null) {
            return false; // invalid ID
        }
        openConnection();
        // the table SHOULD exist at this point. if not, an error should be thrown.
        String removeAccountString = "DELETE FROM ? WHERE accound_id = ?";
        
        PreparedStatement removeAccountFromDB = connection.prepareStatement(removeAccountString);
        removeAccountFromDB.setString(1, ACCOUNT_DB_NAME);
        removeAccountFromDB.setString(2, accountID.toString());
        removeAccountFromDB.executeUpdate();

        closeConnection();
        return true;
    }

    public Account getAccount(UUID accountID) throws SQLException {
        // Check that account exists
        if (accountID == null) {
            return null; // invalid ID
        }

        // Open connection
        openConnection();

        // Get account from database
        String getAccountString = "SELECT 1 FROM ? WHERE account_id = ?";
        PreparedStatement getAccountFromDB = connection.prepareStatement(getAccountString);
        getAccountFromDB.setString(1, ACCOUNT_DB_NAME);
        getAccountFromDB.setString(2, accountID.toString());
        Object accountObject = getAccountFromDB.executeQuery().getObject(1);
        Account account = (Account)accountObject;

        // Close connection
        closeConnection();

        // Return account object
        return account;
    }
}
