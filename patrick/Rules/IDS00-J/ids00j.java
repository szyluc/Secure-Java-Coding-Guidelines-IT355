/**
 * Name: Patrick Marrella
 * Date: February 9, 2026
 * RULE: IDS00-J
 * 
 * Description: Prevent SQL Injection
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public void doPrivilegedAction(String username, char[] password) throws SQLException {
    Connection connection = getConnection();
    if (connection == null) {
        // handle error
    }
    try {
        if (username.length() > 8) {
            // some appropriate error for short username
        }
        String pwd = hashPassword(password);
        String sqlString = """
                SELECT * FROM DB_USER WHERE USERNAME=? AND PASSWORD=?
                """;
        PreparedStatement stmt = connection.prepareStatement(sqlString);
        stmt.setString(1, username);
        stmt.setString(2, pwd);
        ResultSet rs = stmt.executeQuery();

        // if username / password hash is incorrect
        if (!rs.next()) {
            throw new SecurityException("Username / Password incorrect.");
        }
        // At this stage, we are authenticated and can do whatever we wish.


    } finally {
        try {
            connection.close();
        } catch (SQLException x) {
            // Some kind of handler if the connection could not be closed
        }
    }
}

class Login {
    public Connection getConnection() throws SQLException {
        Driver serverDriver = new com.microsoft.sqlserver.jdbc.SQLServerDriver();
        DriverManager.registerDriver(serverDriver);
        String dbConnection = PropertyManager.getProperty("db.connection");

        //
        return DriverManager.getConnection(dbConnection);
    }

    String hashPassword(char[] password) {

    }

    public void doPrivilegedAction(String username, char[] password) throws SQLException {
        Connection connection = getConnection();
    }

}

/**
 * Main method for rule IDS00-J
 * @param args
 */
public static void main(String[] args) {
    
}