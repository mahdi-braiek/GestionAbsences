package model;

import java.util.Date;

public class Absence {

    private int idAbsence;
    private int idEtudiant;
    private int idEnseignant;
    private int idMatiere;
    private int numSeance;
    private Date dateAbsence;
    private boolean justifiee;

    private String nomEtudiant;
    private String prenomEtudiant;
    private String libelleMatiere;
    private String nomEnseignant;

    public Absence() {
    }

    public Absence(int idAbsence, int idEtudiant, int idEnseignant, int idMatiere,
            int numSeance, Date dateAbsence, boolean justifiee) {
        this.idAbsence = idAbsence;
        this.idEtudiant = idEtudiant;
        this.idEnseignant = idEnseignant;
        this.idMatiere = idMatiere;
        this.numSeance = numSeance;
        this.dateAbsence = dateAbsence;
        this.justifiee = justifiee;
    }

    public int getIdAbsence() {
        return idAbsence;
    }

    public void setIdAbsence(int idAbsence) {
        this.idAbsence = idAbsence;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public int getNumSeance() {
        return numSeance;
    }

    public void setNumSeance(int numSeance) {
        this.numSeance = numSeance;
    }

    public Date getDateAbsence() {
        return dateAbsence;
    }

    public void setDateAbsence(Date dateAbsence) {
        this.dateAbsence = dateAbsence;
    }

    public boolean isJustifiee() {
        return justifiee;
    }

    public void setJustifiee(boolean justifiee) {
        this.justifiee = justifiee;
    }

    public String getNomEtudiant() {
        return nomEtudiant;
    }

    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }

    public String getPrenomEtudiant() {
        return prenomEtudiant;
    }

    public void setPrenomEtudiant(String prenomEtudiant) {
        this.prenomEtudiant = prenomEtudiant;
    }

    public String getLibelleMatiere() {
        return libelleMatiere;
    }

    public void setLibelleMatiere(String libelleMatiere) {
        this.libelleMatiere = libelleMatiere;
    }

    public String getNomEnseignant() {
        return nomEnseignant;
    }

    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    public String getStatut() {
        return justifiee ? "Justifiée" : "Non justifiée";
    }

    @Override
    public String toString() {
        return "Absence{" +
                "idAbsence=" + idAbsence +
                ", etudiant=" + prenomEtudiant + " " + nomEtudiant +
                ", matiere='" + libelleMatiere + '\'' +
                ", date=" + dateAbsence +
                ", seance=" + numSeance +
                ", justifiee=" + justifiee +
                '}';
    }
}
