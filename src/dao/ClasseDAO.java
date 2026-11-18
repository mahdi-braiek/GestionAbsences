package dao;

import model.Classe;
import util.DBConnection;
import java.sql.*;
import java.util.Vector;

public class ClasseDAO {

    private Connection connection;

    public ClasseDAO() {
        this.connection = DBConnection.getConnection();
    }

    public Vector<Classe> getAll() {
        Vector<Classe> classes = new Vector<>();
        String sql = "SELECT * FROM Classe ORDER BY libelle";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Classe c = new Classe();
                c.setIdClasse(rs.getInt("id_classe"));
                c.setLibelle(rs.getString("libelle"));
                c.setNiveau(rs.getString("niveau"));
                c.setFiliere(rs.getString("filiere"));
                classes.add(c);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des classes.");
            e.printStackTrace();
        }

        return classes;
    }

    public Classe getById(int id) {
        String sql = "SELECT * FROM Classe WHERE id_classe = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Classe c = new Classe();
                c.setIdClasse(rs.getInt("id_classe"));
                c.setLibelle(rs.getString("libelle"));
                c.setNiveau(rs.getString("niveau"));
                c.setFiliere(rs.getString("filiere"));
                return c;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération de la classe.");
            e.printStackTrace();
        }

        return null;
    }

    public Vector<Classe> getByEnseignant(int idEnseignant) {
        Vector<Classe> classes = new Vector<>();
        String sql = "SELECT DISTINCT c.* FROM Classe c " +
                "JOIN Correspondance co ON c.id_classe = co.id_classe " +
                "WHERE co.id_enseignant = ? ORDER BY c.libelle";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEnseignant);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Classe c = new Classe();
                c.setIdClasse(rs.getInt("id_classe"));
                c.setLibelle(rs.getString("libelle"));
                c.setNiveau(rs.getString("niveau"));
                c.setFiliere(rs.getString("filiere"));
                classes.add(c);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des classes par enseignant.");
            e.printStackTrace();
        }

        return classes;
    }
}
