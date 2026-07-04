package dao;

import databases.Connexion;
import models.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    /* Convertir une ligne les parametres de l'utilisateur */
    private void remplirUtilisateur(PreparedStatement ps, Utilisateur u) throws SQLException {
        ps.setString(1, u.getNom());
        ps.setString(2, u.getEmail());
        ps.setString(3, u.getMot_de_passe());
        ps.setString(4, u.getRole());
    }
    /* Convertir une ligne ResultSet en objet Utilisateur */
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("mot_de_passe"),
                rs.getString("role"),
                rs.getTimestamp("cree_le")
        );
    }

    
    /* 1. UML: seConnecter() -> Fanamarinana ho an'ny Login */
    public Utilisateur seConnecter(String email, String mdp) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, mdp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Connexion réussie pour : " + email);
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
        }
        System.out.println("Échec de connexion pour : " + email);
        return null;
    }


    /* 2. UML: seDeconnecter() -> Lojika fialana tsotra */
    public void seDeconnecter(String email) {
        System.out.println("Déconnexion de l'utilisateur : " + email);
    }

    
    /* 3. UML: changerMdp() -> Fanovana tenimiafina */
    public boolean changerMdp(int id, String nouvMdp) {
        String sql = "UPDATE utilisateur SET mot_de_passe = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouvMdp);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors du changement de mot de passe : " + e.getMessage());
            return false;
        }
    }

    
    /* 4. UML: verifierRole() -> Mijery ny role an'ny utilisateur iray */
    public String verifierRole(int id) {
        String sql = "SELECT role FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du rôle : " + e.getMessage());
        }
        return null;
    }

    
    /* FANAMPIANA: Ajouter un utilisateur */
    public boolean ajouterUtilisateur(Utilisateur u) {
        String sql = """
                INSERT INTO utilisateur(nom, email, mot_de_passe, role)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirUtilisateur(ps, u);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Modifier un utilisateur */
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = """
                UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ?
                WHERE id = ?
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirUtilisateur(ps, u);
            ps.setInt(5, u.getId());
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Supprimer un utilisateur */
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Rechercher par ID */
    public Utilisateur rechercherParId(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    
    /* FANAMPIANA: Rechercher tous les utilisateurs */
    public List<Utilisateur> rechercherTous() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY id DESC";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToUtilisateur(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return liste;
    }
}