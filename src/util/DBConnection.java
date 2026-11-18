package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = System.getenv().getOrDefault(
            "DB_URL", "jdbc:mysql://localhost:3306/absence_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    private static Connection connection = null;

    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[INFO] Connexion à la base de données réussie !");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[ERREUR] Driver MySQL non trouvé !");
            System.err.println("Assurez-vous d'avoir le fichier mysql-connector-java.jar dans le classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ERREUR] Impossible de se connecter à la base de données !");
            System.err.println("Verifiez que MySQL est demarre et que la base existe.");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("[INFO] Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("[ERREUR] Erreur lors de la fermeture de la connexion.");
                e.printStackTrace();
            }
        }
    }
}
