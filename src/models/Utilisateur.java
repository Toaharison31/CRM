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

    public Utilisateur () {}
    public Utilisateur(int id, String nom, String email, String mot_de_passe, String role, Timestamp cree_le) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.role = role;
        this.cree_le = cree_le;
    }
    public Utilisateur(String nom, String email, String mot_de_passe, String role, Timestamp cree_le) {
        this.nom = nom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.role = role;
        this.cree_le = cree_le;
    }

    /* getter and setter */
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getMot_de_passe() {return mot_de_passe;}
    public void setMot_de_passe(String mot_de_passe) {this.mot_de_passe = mot_de_passe;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    public Timestamp getCree_le() {return cree_le;}
    public void setCree_le(Timestamp cree_le) {this.cree_le = cree_le;}


    /* Utilisateur toString method */
    @Override
    public String toString() {
        return "Utilisateur { id=" + id + ", nom='" + nom + "', email='" + email + "', mot_de_passe='" + mot_de_passe + "', role=" + role + ", cree_le=" + cree_le + " }";
    }

}