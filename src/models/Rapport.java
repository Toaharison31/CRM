package models;

import java.sql.Timestamp;

/* RAPPORT CLASS */
public class Rapport {
    private int id;
    private String type_rapport;
    private int genere_par;
    private String donnees;
    private Timestamp cree_le;

    public Rapport() {}
    public Rapport(int id, String type_rapport, int genere_par, String donnees, Timestamp cree_le) {
        this.id = id;
        this.type_rapport = type_rapport;
        this.genere_par = genere_par;
        this.donnees = donnees;
        this.cree_le = cree_le;
    }
    public Rapport(String type_rapport, int genere_par, String donnees, Timestamp cree_le) {
        this.type_rapport = type_rapport;
        this.genere_par = genere_par;
        this.donnees = donnees;
        this.cree_le = cree_le;
    }

    
    /*  getters and setters */
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getType_rapport() {return type_rapport;}
    public void setType_rapport(String type_rapport) {this.type_rapport = type_rapport;}

    public int getGenere_par() {return genere_par;}
    public void setGenere_par(int genere_par) {this.genere_par = genere_par;}

    public String getDonnees() {return donnees;}
    public void setDonnees(String donnees) {this.donnees = donnees;}

    public Timestamp getCree_le() {return cree_le;}
    public void setCree_le(Timestamp cree_le) {this.cree_le = cree_le;}
    

    // Rapport toString method
    @Override
    public String toString() {
        return "Rapport { id=" + id + ", type_rapport=" + type_rapport + ", genere_par=" + genere_par
                + ", donnees=" + donnees + ", cree_le=" + cree_le + " }";
    }
}