package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import psql.Lien;

public class Connexion {
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found. Add the driver to the classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(Lien.URL, Lien.USER, Lien.PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.getStackTrace();
            } finally {
                connection = null;
            }
        }
    }
}
