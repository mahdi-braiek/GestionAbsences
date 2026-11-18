package main;

import dao.*;
import model.*;
import util.ConsoleUtils;
import util.DBConnection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Application {

    private static Utilisateur utilisateurConnecte = null;

    private static AuthentificationDAO authDAO = new AuthentificationDAO();
    private static AbsenceDAO absenceDAO = new AbsenceDAO();
    private static EtudiantDAO etudiantDAO = new EtudiantDAO();
    private static ClasseDAO classeDAO = new ClasseDAO();
    private static MatiereDAO matiereDAO = new MatiereDAO();

    public static void main(String[] args) {
        ConsoleUtils.afficherTitre("GESTION DES ABSENCES DES ÉTUDIANTS");
        ConsoleUtils.afficherInfo("Système de gestion des absences - Version 1.0");
        ConsoleUtils.afficherSeparateur();
        boolean continuer = true;
        while (continuer) {
            if (utilisateurConnecte == null) {
                continuer = menuConnexion();
            } else {
                switch (utilisateurConnecte.getRole()) {
                    case "ETUDIANT":
                        menuEtudiant();
                        break;
                    case "ENSEIGNANT":
                        menuEnseignant();
                        break;
                    case "RESPONSABLE":
                        menuResponsable();
                        break;
                }
            }
        }

        DBConnection.closeConnection();
        ConsoleUtils.afficherInfo("Au revoir !");
    }

    private static boolean menuConnexion() {
        ConsoleUtils.afficherSousTitre("CONNEXION");
        System.out.println("1. Se connecter");
        System.out.println("0. Quitter");
        ConsoleUtils.afficherSeparateur();

        int choix = ConsoleUtils.lireInt("Votre choix : ");

        switch (choix) {
            case 1:
                seConnecter();
                return true;
            case 0:
                return false;
            default:
                ConsoleUtils.afficherErreur("Choix invalide !");
                return true;
        }
    }

    private static void seConnecter() {
        ConsoleUtils.effacerEcran();
        ConsoleUtils.afficherSousTitre("AUTHENTIFICATION");

        String login = ConsoleUtils.lireString("Login : ");
        String password = ConsoleUtils.lireString("Mot de passe : ");

        utilisateurConnecte = authDAO.authentifier(login, password);

        if (utilisateurConnecte != null) {
            ConsoleUtils.afficherSucces("Connexion réussie !");
            ConsoleUtils.afficherInfo("Bienvenue " + utilisateurConnecte.getNomComplet() +
                    " (" + utilisateurConnecte.getRole() + ")");
        } else {
            ConsoleUtils.afficherErreur("Login ou mot de passe incorrect !");
        }

        ConsoleUtils.pause();
    }

    private static void seDeconnecter() {
        ConsoleUtils.afficherInfo("Déconnexion de " + utilisateurConnecte.getNomComplet());
        utilisateurConnecte = null;
    }

    private static void menuEtudiant() {
        ConsoleUtils.effacerEcran();
        ConsoleUtils.afficherTitre("ESPACE ÉTUDIANT");
        ConsoleUtils.afficherInfo("Connecté en tant que : " + utilisateurConnecte.getNomComplet());
        ConsoleUtils.afficherSeparateur();

        System.out.println("1. Consulter mes absences");
        System.out.println("0. Se déconnecter");
        ConsoleUtils.afficherSeparateur();

        int choix = ConsoleUtils.lireInt("Votre choix : ");

        switch (choix) {
            case 1:
                consulterMesAbsences();
                break;
            case 0:
                seDeconnecter();
                break;
            default:
                ConsoleUtils.afficherErreur("Choix invalide !");
        }
    }

    private static void consulterMesAbsences() {
        ConsoleUtils.afficherSousTitre("MES ABSENCES");

        Etudiant etudiant = (Etudiant) utilisateurConnecte;
        Vector<Absence> absences = absenceDAO.getByEtudiant(etudiant.getId());

        if (absences.isEmpty()) {
            ConsoleUtils.afficherSucces("Vous n'avez aucune absence enregistrée.");
        } else {
            afficherListeAbsences(absences);
            ConsoleUtils.afficherInfo("Total : " + absences.size() + " absence(s)");
        }

        ConsoleUtils.pause();
    }

    private static void menuEnseignant() {
        ConsoleUtils.effacerEcran();
        ConsoleUtils.afficherTitre("ESPACE ENSEIGNANT");
        ConsoleUtils.afficherInfo("Connecté en tant que : " + utilisateurConnecte.getNomComplet());
        ConsoleUtils.afficherSeparateur();

        System.out.println("1. Enregistrer une absence");
        System.out.println("2. Consulter les absences d'une classe");
        System.out.println("0. Se déconnecter");
        ConsoleUtils.afficherSeparateur();

        int choix = ConsoleUtils.lireInt("Votre choix : ");

        switch (choix) {
            case 1:
                enregistrerAbsence();
                break;
            case 2:
                consulterAbsencesClasse();
                break;
            case 0:
                seDeconnecter();
                break;
            default:
                ConsoleUtils.afficherErreur("Choix invalide !");
        }
    }

    private static void enregistrerAbsence() {
        ConsoleUtils.afficherSousTitre("ENREGISTRER UNE ABSENCE");

        Enseignant enseignant = (Enseignant) utilisateurConnecte;

        Vector<Classe> classes = classeDAO.getByEnseignant(enseignant.getId());
        if (classes.isEmpty()) {
            ConsoleUtils.afficherErreur("Vous n'enseignez dans aucune classe.");
            ConsoleUtils.pause();
            return;
        }

        ConsoleUtils.afficherInfo("Vos classes :");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getLibelle());
        }

        int choixClasse = ConsoleUtils.lireInt("Choisir une classe (numéro) : ");
        if (choixClasse < 1 || choixClasse > classes.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }
        Classe classeChoisie = classes.get(choixClasse - 1);

        Vector<Matiere> matieres = matiereDAO.getByEnseignantAndClasse(enseignant.getId(), classeChoisie.getIdClasse());
        if (matieres.isEmpty()) {
            ConsoleUtils.afficherErreur("Aucune matière trouvée.");
            ConsoleUtils.pause();
            return;
        }

        ConsoleUtils.afficherInfo("Matières :");
        for (int i = 0; i < matieres.size(); i++) {
            System.out.println((i + 1) + ". " + matieres.get(i).getLibelle());
        }

        int choixMatiere = ConsoleUtils.lireInt("Choisir une matière (numéro) : ");
        if (choixMatiere < 1 || choixMatiere > matieres.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }
        Matiere matiereChoisie = matieres.get(choixMatiere - 1);

        Vector<Etudiant> etudiants = etudiantDAO.getByClasse(classeChoisie.getIdClasse());
        if (etudiants.isEmpty()) {
            ConsoleUtils.afficherErreur("Aucun étudiant dans cette classe.");
            ConsoleUtils.pause();
            return;
        }

        ConsoleUtils.afficherInfo("Étudiants de la classe " + classeChoisie.getLibelle() + " :");
        for (int i = 0; i < etudiants.size(); i++) {
            System.out.println((i + 1) + ". " + etudiants.get(i).getNomComplet());
        }

        int choixEtudiant = ConsoleUtils.lireInt("Choisir l'étudiant absent (numéro) : ");
        if (choixEtudiant < 1 || choixEtudiant > etudiants.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }
        Etudiant etudiantAbsent = etudiants.get(choixEtudiant - 1);

        int numSeance = ConsoleUtils.lireInt("Numéro de la séance : ");
        if (numSeance < 1) {
            ConsoleUtils.afficherErreur("Numéro de séance invalide !");
            ConsoleUtils.pause();
            return;
        }

        Absence absence = new Absence();
        absence.setIdEtudiant(etudiantAbsent.getId());
        absence.setIdEnseignant(enseignant.getId());
        absence.setIdMatiere(matiereChoisie.getIdMatiere());
        absence.setNumSeance(numSeance);
        absence.setDateAbsence(new Date());
        absence.setJustifiee(false);

        if (absenceDAO.ajouter(absence)) {
            ConsoleUtils.afficherSucces("Absence enregistrée avec succès !");

            ConsoleUtils.simulerEnvoiMail(
                    etudiantAbsent.getNomComplet(),
                    "Vous avez été marqué absent en " + matiereChoisie.getLibelle() +
                            " (Séance " + numSeance + ")");
        } else {
            ConsoleUtils.afficherErreur("Erreur lors de l'enregistrement de l'absence.");
        }

        ConsoleUtils.pause();
    }

    private static void consulterAbsencesClasse() {
        ConsoleUtils.afficherSousTitre("ABSENCES PAR CLASSE");

        Vector<Classe> classes = classeDAO.getAll();
        if (classes.isEmpty()) {
            ConsoleUtils.afficherErreur("Aucune classe trouvée.");
            ConsoleUtils.pause();
            return;
        }

        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getLibelle());
        }

        int choix = ConsoleUtils.lireInt("Choisir une classe (numéro) : ");
        if (choix < 1 || choix > classes.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }

        Classe classeChoisie = classes.get(choix - 1);
        Vector<Absence> absences = absenceDAO.getByClasse(classeChoisie.getIdClasse());

        ConsoleUtils.afficherInfo("Absences de la classe " + classeChoisie.getLibelle() + " :");

        if (absences.isEmpty()) {
            ConsoleUtils.afficherSucces("Aucune absence enregistrée pour cette classe.");
        } else {
            afficherListeAbsences(absences);
            ConsoleUtils.afficherInfo("Total : " + absences.size() + " absence(s)");
        }

        ConsoleUtils.pause();
    }

    private static void menuResponsable() {
        ConsoleUtils.effacerEcran();
        ConsoleUtils.afficherTitre("ESPACE RESPONSABLE");
        ConsoleUtils.afficherInfo("Connecté en tant que : " + utilisateurConnecte.getNomComplet());
        ConsoleUtils.afficherSeparateur();

        System.out.println("1. Consulter toutes les absences");
        System.out.println("2. Consulter les absences par classe");
        System.out.println("3. Consulter les absences par étudiant");
        System.out.println("4. Annuler une absence");
        System.out.println("5. Justifier une absence");
        System.out.println("6. Statistiques par classe");
        System.out.println("7. Statistiques par étudiant");
        System.out.println("0. Se déconnecter");
        ConsoleUtils.afficherSeparateur();

        int choix = ConsoleUtils.lireInt("Votre choix : ");

        switch (choix) {
            case 1:
                consulterToutesAbsences();
                break;
            case 2:
                consulterAbsencesParClasse();
                break;
            case 3:
                consulterAbsencesParEtudiant();
                break;
            case 4:
                annulerAbsence();
                break;
            case 5:
                justifierAbsence();
                break;
            case 6:
                afficherStatistiquesParClasse();
                break;
            case 7:
                afficherStatistiquesParEtudiant();
                break;
            case 0:
                seDeconnecter();
                break;
            default:
                ConsoleUtils.afficherErreur("Choix invalide !");
        }
    }

    private static void consulterToutesAbsences() {
        ConsoleUtils.afficherSousTitre("TOUTES LES ABSENCES");

        Vector<Absence> absences = absenceDAO.getAll();

        if (absences.isEmpty()) {
            ConsoleUtils.afficherSucces("Aucune absence enregistrée dans le système.");
        } else {
            afficherListeAbsences(absences);
            ConsoleUtils.afficherInfo("Total : " + absences.size() + " absence(s)");
        }

        ConsoleUtils.pause();
    }

    private static void consulterAbsencesParClasse() {
        ConsoleUtils.afficherSousTitre("ABSENCES PAR CLASSE");

        Vector<Classe> classes = classeDAO.getAll();
        if (classes.isEmpty()) {
            ConsoleUtils.afficherErreur("Aucune classe trouvée.");
            ConsoleUtils.pause();
            return;
        }

        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getLibelle() +
                    " (" + classes.get(i).getFiliere() + ")");
        }

        int choix = ConsoleUtils.lireInt("Choisir une classe (numéro) : ");
        if (choix < 1 || choix > classes.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }

        Classe classeChoisie = classes.get(choix - 1);
        Vector<Absence> absences = absenceDAO.getByClasse(classeChoisie.getIdClasse());

        ConsoleUtils.afficherInfo("Absences de la classe : " + classeChoisie.getLibelle());

        if (absences.isEmpty()) {
            ConsoleUtils.afficherSucces("Aucune absence pour cette classe.");
        } else {
            afficherListeAbsences(absences);
            ConsoleUtils.afficherInfo("Total : " + absences.size() + " absence(s)");
        }

        ConsoleUtils.pause();
    }

    private static void consulterAbsencesParEtudiant() {
        ConsoleUtils.afficherSousTitre("ABSENCES PAR ÉTUDIANT");

        Vector<Etudiant> etudiants = etudiantDAO.getAll();
        if (etudiants.isEmpty()) {
            ConsoleUtils.afficherErreur("Aucun étudiant trouvé.");
            ConsoleUtils.pause();
            return;
        }

        for (int i = 0; i < etudiants.size(); i++) {
            System.out.println((i + 1) + ". " + etudiants.get(i).getNomComplet());
        }

        int choix = ConsoleUtils.lireInt("Choisir un étudiant (numéro) : ");
        if (choix < 1 || choix > etudiants.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }

        Etudiant etudiantChoisi = etudiants.get(choix - 1);
        Vector<Absence> absences = absenceDAO.getByEtudiant(etudiantChoisi.getId());

        ConsoleUtils.afficherInfo("Absences de : " + etudiantChoisi.getNomComplet());

        if (absences.isEmpty()) {
            ConsoleUtils.afficherSucces("Aucune absence pour cet étudiant.");
        } else {
            afficherListeAbsences(absences);
            ConsoleUtils.afficherInfo("Total : " + absences.size() + " absence(s)");
        }

        ConsoleUtils.pause();
    }

    private static void annulerAbsence() {
        ConsoleUtils.afficherSousTitre("ANNULER UNE ABSENCE");

        Vector<Absence> absences = absenceDAO.getAll();
        if (absences.isEmpty()) {
            ConsoleUtils.afficherInfo("Aucune absence à annuler.");
            ConsoleUtils.pause();
            return;
        }

        afficherListeAbsences(absences);

        int idAbsence = ConsoleUtils.lireInt("ID de l'absence à annuler (0 pour annuler) : ");
        if (idAbsence == 0) {
            return;
        }

        Absence absence = absenceDAO.getById(idAbsence);
        if (absence == null) {
            ConsoleUtils.afficherErreur("Absence non trouvée !");
            ConsoleUtils.pause();
            return;
        }

        String confirmation = ConsoleUtils.lireString("Confirmer la suppression ? (oui/non) : ");
        if (confirmation.equalsIgnoreCase("oui")) {
            if (absenceDAO.supprimer(idAbsence)) {
                ConsoleUtils.afficherSucces("Absence annulée avec succès !");
            } else {
                ConsoleUtils.afficherErreur("Erreur lors de l'annulation.");
            }
        } else {
            ConsoleUtils.afficherInfo("Annulation annulée.");
        }

        ConsoleUtils.pause();
    }

    private static void justifierAbsence() {
        ConsoleUtils.afficherSousTitre("JUSTIFIER UNE ABSENCE");

        Vector<Absence> absences = absenceDAO.getAll();
        if (absences.isEmpty()) {
            ConsoleUtils.afficherInfo("Aucune absence enregistrée.");
            ConsoleUtils.pause();
            return;
        }

        afficherListeAbsences(absences);

        int idAbsence = ConsoleUtils.lireInt("ID de l'absence à justifier (0 pour annuler) : ");
        if (idAbsence == 0) {
            return;
        }

        Absence absence = absenceDAO.getById(idAbsence);
        if (absence == null) {
            ConsoleUtils.afficherErreur("Absence non trouvée !");
            ConsoleUtils.pause();
            return;
        }

        if (absence.isJustifiee()) {
            ConsoleUtils.afficherInfo("Cette absence est déjà justifiée.");
        } else {
            if (absenceDAO.justifier(idAbsence)) {
                ConsoleUtils.afficherSucces("Absence justifiée avec succès !");
            } else {
                ConsoleUtils.afficherErreur("Erreur lors de la justification.");
            }
        }

        ConsoleUtils.pause();
    }

    private static void afficherStatistiquesParClasse() {
        ConsoleUtils.afficherSousTitre("STATISTIQUES PAR CLASSE");

        Vector<Object[]> stats = absenceDAO.getStatistiquesParClasse();

        if (stats.isEmpty()) {
            ConsoleUtils.afficherInfo("Aucune donnée disponible.");
        } else {
            String[] entetes = { "Classe", "Nombre d'absences" };
            String[][] lignes = new String[stats.size()][2];

            for (int i = 0; i < stats.size(); i++) {
                lignes[i][0] = (String) stats.get(i)[1];
                lignes[i][1] = String.valueOf(stats.get(i)[2]);
            }

            ConsoleUtils.afficherTableau(entetes, lignes);
        }

        ConsoleUtils.pause();
    }

    private static void afficherStatistiquesParEtudiant() {
        ConsoleUtils.afficherSousTitre("STATISTIQUES PAR ÉTUDIANT");

        Vector<Classe> classes = classeDAO.getAll();
        if (classes.isEmpty()) {
            ConsoleUtils.afficherErreur("Aucune classe trouvée.");
            ConsoleUtils.pause();
            return;
        }

        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getLibelle());
        }

        int choix = ConsoleUtils.lireInt("Choisir une classe (numéro) : ");
        if (choix < 1 || choix > classes.size()) {
            ConsoleUtils.afficherErreur("Choix invalide !");
            ConsoleUtils.pause();
            return;
        }

        Classe classeChoisie = classes.get(choix - 1);
        Vector<Object[]> stats = absenceDAO.getStatistiquesParEtudiant(classeChoisie.getIdClasse());

        ConsoleUtils.afficherInfo("Statistiques pour la classe : " + classeChoisie.getLibelle());

        if (stats.isEmpty()) {
            ConsoleUtils.afficherInfo("Aucune donnée disponible.");
        } else {
            String[] entetes = { "Étudiant", "Nombre d'absences" };
            String[][] lignes = new String[stats.size()][2];

            for (int i = 0; i < stats.size(); i++) {
                lignes[i][0] = (String) stats.get(i)[0];
                lignes[i][1] = String.valueOf(stats.get(i)[1]);
            }

            ConsoleUtils.afficherTableau(entetes, lignes);
        }

        ConsoleUtils.pause();
    }

    private static void afficherListeAbsences(Vector<Absence> absences) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String[] entetes = { "ID", "Étudiant", "Matière", "Date", "Séance", "Statut" };
        String[][] lignes = new String[absences.size()][6];

        for (int i = 0; i < absences.size(); i++) {
            Absence a = absences.get(i);
            lignes[i][0] = String.valueOf(a.getIdAbsence());
            lignes[i][1] = a.getPrenomEtudiant() + " " + a.getNomEtudiant();
            lignes[i][2] = a.getLibelleMatiere();
            lignes[i][3] = sdf.format(a.getDateAbsence());
            lignes[i][4] = String.valueOf(a.getNumSeance());
            lignes[i][5] = a.getStatut();
        }

        ConsoleUtils.afficherTableau(entetes, lignes);
    }
}
