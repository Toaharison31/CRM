package dao;

import databases.Connexion;
import models.Activite;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivateDAO {
    /* Convertir une ligne les parametre*/
    /* Convertir une ligne ResultSet en objet*/
    private void remplirActivite(PreparedStatement ps, Activite activite)throws SQLException {
        ps.setInt(1, activite.getId_utilisateur());
        ps.setInt(2, activite.getId_opportunite());
        ps.setString(3, activite.getType_activite());
        ps.setString(4, activite.getDescription());
        ps.setDate(5, (Date) activite.getDate_echeance());
        ps.setString(6, activite.getStatut());
    }

    private Activite mapResultSetToActivite(ResultSet rs)throws SQLException {
        return new Activite(
                rs.getInt("id"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_opportunite"),
                rs.getString("type_activite"),
                rs.getString("description"),
                rs.getDate("date_echeance"),
                rs.getString("statut"),
                rs.getTimestamp("cree_le"));
    }



    /*---------------------------------------------------------------*/
    /* Ajouter activiter*/
    public boolean ajouterActivite(Activite activite) {
        String sql = """
                INSERT INTO activite (id_utilisateur, id_opportunite, type_activite, description, date_echeance, statut)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirActivite(ps, activite);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
            return false;
        }
    }


    /*---------------------------------------------------------------*/
    /* Modifier activite*/
    public boolean modifierActivite(Activite activite) {
        String sql = """
                UPDATE activite
                SET id_utilisateur = ?, id_opportunite = ?, type_activite = ?, description = ?, date_echeance = ?, statut = ?
                WHERE id = ?
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirActivite(ps, activite);
            ps.setInt(7, activite.getId());
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }


    /*---------------------------------------------------------------*/
    /* Supprimer activiter*/
    public boolean supprimerActivite(int id) {
        String sql = "DELETE FROM activite WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }


    /*---------------------------------------------------------------*/ 
    /*Rechercher par ID*/
    public Activite rechercherParId(int id) {
        String sql = "SELECT * FROM activite WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToActivite(rs);
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return null;
    }


    /*---------------------------------------------------------------*/ 
    /*Retourner tous*/
    public List<Activite> rechercherToutes() {
        List<Activite> liste = new ArrayList<>();
        String sql = "SELECT * FROM activite ORDER BY id";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToActivite(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return liste;
    }


    /*---------------------------------------------------------------*/ 
    /*Rechercher un ou plusieurs activiter*/
    public List<Activite> rechercherParOpportunite(int idOpportunite) {
        List<Activite> liste = new ArrayList<>();
        String sql = """
                SELECT *
                FROM activite
                WHERE id_opportunite = ?
                ORDER BY date_echeance
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOpportunite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste.add(mapResultSetToActivite(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return liste;
    }


    /*---------------------------------------------------------------*/ 
    /*Changer le statut */ 
    public boolean changerStatut(int id, String statut) {
        String sql = "UPDATE activite SET statut = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
            return false;
        }
    }

}