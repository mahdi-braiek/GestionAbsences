package dao;

import model.Etudiant;
import util.DBConnection;

import java.sql.*;
import java.util.Vector;


public class EtudiantDAO {

    private Connection connection;

    public EtudiantDAO() {
        this.connection = DBConnection.getConnection();
    }

    public Vector<Etudiant> getAll() {
        Vector<Etudiant> etudiants = new Vector<>();
        String sql = "SELECT * FROM Etudiant ORDER BY nom, prenom";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId(rs.getInt("id_etudiant"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setLogin(rs.getString("login"));
                e.setPwd(rs.getString("pwd"));
                e.setIdClasse(rs.getInt("id_classe"));
                etudiants.add(e);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des étudiants.");
            e.printStackTrace();
        }

        return etudiants;
    }

    public Etudiant getById(int id) {
        String sql = "SELECT * FROM Etudiant WHERE id_etudiant = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId(rs.getInt("id_etudiant"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setLogin(rs.getString("login"));
                e.setPwd(rs.getString("pwd"));
                e.setIdClasse(rs.getInt("id_classe"));
                return e;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération de l'étudiant.");
            e.printStackTrace();
        }

        return null;
    }

    public Vector<Etudiant> getByClasse(int idClasse) {
        Vector<Etudiant> etudiants = new Vector<>();
        String sql = "SELECT * FROM Etudiant WHERE id_classe = ? ORDER BY nom, prenom";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClasse);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId(rs.getInt("id_etudiant"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setLogin(rs.getString("login"));
                e.setPwd(rs.getString("pwd"));
                e.setIdClasse(rs.getInt("id_classe"));
                etudiants.add(e);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des étudiants par classe.");
            e.printStackTrace();
        }

        return etudiants;
    }
}
