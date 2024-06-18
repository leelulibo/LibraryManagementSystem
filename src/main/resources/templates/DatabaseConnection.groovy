package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Make sure to adjust the serverName and instanceName to your environment
    private static final String URL = "jdbc:sqlserver://Sibaxolise.Mningiswa;databaseName=Library;integratedSecurity=true";

    public static Connection getConnection() throws SQLException {
        // Load SQL Server JDBC driver if necessary
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC driver not found.", e);
        }
        return DriverManager.getConnection(URL);
    }
}