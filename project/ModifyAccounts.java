import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class ModifyAccounts extends DatabaseController {
    private final String ACCOUNT_DB = """
        CREATE TABLE IF NOT EXISTS account (
            account_id TEXT PRIMARY KEY,
            account_name TEXT NOT NULL,
            account_dob DATE NOT NULL,
            account_role TEXT NOT NULL
        );
        """;
    private static final String ACCOUNT_DB_NAME = "account";

    private static final String ACCOUNT_INSERT = "INSERT INTO " + ACCOUNT_DB_NAME 
    + "(account_id, account_name, account_dob, account_role) VALUES (?, ?, ?, ?);";


    private static final String ACCOUNT_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name = ?";

    public ModifyAccounts() {
        super(ACCOUNT_DB_NAME);
    }

    public void createAccountTable() throws Exception {
        openConnection();
        Statement createAccountDB = connection.createStatement();
        createAccountDB.executeUpdate(ACCOUNT_DB);
        createAccountDB.close();
        closeConnection();
    }

    public boolean addAccount(Account account) throws SQLException {
        if (account == null) {
            return false; // account add failed
        }
        openConnection();

        // Then, add an account to the database.
        String addAccountString = "INSERT INTO " + ACCOUNT_DB_NAME + " (account_id, account_name, account_dob, account_role) VALUES (?, ?, ?, ?);";

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

        ModifyRentedBooks modifyRentedBooks = new ModifyRentedBooks();
        List<RentedBook> rentedBooks = modifyRentedBooks.getRentedBooks(accountID);
        
        if (!rentedBooks.isEmpty()) {
            System.out.println("All rented books must be returned before an account can be deleted.");
            return false;
        }

        openConnection();
        // the table SHOULD exist at this point. if not, an error should be thrown.
        String removeAccountString = "DELETE FROM " + ACCOUNT_DB_NAME + " WHERE account_id = ?";
        
        PreparedStatement removeAccountFromDB = connection.prepareStatement(removeAccountString);
        removeAccountFromDB.setString(1, accountID.toString());
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
        String getAccountString = "SELECT account_id, account_name, account_dob, account_role FROM " + ACCOUNT_DB_NAME + " WHERE account_id = ?";
        PreparedStatement getAccountFromDB = connection.prepareStatement(getAccountString);
        getAccountFromDB.setString(1, accountID.toString());

        ResultSet rs = getAccountFromDB.executeQuery();
        Account account = null;
        // if we found an account
        if (rs.next()) {
            account = new Account(
                UUID.fromString(rs.getString("account_id")),
                rs.getString("account_name"),
                LocalDate.parse(rs.getString("account_dob")),
                Role.valueOf(rs.getString("account_role"))
            );
        }
        // Close connection
        closeConnection();

        // Return account object
        return account;
    }

    public void importAdmins(File xmlFile) throws Exception {
        openConnection();
        PreparedStatement ifAccountDBExists = connection.prepareStatement(ACCOUNT_EXISTS);
        ifAccountDBExists.setString(1, ACCOUNT_DB_NAME);
        ResultSet resultSet = ifAccountDBExists.executeQuery();
        closeConnection();

        if (!resultSet.next()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            List<Account> accountsToAdd = new ArrayList<>();
            NodeList accountsNodeList = document.getElementsByTagName("admin");
            for (int i=0; i<accountsNodeList.getLength(); i++) {
                Node curAccountNode = accountsNodeList.item(i);
                if (curAccountNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element accountElement = (Element) curAccountNode;
                    Account curAccount = new Account(
                        UUID.fromString(getTagValue("id", accountElement)),
                        getTagValue("name", accountElement),
                        LocalDate.parse(getTagValue("date", accountElement)),
                        Role.valueOf(getTagValue("role", accountElement))
                    );
                    accountsToAdd.add(curAccount);
                }
            }
            addAccounts(accountsToAdd);
        }
    }

    private boolean addAccounts(List<Account> accounts) throws SQLException {
        if (accounts == null || accounts.size() == 0) {
            return false; // cannot add zero accounts;
        }
        openConnection();
         // create account DB if necessary
        Statement createAccountDB = connection.createStatement();
        createAccountDB.executeUpdate(ACCOUNT_DB);
        createAccountDB.close();

        for (Account curAccount: accounts) {
            PreparedStatement addAccountToDB = connection.prepareStatement(ACCOUNT_INSERT);
            addAccountToDB.setString(1, curAccount.getAccountId().toString());
            addAccountToDB.setString(2, curAccount.getAccountHolderName());
            addAccountToDB.setString(3, curAccount.getAccountHolderBirthDate().toString());
            addAccountToDB.setString(4, curAccount.getAccountHolderRole().toString());
            addAccountToDB.executeUpdate();
        }
        closeConnection();
        return true;
    }
}
