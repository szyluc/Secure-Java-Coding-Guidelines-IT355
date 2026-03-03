import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DatabaseController {
    private String databaseName;
    protected Connection connection;

    public DatabaseController(String databaseName) {
        this.databaseName = databaseName;
    }

    public boolean openConnection() throws SQLException {
        if (connection == null) {
            String connectionString = "jdbc:sqlite:" + databaseName + ".db";
            connection = DriverManager.getConnection(connectionString);
            return true;
        }
        return false; // an active connection already exists
    }

    public boolean closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            return true;
        }
        return false; // a non-active connection cannot be closed
    }

    protected Connection getConnection() {
        return connection;
    }

    public int getRowCount() throws SQLException {
        String getRowCountString = "SELECT COUNT(*) FROM " + databaseName;
        PreparedStatement getRowCountFromDB = connection.prepareStatement(getRowCountString);
        ResultSet resultSet = getRowCountFromDB.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
        
    }
}