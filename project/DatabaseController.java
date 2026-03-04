import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * Abstract class for managing database connectivity
 * This class provides functionality for opening and closing databae connections
 * executing common queires like row counting.
 * Sub classes extend this class to do database operations
 */
public abstract class DatabaseController {
    /** Name of database */
    private String databaseName;
    /** Active database connection */
    protected Connection connection;
    /**
     * Constructs DatabaseController with specific database name
     * @param databaseName the name fo database file
     * @throws IllegalArgumentException if database is invalid
     */
    public DatabaseController(String databaseName) {

        if(databaseName == null || databaseName.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.databaseName = databaseName;
    }
    /**
     * Opens connection if one is not already active
     * @return true if connection was opened and false if connection already active
     * @throws SQLException if database access error occurs
     */
    public boolean openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String connectionString = "jdbc:sqlite:" + databaseName + ".db";
            connection = DriverManager.getConnection(connectionString);
            return true;
        }
        return false; // an active connection already exists
    }
    /**
     * Closes active database connection if exist
     * @return true if connection successfully closed, false if no connection exists
     * @throws SQLException is database access error occurs
     */
    public boolean closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            return true;
        }
        return false; // a non-active connection cannot be closed
    }
    /**
     * Returns current database connection
     * @return the active Connection object
     */
    protected Connection getConnection() {
        return connection;
    }
    /**
     * Returns the numnber of rows in the database table that match the
     * specified column name and values
     * @param colName the column name to filter
     * @param colVal the value to match in column
     * @return the number of matching rows
     * @throws SQLException if database error occurs
     * @throws IllegalArgumentException if parameters are invalid
     */
    public int getRowCount(String colName, String colVal) throws SQLException {
          if(colName == null || colName.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if(colVal == null || colVal.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        // Open connection
        openConnection();

        // Get row count from database
        String getRowCountString = "SELECT COUNT(*) FROM " + databaseName + " WHERE " + colName + " = ?";
        PreparedStatement getRowCountFromDB = connection.prepareStatement(getRowCountString);
        getRowCountFromDB.setString(1, colVal);
        ResultSet resultSet = getRowCountFromDB.executeQuery();
        resultSet.next(); // moves cursor to rowCount
        int rowCount = resultSet.getInt(1);

        // Close connection
        closeConnection();

        // Return row count
        return rowCount;
    }
    /**
     * Extrats adn returns text value of a XML tag
     * from the specified element
     * @param tag the XML tag name
     * @param element the XML element containing the tag
     * @return the text value of the specified tag
     */
    protected static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}