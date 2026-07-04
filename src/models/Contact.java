package models;
import java.sql.Date;


/*CONTACT CLASS */
public class Contact {
    private int id;
    private int id_client;
    private String type_contact;
    private String notes;
    private Date date_contact;
    private int cree_par;

    public Contact() {}
    public Contact(int id, int id_client, String type_contact, String notes, Date date_contact, int cree_par) {
        this.id = id;
        this.id_client = id_client;
        this.type_contact = type_contact;
        this.notes = notes;
        this.date_contact = date_contact;
        this.cree_par = cree_par;
    }
    public Contact(int id_client, String type_contact, String notes, Date date_contact, int cree_par) {
        this.id_client = id_client;
        this.type_contact = type_contact;
        this.notes = notes;
        this.date_contact = date_contact;
        this.cree_par = cree_par;
    }

    //methede get & set
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getId_client() {return id_client;}
    public void setId_client(int id_client) {this.id_client = id_client;}

    public String getType_contact() {return type_contact;}
    public void setType_contact(String type_contact) {this.type_contact = type_contact;}

    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}

    public Date getDate_contact() {return date_contact;}
    public void setDate_contact(Date date_contact) {this.date_contact = date_contact;}

    public int getCree_par() {return cree_par;}
    public void setCree_par(int cree_par) {this.cree_par = cree_par;}


    // Contact toString method
    @Override
    public String toString() {
        return "Contact { id=" + id + ", id_client=" + id_client + ", type_contact=" + type_contact + ", notes=" + notes
                + ", date_contact=" + date_contact + ", cree_par=" + cree_par + " }";
    }
}