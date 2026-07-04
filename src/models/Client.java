package models;

/* CLIENT CLASS */
public class Client {
    private int id;
    private String nom_complet;
    private String email;
    private String telephone;
    private String entreprise;
    private String adresse;
    private String statut;

    public Client () {}
    public Client(int id, String nom_complet, String email, String telephone, String entreprise, String adresse, String statut) {
        this.id = id;
        this.nom_complet = nom_complet;
        this.email = email;
        this.telephone = telephone;
        this.entreprise = entreprise;
        this.adresse = adresse;
        this.statut = statut;
    }
    public Client(String nom_complet, String email, String telephone, String entreprise, String adresse, String statut) {
        this.nom_complet = nom_complet;
        this.email = email;
        this.telephone = telephone;
        this.entreprise = entreprise;
        this.adresse = adresse;
        this.statut = statut;
    }


    /* getter and setter */
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNom_complet() {return nom_complet;}
    public void setNom_complet(String nom_complet) {this.nom_complet = nom_complet;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getTelephone() {return telephone;}
    public void setTelephone(String telephone) {this.telephone = telephone;}

    public String getEntreprise() {return entreprise;}
    public void setEntreprise(String entreprise) {this.entreprise = entreprise;}

    public String getAdresse() {return adresse;}
    public void setAdresse(String adresse) {this.adresse = adresse;}

    public String getStatut() {return statut;}
    public void setStatut(String statut) {this.statut = statut;}

    // Client toString method
    @Override
    public String toString() {
        return "Client { id=" + id + ", nom='" + nom_complet + "', email='" + email + "', téléphone='" + telephone
                + "', entreprise= '" + entreprise + "', adresse= '" + adresse + "', statut= '" + statut + "' }";
    }

}
