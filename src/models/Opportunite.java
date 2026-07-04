package models;

import java.sql.Timestamp;
import java.sql.Date;

/* OPPORTUNITE CLASS */
public class Opportunite {
    private int id;
    private int id_client;
    private String titre;
    private double valeur; 
    private String etape;  
    private Date date_cloture;
    private Timestamp cree_le;
    private int cree_par;

    public Opportunite() {}
    public Opportunite(int id, int id_client, String titre, double valeur, String etape, Date date_cloture, Timestamp cree_le, int cree_par) {
        this.id = id;
        this.id_client = id_client;
        this.titre = titre;
        this.valeur = valeur;
        this.etape = etape;
        this.date_cloture = date_cloture;
        this.cree_le = cree_le;
        this.cree_par = cree_par;
    }
    public Opportunite(int id_client, String titre, double valeur, String etape, Date date_cloture, int cree_par) {
        this.id_client = id_client;
        this.titre = titre;
        this.valeur = valeur;
        this.etape = etape;
        this.date_cloture = date_cloture;
        this.cree_par = cree_par;
    }

    /* getters and setters */
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getId_client() {return id_client;}
    public void setId_client(int id_client) {this.id_client = id_client;}

    public String getTitre() {return titre; }
    public void setTitre(String titre) {this.titre = titre;}

    public double getValeur() {return valeur;}
    public void setValeur(double valeur) {this.valeur = valeur;}

    public String getEtape() {return etape;}
    public void setEtape(String etape) {this.etape = etape;}

    public Date getDate_cloture() {return date_cloture;}
    public void setDate_cloture(Date date_cloture) {this.date_cloture = date_cloture;}

    public Timestamp getCree_le() {return cree_le;}
    public void setCree_le(Timestamp cree_le) {this.cree_le = cree_le;}

    public int getCree_par() {return cree_par;}
    public void setCree_par(int cree_par) {this.cree_par = cree_par;}

    // Opportunite toString method
    @Override
    public String toString() {
        return "Opportunite { id=" + id + ", id_client=" + id_client + ", titre='" + titre + "', valeur=" + valeur
                + ", etape='" + etape + "', date_cloture=" + date_cloture + ", cree_le=" + cree_le + ", cree_par="
                + cree_par + " }";
    }
}