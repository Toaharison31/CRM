package dao;

import databases.Connexion;
import models.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class ClientDAO {
    /* Convertir une ligne les parametre du client*/
    /* Convertir une ligne ResultSet en objet Client*/
    private void remplirClient(PreparedStatement ps, Client client) throws SQLException {
        ps.setString(1, client.getNom_complet());
        ps.setString(2, client.getEmail());
        ps.setString(3, client.getTelephone());
        ps.setString(4, client.getEntreprise());
        ps.setString(5, client.getAdresse());
        ps.setString(6, client.getStatut());
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("nom_complet"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("entreprise"),
                rs.getString("adresse"),
                rs.getString("statut"));
    }



    /*---------------------------------------------------------------*/
    /* Ajouter un nouveau client*/
    public boolean ajouterClient(Client client) {
        String sql = """
                INSERT INTO client(nom_complet, email, telephone, entreprise, adresse, statut)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirClient(ps, client);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
            return false;
        }
    }


    /*---------------------------------------------------------------*/
    /* Modifier les informations d'un client*/
    public boolean modifierClient(Client client) {
        String sql = """
                UPDATE client SET nom_complet = ?, email = ?, telephone = ?, entreprise = ?, adresse = ?, statut = ?
                WHERE id = ?
                """;
        try ( Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirClient(ps, client);
            ps.setInt(7, client.getId());
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }


    /*---------------------------------------------------------------*/
    /* Supprimer un client par son ID*/
    public boolean supprimerClient(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection conn = Connexion.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }


    /*---------------------------------------------------------------*/ 
    /*Rechercher un client par son ID*/
    public Client rechercherParId(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                 return mapResultSetToClient(rs);
            }
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }
        return null;
    }


    /*---------------------------------------------------------------*/ 
    /*Retourner tous les clients*/
    public List<Client> rechercherTous() {
        List<Client> listeClients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY id";
        try (Connection conn = Connexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listeClients.add(mapResultSetToClient(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération : " + e.getMessage());
        }

        return listeClients;
    }


    /*---------------------------------------------------------------*/ 
    /*Rechercher un ou plusieurs clients par leur nom*/
    public List<Client> rechercherParNom(String nom) {
        List<Client> listeClients = new ArrayList<>();
        String sql = """
                SELECT * FROM client
                WHERE LOWER(nom_complet) LIKE LOWER(?)
                ORDER BY nom_complet
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               listeClients.add(mapResultSetToClient(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }

        return listeClients;
    }


    /*---------------------------------------------------------------*/ 
    /*Changer le statut d'un client*/ 
    public boolean changerStatut(int id, String statut) {
        String sql = "UPDATE client SET statut = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            System.out.println("Erreur lors du changement du statut : " + e.getMessage());
            return false;
        }
    }
}