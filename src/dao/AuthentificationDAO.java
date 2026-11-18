package dao;

import java.sql.*;
import model.Enseignant;
import model.Etudiant;
import model.Responsable;
import model.Utilisateur;
import util.DBConnection;
import util.PasswordUtils;

public class AuthentificationDAO {

    private Connection connection;

    public AuthentificationDAO() {
        this.connection = DBConnection.getConnection();
    }

    public Utilisateur authentifier(String login, String password) {
        Utilisateur utilisateur = null;
        String passwordHash = PasswordUtils.hash(password);

        utilisateur = rechercherEtudiant(login, passwordHash);
        if (utilisateur != null) {
            return utilisateur;
        }

        utilisateur = rechercherEnseignant(login, passwordHash);
        if (utilisateur != null) {
            return utilisateur;
        }

        utilisateur = rechercherResponsable(login, passwordHash);

        return utilisateur;
    }
    private Etudiant rechercherEtudiant(String login, String password) {
        String sql = "SELECT * FROM Etudiant WHERE login = ? AND pwd = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getInt("id_etudiant"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiant.setLogin(rs.getString("login"));
                etudiant.setPwd(rs.getString("pwd"));
                etudiant.setIdClasse(rs.getInt("id_classe"));
                return etudiant;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la recherche de l'étudiant.");
            e.printStackTrace();
        }

        return null;
    }

    private Enseignant rechercherEnseignant(String login, String password) {
        String sql = "SELECT * FROM Enseignant WHERE login = ? AND pwd = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Enseignant enseignant = new Enseignant();
                enseignant.setId(rs.getInt("id_enseignant"));
                enseignant.setNom(rs.getString("nom"));
                enseignant.setPrenom(rs.getString("prenom"));
                enseignant.setLogin(rs.getString("login"));
                enseignant.setPwd(rs.getString("pwd"));
                return enseignant;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la recherche de l'enseignant.");
            e.printStackTrace();
        }

        return null;
    }

    private Responsable rechercherResponsable(String login, String password) {
        String sql = "SELECT * FROM Responsable WHERE login = ? AND pwd = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Responsable responsable = new Responsable();
                responsable.setId(rs.getInt("id_responsable"));
                responsable.setNom(rs.getString("nom"));
                responsable.setPrenom(rs.getString("prenom"));
                responsable.setLogin(rs.getString("login"));
                responsable.setPwd(rs.getString("pwd"));
                return responsable;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la recherche du responsable.");
            e.printStackTrace();
        }

        return null;
    }
}
