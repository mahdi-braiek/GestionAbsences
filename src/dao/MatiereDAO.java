package dao;

import model.Matiere;
import util.DBConnection;

import java.sql.*;
import java.util.Vector;


public class MatiereDAO {

    private Connection connection;

    public MatiereDAO() {
        this.connection = DBConnection.getConnection();
    }

    public Vector<Matiere> getAll() {
        Vector<Matiere> matieres = new Vector<>();
        String sql = "SELECT * FROM Matiere ORDER BY libelle";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Matiere m = new Matiere();
                m.setIdMatiere(rs.getInt("id_matiere"));
                m.setLibelle(rs.getString("libelle"));
                matieres.add(m);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des matières.");
            e.printStackTrace();
        }

        return matieres;
    }

    public Matiere getById(int id) {
        String sql = "SELECT * FROM Matiere WHERE id_matiere = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Matiere m = new Matiere();
                m.setIdMatiere(rs.getInt("id_matiere"));
                m.setLibelle(rs.getString("libelle"));
                return m;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération de la matière.");
            e.printStackTrace();
        }

        return null;
    }

    public Vector<Matiere> getByEnseignantAndClasse(int idEnseignant, int idClasse) {
        Vector<Matiere> matieres = new Vector<>();
        String sql = "SELECT m.* FROM Matiere m " +
                "JOIN Correspondance co ON m.id_matiere = co.id_matiere " +
                "WHERE co.id_enseignant = ? AND co.id_classe = ? ORDER BY m.libelle";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEnseignant);
            stmt.setInt(2, idClasse);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Matiere m = new Matiere();
                m.setIdMatiere(rs.getInt("id_matiere"));
                m.setLibelle(rs.getString("libelle"));
                matieres.add(m);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des matières.");
            e.printStackTrace();
        }

        return matieres;
    }
}
