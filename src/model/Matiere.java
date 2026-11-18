package model;

public class Matiere {

    private int idMatiere;
    private String libelle;

    public Matiere() {
    }

    public Matiere(int idMatiere, String libelle) {
        this.idMatiere = idMatiere;
        this.libelle = libelle;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
