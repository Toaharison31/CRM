package dao;

import databases.Connexion;
import models.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    /* Convertir une ligne les parametres du contact */
    private void remplirContact(PreparedStatement ps, Contact contact) throws SQLException {
        ps.setInt(1, contact.getId_client());
        ps.setString(2, contact.getType_contact());
        ps.setString(3, contact.getNotes());
        ps.setDate(4, contact.getDate_contact());
        ps.setInt(5, contact.getCree_par());
    }
    /* Convertir une ligne ResultSet en objet Contact */
    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        return new Contact(
                rs.getInt("id"),
                rs.getInt("id_client"),
                rs.getString("type_contact"),
                rs.getString("notes"),
                rs.getDate("date_contact"),
                rs.getInt("cree_par")
        );
    }

    
    /* 1. UML: ajouterContact() -> Mampiditra contact vaovao */
    public boolean ajouterContact(Contact contact) {
        String sql = """
                INSERT INTO contact(id_client, type_contact, notes, date_contact, cree_par)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirContact(ps, contact);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du contact : " + e.getMessage());
            return false;
        }
    }

    
    /* 2. UML: voirHistorique() -> Maka ny tantaran'ny fifandraisana amin'ny mpanjifa iray */
    public List<Contact> voirHistorique(int idClient) {
        List<Contact> historique = new ArrayList<>();
        String sql = "SELECT * FROM contact WHERE id_client = ? ORDER BY date_contact DESC, id DESC";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historique.add(mapResultSetToContact(rs));
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
        }
        return historique;
    }

    
    /* 3. UML: supprimerContact() -> Mamafa contact iray */
    public boolean supprimerContact(int id) {
        String sql = "DELETE FROM contact WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du contact : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Modifier un contact */
    public boolean modifierContact(Contact contact) {
        String sql = """
                UPDATE contact SET id_client = ?, type_contact = ?, notes = ?, date_contact = ?, cree_par = ?
                WHERE id = ?
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirContact(ps, contact);
            ps.setInt(6, contact.getId());
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la modification du contact : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Rechercher par ID */
    public Contact rechercherParId(int id) {
        String sql = "SELECT * FROM contact WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToContact(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du contact : " + e.getMessage());
        }
        return null;
    }

    
    /* FANAMPIANA: Rechercher tous les contacts */
    public List<Contact> rechercherTous() {
        List<Contact> liste = new ArrayList<>();
        String sql = "SELECT * FROM contact ORDER BY id DESC";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToContact(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des contacts : " + e.getMessage());
        }
        return liste;
    }
}