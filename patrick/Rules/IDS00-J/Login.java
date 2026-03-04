import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to implement rule IDS00-J. 
 * Prevent SQL injection.
 */
class Login {
    private static final String DB_NAME = "test";
    private final String CREATE_DB = """
        CREATE TABLE IF NOT EXISTS test (
            username TEXT PRIMARY KEY,
            password INTEGER
    );
    """;
    private Connection connection;

    /**
     * Main method.
     * @param args Console args
     */
    public static void main(String[] args) {
        Login login = null;
        try {
            login = new Login();
            login.createTable();
            login.openConnection();
            String username = "myusername";
            char[] password = {'m', 'y', 'p', 'a', 's', 's'};
            login.doPrivilegedAction(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (login != null) {
                try {
                    login.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates the SQLite table
     * @throws Exception
     */
    public void createTable() throws Exception {
        openConnection();
        Statement createDB = connection.createStatement();
        try {
            createDB.executeUpdate(CREATE_DB);
        } finally {
            try {
                createDB.close();
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to apply a simple hash function on a char array
     * @param password The inputted char array
     * @return A hash value
     */
    private int hashPassword(char[] password) {
        int hash = 0;
        for (char c : password) {
            hash = 31 * hash + c;  // simple polynomial accumulation
        }
        return hash;
    }

    /**
     * Simulates a privileged action (i.e. adding an account to the database)
     * @param username The user's username
     * @param password The user's password
     * @throws SQLException
     */
    public void doPrivilegedAction(String username, char[] password) throws SQLException {
        if (connection == null || connection.isClosed()) {
            return;
        }
        try {
            if (username.length() < 8) {
                System.out.println("Please provide longer username!");
                return;
            }
            int pwdHash = hashPassword(password);
            String sqlString = "INSERT INTO test (username, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlString);
            stmt.setString(1, username);
            stmt.setInt(2, pwdHash);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the DB connection
     * @return True: connection successful. False: connection unsuccessful.
     * @throws SQLException
     */
    public boolean openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String connectionString = "jdbc:sqlite:" + DB_NAME + ".db";
            connection = DriverManager.getConnection(connectionString);
            return true;
        }
        return false; // an active connection already exists
    }

    /**
     * Closes the DB connection
     * @return True: closing successful. False: closing unsuccessful.
     * @throws SQLException
     */
    private boolean closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            return true;
        }
        return false; // a non-active connection cannot be closed
    }
}