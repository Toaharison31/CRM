package dao;

import database.Connexion;
import models.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/*---------------------------------------------------------------*/
/* Ajouter un nouveau client*/
/*---------------------------------------------------------------*/
public class ClientDAO {
    public boolean ajouterClient(Client client) {
        String sql = """
                INSERT INTO client(nom_complet, email, telephone, entreprise, adresse, statut)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getNom_complet());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEntreprise());
            ps.setString(5, client.getAdresse());
            ps.setString(6, client.getStatut());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
            return false;
        }
    }
}



/*---------------------------------------------------------------*/
/* Modifier les informations d'un client*/
/*---------------------------------------------------------------*/
public boolean modifierClient(Client client) {
    String sql = """
            UPDATE client SET nom_complet = ?, email = ?, telephone = ?, entreprise = ?, adresse = ?, statut = ?
            WHERE id = ?
            """;

    try ( Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, client.getNom_complet());
        ps.setString(2, client.getEmail());
        ps.setString(3, client.getTelephone());
        ps.setString(4, client.getEntreprise());
        ps.setString(5, client.getAdresse());
        ps.setString(6, client.getStatut());
        ps.setInt(7, client.getId());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Erreur lors de la modification : " + e.getMessage());
        return false;
    }
}



/*---------------------------------------------------------------*/
/* Supprimer un client par son ID*/
/*---------------------------------------------------------------*/
public boolean supprimerClient(int id) {
    String sql = "DELETE FROM client WHERE id = ?";
    try (Connection conn = Connexion.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Erreur lors de la suppression : " + e.getMessage());
        return false;
    }
}