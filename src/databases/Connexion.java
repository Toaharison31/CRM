package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utils.Config;

/**
 * Gestionnaire de connexions à la base de données PostgreSQL.
 * Thread-safe avec synchronisation et gestion robuste des exceptions.
 */
public class Connexion {
    private static Connection connection;
    // Lock pour assurer la thread-safety
    private static final Object CONNECTION_LOCK = new Object();

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✓ PostgreSQL JDBC driver chargé avec succès.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERREUR: Driver PostgreSQL JDBC non trouvé.");
            System.err.println("   Ajouter le JAR du driver au classpath.");
        }
    }

    /**
     * Obtient une connexion à la base de données (thread-safe).
     * Recrée la connexion si elle est fermée.
     * 
     * @return Connection à la base de données
     * @throws SQLException En cas d'erreur de connexion
     */
    public static Connection getConnection() throws SQLException {
        synchronized (CONNECTION_LOCK) {
            if (connection == null || connection.isClosed()) {
                try {
                    connection = DriverManager.getConnection(
                            Config.getDatabaseUrl(),
                            Config.getDatabaseUser(),
                            Config.getDatabasePassword()
                    );
                    System.out.println("✓ Connexion à la base de données établie.");
                } catch (SQLException e) {
                    System.err.println("❌ ERREUR: Impossible de connecter à la base de données.");
                    System.err.println("   Message: " + e.getMessage());
                    throw e;
                }
            }
            return connection;
        }
    }

    /**
     * Ferme la connexion à la base de données (thread-safe).
     * À appeler lors de l'arrêt de l'application.
     */
    public static void closeConnection() {
        synchronized (CONNECTION_LOCK) {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("✓ Connexion à la base de données fermée.");
                } catch (SQLException e) {
                    System.err.println("❌ ERREUR lors de la fermeture de la connexion: " + e.getMessage());
                } finally {
                    connection = null;
                }
            }
        }
    }

    /**
     * Vérifie si la connexion est active.
     * 
     * @return true si la connexion est active
     */
    public static boolean isConnected() {
        synchronized (CONNECTION_LOCK) {
            try {
                return connection != null && !connection.isClosed();
            } catch (SQLException e) {
                System.err.println("❌ ERREUR lors de la vérification de la connexion: " + e.getMessage());
                return false;
            }
        }
    }
}
