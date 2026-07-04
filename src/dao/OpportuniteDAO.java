package dao;

import databases.Connexion;
import models.Opportunite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OpportuniteDAO {
    /* Convertir une ligne les parametres de l'opportunite */
    private void remplirOpportunite(PreparedStatement ps, Opportunite opp) throws SQLException {
        ps.setInt(1, opp.getId_client());
        ps.setString(2, opp.getTitre());
        ps.setDouble(3, opp.getValeur());
        ps.setString(4, opp.getEtape());
        ps.setDate(5, opp.getDate_cloture());
        ps.setInt(6, opp.getCree_par());
    }
    /* Convertir une ligne ResultSet en objet Opportunite */
    private Opportunite mapResultSetToOpportunite(ResultSet rs) throws SQLException {
        return new Opportunite(
                rs.getInt("id"),
                rs.getInt("id_client"),
                rs.getString("titre"),
                rs.getDouble("valeur"),
                rs.getString("etape"),
                rs.getDate("date_cloture"),
                rs.getTimestamp("cree_le"),
                rs.getInt("cree_par")
        );
    }

    
    /* 1. UML: creerOpportunite() -> Mampiditra opportunite vaovao */
    public boolean creerOpportunite(Opportunite opp) {
        String sql = """
                INSERT INTO opportunite(id_client, titre, valeur, etape, date_cloture, cree_par)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirOpportunite(ps, opp);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'opportunité : " + e.getMessage());
            return false;
        }
    }

    
    /* 2. UML: changerEtape() -> Manova ny etape (prospect, qualifié, gagné, ...) */
    public boolean changerEtape(int id, String etape) {
        String sql = "UPDATE opportunite SET etape = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, etape);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors du changement d'étape : " + e.getMessage());
            return false;
        }
    }

    
    /* 3. UML: calculerValeurTotal() -> Mikajy ny fitambaran'ny vola rehetra */
    public double calculerValeurTotal() {
        String sql = "SELECT SUM(valeur) AS total FROM opportunite";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors du calcul de la valeur totale : " + e.getMessage());
        }
        return 0.0;
    }

    
    /* 4. UML: listerParStatut() -> Maka ny opportunités isaky ny etape voatondro */
    public List<Opportunite> listerParEtape(String etape) {
        List<Opportunite> liste = new ArrayList<>();
        String sql = "SELECT * FROM opportunite WHERE etape = ? ORDER BY id DESC";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, etape);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSetToOpportunite(rs));
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération par étape : " + e.getMessage());
        }
        return liste;
    }

    
    /* FANAMPIANA: Modifier une opportunité */
    public boolean modifierOpportunite(Opportunite opp) {
        String sql = """
                UPDATE opportunite SET id_client = ?, titre = ?, valeur = ?, etape = ?, date_cloture = ?, cree_par = ?
                WHERE id = ?
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirOpportunite(ps, opp);
            ps.setInt(7, opp.getId());
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'opportunité : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Supprimer une opportunité */
    public boolean supprimerOpportunite(int id) {
        String sql = "DELETE FROM opportunite WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'opportunité : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Rechercher par ID */
    public Opportunite rechercherParId(int id) {
        String sql = "SELECT * FROM opportunite WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOpportunite(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'opportunité : " + e.getMessage());
        }
        return null;
    }

    
    /* FANAMPIANA: Rechercher toutes les opportunités */
    public List<Opportunite> rechercherTous() {
        List<Opportunite> liste = new ArrayList<>();
        String sql = "SELECT * FROM opportunite ORDER BY id DESC";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToOpportunite(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des opportunités : " + e.getMessage());
        }
        return liste;
    }
}