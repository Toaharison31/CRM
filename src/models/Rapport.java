package models;

import java.sql.Timestamp;
import org.json.JSONObject;

/* RAPPORT CLASS */
public class Rapport {
    private int id;
    private char type_rapport;
    private int genere_par;
    private JSONObject donnees;
    private Timestamp cree_le;

    /* RAPPORT CONSTRUCTOR */
    public Rapport(int id, char type_rapport, int genere_par, JSONObject donnees, Timestamp cree_le) {
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
    public char getType_rapport() {
        return type_rapport;
    }

    public void setType_rapport(char type_rapport) {
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
}