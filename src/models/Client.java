package models;

/*CLASSE CLIENT */
public class Client {
    private int id;
    private String nom_complet;
    private String email;
    private String telephone;
    private String entreprise;
    private String adresse;
    private String statut;

    /************************************************************************************************* */
    /*CREATIOIN DES CONSTRUCTEURS */
    public Client(int id, String nom_complet, String email, String telephone, String entreprise, String adresse,
            String statut) {
        this.id = id;
        this.nom_complet = nom_complet;
        this.email = email;
        this.telephone = telephone;
        this.entreprise = entreprise;
        this.adresse = adresse;
        this.statut = statut;
    }

    /************************************************************************************************* */
    // GETING METHOD
    public int getId() {
        return id;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getStatut() {
        return statut;
    }

    /************************************************************************************************* */
    /* SETTING METHOD */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }

    /************************************************************************************************* */
    // AFICHAGE
    @Override
    public String toString() {
        return "Client { id=" + id + ", nom='" + nom_complet + "', email='" + email + "', téléphone='" + telephone
                + "', entreprise= '" + entreprise + "', adresse= '" + adresse + "', statut= '" + statut + "' }";
    }

}
