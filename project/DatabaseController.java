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

public abstract class DatabaseController {
    private String databaseName;
    protected Connection connection;

    public DatabaseController(String databaseName) {

        if(databaseName == null || databaseName.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.databaseName = databaseName;
    }

    public boolean openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String connectionString = "jdbc:sqlite:" + databaseName + ".db";
            connection = DriverManager.getConnection(connectionString);
            return true;
        }
        return false; // an active connection already exists
    }

    public boolean closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            return true;
        }
        return false; // a non-active connection cannot be closed
    }

    protected Connection getConnection() {
        return connection;
    }

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

    protected static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}