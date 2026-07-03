package models;

import java.sql.Timestamp;

/* UTILISATEUR CLASS */
public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String mot_de_passe;
    private String role; 
    private Timestamp cree_le;

    /* UTILISATEUR CONSTRUCTOR */
    public Utilisateur(int id,String nom, String email, String mot_de_passe, String role, Timestamp cree_le) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.role = role;
        this.cree_le = cree_le;
    }

    // GETTERS and SETTERS for Utilisateur
    /* Utilisateur id getter and setter */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* Utilisateur nom getter and setter */
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /* Utilisateur email getter and setter */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* Utilisateur mot_de_passe getter and setter */
    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    /* Utilisateur role getter and setter */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /* Utilisateur cree_le getter and setter */
    public Timestamp getCree_le() {
        return cree_le;
    }

    public void setCree_le(Timestamp cree_le) {
        this.cree_le = cree_le;
    }

    /************************************************************************************************* */
    /* Utilisateur toString method */
    /************************************************************************************************* */
    @Override
    public String toString() {
        return "Utilisateur { id=" + id + ", nom='" + nom + "', email='" + email + "', mot_de_passe='" + mot_de_passe + "', role=" + role + ", cree_le=" + cree_le + " }";
    }

    /************************************************************************************************* */
    /*INSERT INTO DATABASE gestion_crm */
    /************************************************************************************************* */
    public String insertUtilisateur() {

        // Requête SQL pour insérer un utilisateur dans la table utilisateur
        String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, role, cree_le) VALUES (?, ?, ?, ?, ?);";

        // Utilisation de la connexion à la base de données pour exécuter la requête d'insertion
        try {
            java.sql.Connection conn = databases.Connexion.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

            // Valeurs à insérer dans la requête préparée
            pstmt.setString(1, this.nom);
            pstmt.setString(2, this.email);
            pstmt.setString(3, this.mot_de_passe);
            pstmt.setString(4, this.role);
            pstmt.setTimestamp(5, this.cree_le);

            // Executer la requête d'insertion
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            // Retourner un message de succès avec les détails de l'utilisateur inséré
            return "Utilisateur enregistré avec succès : " + this.toString();
        } 
        // Gestion des exceptions SQL
        catch (java.sql.SQLException e) {
            return "Error inserting utilisateur: " + e.getMessage();
        }
    }
}