package models;

import java.util.Date;
import org.w3c.dom.Text;

/*CONTACT CLASS */
public class Contact {
    private int id;
    private int id_client;
    private char type_contact;;
    private Text notes;
    private Date date_contact;
    private int cree_par;

    public Contact() {
    }

    /* CONTACT CONSTRUCTOR */
    public Contact(int id, int id_client, char type_contact, Text notes, Date date_contact, int cree_par) {
        this.id = id;
        this.id_client = id_client;
        this.type_contact = type_contact;
        this.notes = notes;
        this.date_contact = date_contact;
        this.cree_par = cree_par;
    }

    // GETTERS and SETTERS for Contact
    /* Contact id getter and setter */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* Contact nom_complet getter and setter */
    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    /* Contact type_contact getter and setter */
    public char getType_contact() {
        return type_contact;
    }

    public void setType_contact(char type_contact) {
        this.type_contact = type_contact;
    }

    /* Contact notes getter and setter */
    public Text getNotes() {
        return notes;
    }

    public void setNotes(Text notes) {
        this.notes = notes;
    }

    /* Contact date_contact getter and setter */
    public Date getDate_contact() {
        return date_contact;
    }

    public void setDate_contact(Date date_contact) {
        this.date_contact = date_contact;
    }

    /* Contact cree_par getter and setter */
    public int getCree_par() {
        return cree_par;
    }

    public void setCree_par(int cree_par) {
        this.cree_par = cree_par;
    }

    /************************************************************************************************* */
    // Contact toString method
    /************************************************************************************************* */
    @Override
    public String toString() {
        return "Contact { id=" + id + ", id_client=" + id_client + ", type_contact=" + type_contact + ", notes=" + notes
                + ", date_contact=" + date_contact + ", cree_par=" + cree_par + " }";
    }
}