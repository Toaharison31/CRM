package dao;

import databases.Connexion;
import models.Utilisateur;
import utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**Data Access Object pour les utilisateurs.
 Gère l'authentification sécurisée avec hachage de mots de passe.*/
public class UtilisateurDAO {
    
    /** Remplit les paramètres d'un PreparedStatement avec les données d'un utilisateur.
     IMPORTANTE: Le mot de passe est crypté avant d'être stocké.*/
    private void remplirUtilisateur(PreparedStatement ps, Utilisateur u) throws SQLException {
        ps.setString(1, u.getNom());
        ps.setString(2, u.getEmail());
        // Hache le mot de passe avant stockage
        ps.setString(3, PasswordUtils.hashPassword(u.getMot_de_passe()));
        ps.setString(4, u.getRole());
    }
    
    /** Convertit une ligne ResultSet en objet Utilisateur.*/
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

    /**
     * Authentifie un utilisateur avec vérification du mot de passe haché.
     * SÉCURISÉ: Compare contre le hash stocké sans jamais afficher les mots de passe.
    @param email Email de l'utilisateur
    @param mdp Mot de passe en clair (sera haché pour comparaison)
    @return Utilisateur authentifié ou null si échec */
    public Utilisateur seConnecter(String email, String mdp) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur u = mapResultSetToUtilisateur(rs);
                    // Vérifie le mot de passe haché de manière sécurisée
                    if (PasswordUtils.verifyPassword(mdp, u.getMot_de_passe())) {
                        System.out.println("✓ Authentification réussie pour: " + email);
                        return u;
                    } else {
                        System.out.println("⚠ Mot de passe incorrect pour: " + email);
                        return null;
                    }
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors de l'authentification: " + e.getMessage());
        }
        System.out.println("⚠ Utilisateur non trouvé: " + email);
        return null;
    }

    /**
     * Déconnecte un utilisateur (log déconnexion).
     * 
     * @param email Email de l'utilisateur à déconnecter
     */
    public void seDeconnecter(String email) {
        System.out.println("✓ Déconnexion de l'utilisateur: " + email);
        // À améliorer: implémenter une vraie gestion de session
    }

    /**
     * Change le mot de passe d'un utilisateur avec validation et hachage.
     * 
     * @param id ID de l'utilisateur
     * @param nouvMdp Nouveau mot de passe en clair
     * @return true si succès
     */
    public boolean changerMdp(int id, String nouvMdp) {
        // Valide la force du mot de passe
        String validationError = PasswordUtils.validatePasswordStrength(nouvMdp);
        if (!validationError.isEmpty()) {
            System.err.println("❌ Mot de passe faible: " + validationError);
            return false;
        }
        
        String sql = "UPDATE utilisateur SET mot_de_passe = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Hache le nouveau mot de passe avant stockage
            ps.setString(1, PasswordUtils.hashPassword(nouvMdp));
            ps.setInt(2, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                System.out.println("✓ Mot de passe changé pour utilisateur ID: " + id);
            } else {
                System.err.println("❌ Utilisateur non trouvé: ID " + id);
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors du changement de mot de passe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie le rôle d'un utilisateur.
     * 
     * @param id ID de l'utilisateur
     * @return Rôle de l'utilisateur ou null si non trouvé
     */
    public String verifierRole(int id) {
        String sql = "SELECT role FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors de la vérification du rôle: " + e.getMessage());
        }
        return null;
    }

    /**
     * Ajoute un nouvel utilisateur avec mot de passe haché.
     * 
     * @param u Utilisateur à ajouter
     * @return true si succès
     */
    public boolean ajouterUtilisateur(Utilisateur u) {
        // Valide les paramètres de base
        if (u == null || u.getNom() == null || u.getEmail() == null) {
            System.err.println("❌ Données utilisateur invalides");
            return false;
        }
        
        String sql = """
                INSERT INTO utilisateur(nom, email, mot_de_passe, role)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirUtilisateur(ps, u);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                System.out.println("✓ Utilisateur créé: " + u.getEmail());
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors de l'ajout d'utilisateur: " + e.getMessage());
            return false;
        }
    }

    /**
     * Modifie un utilisateur existant.
     * 
     * @param u Utilisateur à modifier
     * @return true si succès
     */
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = """
                UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ?
                WHERE id = ?
                """;
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirUtilisateur(ps, u);
            ps.setInt(5, u.getId());
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                System.out.println("✓ Utilisateur modifié: " + u.getEmail());
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors de la modification: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un utilisateur.
     * 
     * @param id ID de l'utilisateur à supprimer
     * @return true si succès
     */
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                System.out.println("✓ Utilisateur supprimé: ID " + id);
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche un utilisateur par son ID.
     * 
     * @param id ID de l'utilisateur
     * @return Utilisateur trouvé ou null
     */
    public Utilisateur rechercherParId(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("❌ ERREUR lors de la recherche: " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère tous les utilisateurs.
     * @return Liste de tous les utilisateurs
     */
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
            System.err.println("❌ ERREUR lors de la récupération: " + e.getMessage());
        }
        return liste;
    }
}