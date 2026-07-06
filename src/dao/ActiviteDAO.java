package dao;

import databases.Connexion;
import models.Activite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActiviteDAO {
    /* Convertir une ligne les parametres de l'activite */
    private void remplirActivite(PreparedStatement ps, Activite activite) throws SQLException {
        ps.setInt(1, activite.getId_utilisateur());
        ps.setInt(2, activite.getId_opportunite());
        ps.setString(3, activite.getType_activite());
        ps.setString(4, activite.getDescription());
        ps.setDate(5, activite.getDate_echeance());
        ps.setString(6, activite.getStatut());
    }
    /* Convertir une ligne ResultSet en objet Activite */
    private Activite mapResultSetToActivite(ResultSet rs) throws SQLException {
        return new Activite(
                rs.getInt("id"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_opportunite"),
                rs.getString("type_activite"),
                rs.getString("description"),
                rs.getDate("date_echeance"),
                rs.getString("statut"),
                rs.getTimestamp("cree_le")
        );
    }

   
    /* 1. UML: creerActivite() -> Mampiditra activite vaovao */
    public boolean creerActivite(Activite activite) {
        String sql = """
                INSERT INTO activite(id_utilisateur, id_opportunite, type_activite, description, date_echeance, statut)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirActivite(ps, activite);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'activité : " + e.getMessage());
            return false;
        }
    }

    
    /* 2. UML: marquerTermine() -> Manova statut ho 'terminé' */
    public boolean marquerTermine(int id) {
        String sql = "UPDATE activite SET statut = 'terminé' WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors du marquage comme terminé : " + e.getMessage());
            return false;
        }
    }

    
    /* 3. UML: listerEnAttente() -> Maka ny asa mbola 'en attente' */
    public List<Activite> listerEnAttente() {
        List<Activite> liste = new ArrayList<>();
        String sql = "SELECT * FROM activite WHERE statut = 'En attente' ORDER BY date_echeance ASC";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToActivite(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des activités en attente : " + e.getMessage());
        }
        return liste;
    }

    
    /* 4. UML: relancer() -> Mamorona Contact vaovao avy amin'ny Activite iray */
    public boolean relancer(int idActivite, String typeContact, String notes, int creePar) {
        String sqlSelect = """
                SELECT o.id_client FROM activite a
                JOIN opportunite o ON a.id_opportunite = o.id
                WHERE a.id = ?
                """;
        String sqlInsertContact = """
                INSERT INTO contact(id_client, type_contact, notes, date_contact, cree_par)
                VALUES (?, ?, ?, CURRENT_DATE, ?)
                """;
        
        try (Connection conn = Connexion.getConnection()) {
            int idClient = -1;
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setInt(1, idActivite);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        idClient = rs.getInt("id_client");
                    }
                }
            }
            
            if (idClient != -1) {
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertContact)) {
                    psInsert.setInt(1, idClient);
                    psInsert.setString(2, typeContact); // 'appel' na 'email'
                    psInsert.setString(3, notes);
                    psInsert.setInt(4, creePar);
                    return psInsert.executeUpdate() > 0;
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la relance : " + e.getMessage());
        }
        return false;
    }


    /*  FANAMPIANA: Modifier une activité */
    public boolean modifierActivite(Activite activite) {
        String sql = """
                UPDATE activite SET id_utilisateur = ?, id_opportunite = ?, type_activite = ?, description = ?, date_echeance = ?, statut = ?
                WHERE id = ?
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirActivite(ps, activite);
            ps.setInt(7, activite.getId());
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'activité : " + e.getMessage());
            return false;
        }
    }

    
    /*  FANAMPIANA: Supprimer une activité */
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

    
    /* FANAMPIANA: Rechercher par ID */
    public Activite rechercherParId(int id) {
        String sql = "SELECT * FROM activite WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActivite(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }
        return null;
    }

    /* FANAMPIANA: Rechercher toutes les activités */
    public List<Activite> rechercherTous() {
        List<Activite> liste = new ArrayList<>();
        String sql = "SELECT * FROM activite ORDER BY id";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToActivite(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération : " + e.getMessage());
        }
        return liste;
    }
}