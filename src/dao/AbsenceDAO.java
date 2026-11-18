package dao;

import model.Absence;
import util.DBConnection;
import java.sql.*;
import java.util.Vector;

public class AbsenceDAO {

    private Connection connection;

    public AbsenceDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean ajouter(Absence absence) {
        String sql = "INSERT INTO Absence (id_etudiant, id_enseignant, id_matiere, numseance, date, justification) "
                +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, absence.getIdEtudiant());
            stmt.setInt(2, absence.getIdEnseignant());
            stmt.setInt(3, absence.getIdMatiere());
            stmt.setInt(4, absence.getNumSeance());
            stmt.setDate(5, new java.sql.Date(absence.getDateAbsence().getTime()));
            stmt.setBoolean(6, absence.isJustifiee());

            int lignesAffectees = stmt.executeUpdate();
            return lignesAffectees > 0;

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de l'ajout de l'absence.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int idAbsence) {
        String sql = "DELETE FROM Absence WHERE id_absence = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAbsence);

            int lignesAffectees = stmt.executeUpdate();
            return lignesAffectees > 0;

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la suppression de l'absence.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean justifier(int idAbsence) {
        String sql = "UPDATE Absence SET justification = TRUE WHERE id_absence = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAbsence);

            int lignesAffectees = stmt.executeUpdate();
            return lignesAffectees > 0;

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la justification de l'absence.");
            e.printStackTrace();
            return false;
        }
    }

    public Vector<Absence> getAll() {
        Vector<Absence> absences = new Vector<>();
        String sql = "SELECT a.*, e.nom AS nom_etudiant, e.prenom AS prenom_etudiant, " +
                "m.libelle AS libelle_matiere, en.nom AS nom_enseignant " +
                "FROM Absence a " +
                "JOIN Etudiant e ON a.id_etudiant = e.id_etudiant " +
                "JOIN Matiere m ON a.id_matiere = m.id_matiere " +
                "JOIN Enseignant en ON a.id_enseignant = en.id_enseignant " +
                "ORDER BY a.date DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Absence a = extraireAbsence(rs);
                absences.add(a);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des absences.");
            e.printStackTrace();
        }

        return absences;
    }

    public Vector<Absence> getByEtudiant(int idEtudiant) {
        Vector<Absence> absences = new Vector<>();
        String sql = "SELECT a.*, e.nom AS nom_etudiant, e.prenom AS prenom_etudiant, " +
                "m.libelle AS libelle_matiere, en.nom AS nom_enseignant " +
                "FROM Absence a " +
                "JOIN Etudiant e ON a.id_etudiant = e.id_etudiant " +
                "JOIN Matiere m ON a.id_matiere = m.id_matiere " +
                "JOIN Enseignant en ON a.id_enseignant = en.id_enseignant " +
                "WHERE a.id_etudiant = ? " +
                "ORDER BY a.date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Absence a = extraireAbsence(rs);
                absences.add(a);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des absences par étudiant.");
            e.printStackTrace();
        }

        return absences;
    }

    public Vector<Absence> getByClasse(int idClasse) {
        Vector<Absence> absences = new Vector<>();
        String sql = "SELECT a.*, e.nom AS nom_etudiant, e.prenom AS prenom_etudiant, " +
                "m.libelle AS libelle_matiere, en.nom AS nom_enseignant " +
                "FROM Absence a " +
                "JOIN Etudiant e ON a.id_etudiant = e.id_etudiant " +
                "JOIN Matiere m ON a.id_matiere = m.id_matiere " +
                "JOIN Enseignant en ON a.id_enseignant = en.id_enseignant " +
                "WHERE e.id_classe = ? " +
                "ORDER BY a.date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClasse);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Absence a = extraireAbsence(rs);
                absences.add(a);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des absences par classe.");
            e.printStackTrace();
        }

        return absences;
    }

    public Vector<Absence> getByEnseignant(int idEnseignant) {
        Vector<Absence> absences = new Vector<>();
        String sql = "SELECT a.*, e.nom AS nom_etudiant, e.prenom AS prenom_etudiant, " +
                "m.libelle AS libelle_matiere, en.nom AS nom_enseignant " +
                "FROM Absence a " +
                "JOIN Etudiant e ON a.id_etudiant = e.id_etudiant " +
                "JOIN Matiere m ON a.id_matiere = m.id_matiere " +
                "JOIN Enseignant en ON a.id_enseignant = en.id_enseignant " +
                "WHERE a.id_enseignant = ? " +
                "ORDER BY a.date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEnseignant);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Absence a = extraireAbsence(rs);
                absences.add(a);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération des absences par enseignant.");
            e.printStackTrace();
        }

        return absences;
    }

    public Absence getById(int idAbsence) {
        String sql = "SELECT a.*, e.nom AS nom_etudiant, e.prenom AS prenom_etudiant, " +
                "m.libelle AS libelle_matiere, en.nom AS nom_enseignant " +
                "FROM Absence a " +
                "JOIN Etudiant e ON a.id_etudiant = e.id_etudiant " +
                "JOIN Matiere m ON a.id_matiere = m.id_matiere " +
                "JOIN Enseignant en ON a.id_enseignant = en.id_enseignant " +
                "WHERE a.id_absence = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAbsence);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extraireAbsence(rs);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors de la récupération de l'absence.");
            e.printStackTrace();
        }

        return null;
    }

    public Vector<Object[]> getStatistiquesParClasse() {
        Vector<Object[]> stats = new Vector<>();
        String sql = "SELECT c.id_classe, c.libelle, COUNT(a.id_absence) AS nb_absences " +
                "FROM Classe c " +
                "LEFT JOIN Etudiant e ON c.id_classe = e.id_classe " +
                "LEFT JOIN Absence a ON e.id_etudiant = a.id_etudiant " +
                "GROUP BY c.id_classe, c.libelle " +
                "ORDER BY nb_absences DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] ligne = new Object[3];
                ligne[0] = rs.getInt("id_classe");
                ligne[1] = rs.getString("libelle");
                ligne[2] = rs.getInt("nb_absences");
                stats.add(ligne);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors du calcul des statistiques.");
            e.printStackTrace();
        }

        return stats;
    }

    public Vector<Object[]> getStatistiquesParEtudiant(int idClasse) {
        Vector<Object[]> stats = new Vector<>();
        String sql = "SELECT CONCAT(e.prenom, ' ', e.nom) AS nom_complet, COUNT(a.id_absence) AS nb_absences " +
                "FROM Etudiant e " +
                "LEFT JOIN Absence a ON e.id_etudiant = a.id_etudiant " +
                "WHERE e.id_classe = ? " +
                "GROUP BY e.id_etudiant, e.nom, e.prenom " +
                "ORDER BY nb_absences DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClasse);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] ligne = new Object[2];
                ligne[0] = rs.getString("nom_complet");
                ligne[1] = rs.getInt("nb_absences");
                stats.add(ligne);
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Erreur lors du calcul des statistiques.");
            e.printStackTrace();
        }

        return stats;
    }

    private Absence extraireAbsence(ResultSet rs) throws SQLException {
        Absence a = new Absence();
        a.setIdAbsence(rs.getInt("id_absence"));
        a.setIdEtudiant(rs.getInt("id_etudiant"));
        a.setIdEnseignant(rs.getInt("id_enseignant"));
        a.setIdMatiere(rs.getInt("id_matiere"));
        a.setNumSeance(rs.getInt("numseance"));
        a.setDateAbsence(rs.getDate("date"));
        a.setJustifiee(rs.getBoolean("justification"));
        a.setNomEtudiant(rs.getString("nom_etudiant"));
        a.setPrenomEtudiant(rs.getString("prenom_etudiant"));
        a.setLibelleMatiere(rs.getString("libelle_matiere"));
        a.setNomEnseignant(rs.getString("nom_enseignant"));
        return a;
    }
}
