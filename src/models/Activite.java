package models;

import java.sql.Timestamp;
import java.sql.Date;

/* ACTIVITE CLASS */
public class Activite {
    private int id;
    private int id_utilisateur;
    private int id_opportunite;
    private String type_activite;
    private String description;
    private Date date_echeance;
    private String statut;
    private Timestamp cree_le;

    /* ACTIVITE CONSTRUCTOR */
    public Activite(int id, int id_utilisateur, int id_opportunite, String type_activite, String description,
            Date date_echeance, String statut, Timestamp cree_le) {
        this.id = id;
        this.id_utilisateur = id_utilisateur;
        this.id_opportunite = id_opportunite;
        this.type_activite = type_activite;
        this.description = description;
        this.date_echeance = date_echeance;
        this.statut = statut;
        this.cree_le = cree_le;
    }

    // GETTERS and SETTERS for Activite
    /* Activite id getter and setter */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* Activite id_utilisateur getter and setter */
    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    /* Activite id_opportunite getter and setter */
    public int getId_opportunite() {
        return id_opportunite;
    }

    public void setId_opportunite(int id_opportunite) {
        this.id_opportunite = id_opportunite;
    }

    /* Activite type_activite getter and setter */
    public String getType_activite() {
        return type_activite;
    }

    public void setType_activite(String type_activite) {
        this.type_activite = type_activite;
    }

    /* Activite description getter and setter */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* Activite date_echeance getter and setter */
    public Date getDate_echeance() {
        return date_echeance;
    }

    public void setDate_echeance(Date date_echeance) {
        this.date_echeance = date_echeance;
    }

    /* Activite statut getter and setter */
    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    /* Activite cree_le getter and setter */
    public Timestamp getCree_le() {
        return cree_le;
    }

    public void setCree_le(Timestamp cree_le) {
        this.cree_le = cree_le;
    }

    /************************************************************************************************* */
    // Activite toString method
    /************************************************************************************************* */
    @Override
    public String toString() {
        return "Activite { id=" + id + ", id_utilisateur=" + id_utilisateur + ", id_opportunite=" + id_opportunite
                + ", type_activite=" + type_activite + ", description=" + description + ", date_echeance="
                + date_echeance + ", statut=" + statut + ", cree_le=" + cree_le + " }";
    }
}