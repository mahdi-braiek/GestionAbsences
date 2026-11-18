package model;

public class Classe {

    private int idClasse;
    private String libelle;
    private String niveau;
    private String filiere;

    public Classe() {
    }

    public Classe(int idClasse, String libelle, String niveau, String filiere) {
        this.idClasse = idClasse;
        this.libelle = libelle;
        this.niveau = niveau;
        this.filiere = filiere;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    @Override
    public String toString() {
        return libelle + " (" + filiere + ")";
    }
}
