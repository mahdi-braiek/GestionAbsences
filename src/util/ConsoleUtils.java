package util;

import java.util.Scanner;

public class ConsoleUtils {

    private static Scanner scanner = new Scanner(System.in);
    public static void afficherTitre(String titre) {
        String ligne = "=".repeat(titre.length() + 4);
        System.out.println();
        System.out.println("+" + ligne + "+");
        System.out.println("|  " + titre + "  |");
        System.out.println("+" + ligne + "+");
        System.out.println();
    }

    public static void afficherSousTitre(String sousTitre) {
        System.out.println();
        System.out.println("--- " + sousTitre + " ---");
        System.out.println();
    }

    public static void afficherSucces(String message) {
        System.out.println("[OK] " + message);
    }

    public static void afficherErreur(String message) {
        System.out.println("[ERREUR] " + message);
    }

    public static void afficherInfo(String message) {
        System.out.println("[i] " + message);
    }

    public static void simulerEnvoiMail(String destinataire, String message) {
        System.out.println();
        System.out.println("+==========================================+");
        System.out.println("|        SIMULATION ENVOI EMAIL            |");
        System.out.println("+==========================================+");
        System.out.println("| A: " + destinataire);
        System.out.println("| Message: " + message);
        System.out.println("+==========================================+");
        System.out.println("[OK] Mail d'alerte envoye a l'etudiant.");
        System.out.println();
    }

    public static void afficherSeparateur() {
        System.out.println("--------------------------------------------");
    }

    public static String lireString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static int lireInt(String message) {
        System.out.print(message);
        try {
            int valeur = Integer.parseInt(scanner.nextLine().trim());
            return valeur;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void pause() {
        System.out.println();
        System.out.print("Appuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    public static void effacerEcran() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    public static void afficherTableau(String[] entetes, String[][] lignes) {
        int[] largeurs = new int[entetes.length];
        for (int i = 0; i < entetes.length; i++) {
            largeurs[i] = entetes[i].length();
        }
        for (String[] ligne : lignes) {
            for (int i = 0; i < ligne.length && i < largeurs.length; i++) {
                if (ligne[i] != null && ligne[i].length() > largeurs[i]) {
                    largeurs[i] = ligne[i].length();
                }
            }
        }
        StringBuilder separateur = new StringBuilder("+");
        StringBuilder enteteLigne = new StringBuilder("|");
        for (int i = 0; i < entetes.length; i++) {
            separateur.append("-".repeat(largeurs[i] + 2)).append("+");
            enteteLigne.append(" ").append(String.format("%-" + largeurs[i] + "s", entetes[i])).append(" |");
        }

        System.out.println(separateur);
        System.out.println(enteteLigne);
        System.out.println(separateur);

        for (String[] ligne : lignes) {
            StringBuilder dataLigne = new StringBuilder("|");
            for (int i = 0; i < entetes.length; i++) {
                String valeur = (i < ligne.length && ligne[i] != null) ? ligne[i] : "";
                dataLigne.append(" ").append(String.format("%-" + largeurs[i] + "s", valeur)).append(" |");
            }
            System.out.println(dataLigne);
        }
        System.out.println(separateur);
    }
}
