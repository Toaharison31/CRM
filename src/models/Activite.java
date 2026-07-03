package models;

import java.sql.Timestamp;

/* ACTIVITE CLASS */
public class Activite {
    private int id;
    private int id_utilisateur;
    private int id_opportunite;
    private String type_activite;
    private String description;
    private String date_echeance;
    private String statut;
    private Timestamp cree_le;

    /* ACTIVITE CONSTRUCTOR */
    public Activite(int id, int id_utilisateur, int id_opportunite, String type_activite, String description,
            String date_echeance, String statut, Timestamp cree_le) {
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
    public String getDate_echeance() {
        return date_echeance;
    }

    public void setDate_echeance(String date_echeance) {
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

    /************************************************************************************************* */
    // INSERT INTO ACTIVITE TABLE
    /************************************************************************************************* */
    public String insertActivite() {

        // Requete SQL pour insérer une nouvelle activité dans la table "activite"
        String sql = "INSERT INTO activite (id_utilisateur, id_opportunite, type_activite, description, date_echeance, statut, cree_le) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Utilisation de la connexion à la base de données pour exécuter la requête
        // d'insertion
        try {

            java.sql.Connection conn = databases.Connexion.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

            // Valeurs à insérer dans la requête préparée
            pstmt.setInt(1, this.id_utilisateur);
            pstmt.setInt(2, this.id_opportunite);
            pstmt.setString(3, this.type_activite);
            pstmt.setString(4, this.description.toString());
            pstmt.setString(5, this.date_echeance);
            pstmt.setString(6, this.statut);
            pstmt.setTimestamp(7, this.cree_le);

            // Exécuter la requête d'insertion
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            // Retourner la requête SQL pour référence
            return "Activité insérée avec succès : " + this.toString();

        }
        // Gestion des exceptions
        catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }
}