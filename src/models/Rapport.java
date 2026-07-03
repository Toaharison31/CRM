package models;

import java.sql.Timestamp;
import org.json.JSONObject;

/* RAPPORT CLASS */
public class Rapport {
    private int id;
    private String type_rapport;
    private int genere_par;
    private JSONObject donnees;
    private Timestamp cree_le;

    /* RAPPORT CONSTRUCTOR */
    public Rapport(int id, String type_rapport, int genere_par, JSONObject donnees, Timestamp cree_le) {
        this.id = id;
        this.type_rapport = type_rapport;
        this.genere_par = genere_par;
        this.donnees = donnees;
        this.cree_le = cree_le;
    }

    /* GETTERS AND SETTERS for Rapport */
    /* Rapport id getters and setters */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* Rapport type_rapport getters and setters */
    public String getType_rapport() {
        return type_rapport;
    }

    public void setType_rapport(String type_rapport) {
        this.type_rapport = type_rapport;
    }

    /* Rapport genere_par getters and setters */
    public int getGenere_par() {
        return genere_par;
    }

    public void setGenere_par(int genere_par) {
        this.genere_par = genere_par;
    }

    /* Rapport donnees getters and setters */
    public JSONObject getDonnees() {
        return donnees;
    }

    public void setDonnees(JSONObject donnees) {
        this.donnees = donnees;
    }

    /* Rapport cree_le getters and setters */
    public Timestamp getCree_le() {
        return cree_le;
    }

    public void setCree_le(Timestamp cree_le) {
        this.cree_le = cree_le;
    }

    /************************************************************************************************* */
    // Rapport toString method
    /************************************************************************************************* */
    @Override
    public String toString() {
        return "Rapport { id=" + id + ", type_rapport=" + type_rapport + ", genere_par=" + genere_par
                + ", donnees=" + donnees + ", cree_le=" + cree_le + " }";
    }

    /************************************************************************************************* */
    // INSERT INTO RAPPORT TABLE
    /************************************************************************************************* */
    public String insertRapport() {
        // Requête SQL pour insérer un rapport dans la base de données
        String sql = "INSERT INTO rapport (type_rapport, genere_par, donnees, cree_le) VALUES (?, ?, ?, ?)";

        // Exécuter la requête SQL pour insérer le rapport dans la base de données
        try {

            // Utilisation de la connexion à la base de données pour exécuter la requête
            // d'insertion
            java.sql.Connection conn = databases.Connexion.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

            // Définir les paramètres de la requête SQL
            pstmt.setString(1, this.type_rapport);
            pstmt.setInt(2, this.genere_par);
            pstmt.setString(3, this.donnees.toString());
            pstmt.setTimestamp(4, this.cree_le);

            // Exécuter la requête d'insertion
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        }
        // Excéption handling
        catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

}
