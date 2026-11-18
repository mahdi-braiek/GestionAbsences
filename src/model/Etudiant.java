package model;

public class Etudiant extends Utilisateur {
    
    private int idClasse;
    
    public Etudiant() {
        super();
    }
    
    public Etudiant(int id, String nom, String prenom, String login, String pwd, int idClasse) {
        super(id, nom, prenom, login, pwd);
        this.idClasse = idClasse;
    }
    
    public int getIdClasse() {
        return idClasse;
    }
    
    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }
    
    @Override
    public String getRole() {
        return "ETUDIANT";
    }
    
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
