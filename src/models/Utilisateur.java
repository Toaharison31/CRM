package models;

import java.sql.Timestamp;

/* UTILISATEUR CLASS */
public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String mot_de_passe;
    private char role; 
    private Timestamp cree_le;

    /* UTILISATEUR CONSTRUCTOR */
    public Utilisateur(int id, String nom, String email, String mot_de_passe, char role, Timestamp cree_le) {
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
    public char getRole() {
        return role;
    }

    public void setRole(char role) {
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

}