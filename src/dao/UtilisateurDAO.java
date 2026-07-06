package dao;

import databases.Connexion;
import models.Utilisateur;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**Data Access Object pour les utilisateurs.
 Gère l'authentification sécurisée avec hachage de mots de passe.*/
public class UtilisateurDAO {
    
    /**
     * Fomba pro hikilasiana ny teny miafina ho SHA-256 (Hachage mivantana eto amin'ny DAO)
     */
    private String hashSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erreur de hachage: " + e.getMessage());
            return password; // Fallback raha misy olana saingy tsy tokony hisy
        }
    }
    
    /** Remplit les paramètres d'un PreparedStatement avec les données d'un utilisateur.*/
    private void remplirUtilisateur(PreparedStatement ps, Utilisateur u) throws SQLException {
        ps.setString(1, u.getNom());
        ps.setString(2, u.getEmail());
        // Hache mivantana amin'ny alalan'ny SHA-256 eto amin'ny DAO
        ps.setString(3, hashSHA256(u.getMot_de_passe()));
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
     */
    public Utilisateur seConnecter(String email, String mdp) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur u = mapResultSetToUtilisateur(rs);
                    
                    // Manamarina ny hash SHA-256 mivantana eto
                    if (hashSHA256(mdp).equals(u.getMot_de_passe())) {
                        System.out.println("Authentification réussie pour: " + email);
                        return u;
                    } else {
                        System.out.println("Mot de passe incorrect pour: " + email);
                        return null;
                    }
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("ERREUR lors de l'authentification: " + e.getMessage());
        }
        System.out.println("⚠ Utilisateur non trouvé: " + email);
        return null;
    }

    public void seDeconnecter(String email) {
        System.out.println("✓ Déconnexion de l'utilisateur: " + email);
    }

    /**
     * Change le mot de passe d'un utilisateur.
     */
    public boolean changerMdp(int id, String nouvMdp) {
        if (nouvMdp == null || nouvMdp.trim().length() < 4) {
            System.err.println("Mot de passe faible");
            return false;
        }
        
        String sql = "UPDATE utilisateur SET mot_de_passe = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashSHA256(nouvMdp));
            ps.setInt(2, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                System.out.println("✓ Mot de passe changé pour utilisateur ID: " + id);
            } else {
                System.err.println("Utilisateur non trouvé: ID " + id);
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("ERREUR lors du changement de mot de passe: " + e.getMessage());
            return false;
        }
    }

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
            System.err.println("ERREUR lors de la vérification du rôle: " + e.getMessage());
        }
        return null;
    }

    public boolean ajouterUtilisateur(Utilisateur u) {
        if (u == null || u.getNom() == null || u.getEmail() == null) {
            System.err.println("Données utilisateur invalides");
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
                System.out.println("Utilisateur créé: " + u.getEmail());
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("ERREUR lors de l'ajout d'utilisateur: " + e.getMessage());
            return false;
        }
    }

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
                System.out.println("Utilisateur modifié: " + u.getEmail());
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("ERREUR lors de la modification: " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                System.out.println("Utilisateur supprimé: ID " + id);
            }
            return success;
        } 
        catch (SQLException e) {
            System.err.println("ERREUR lors de la suppression: " + e.getMessage());
            return false;
        }
    }

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
            System.err.println("ERREUR lors de la recherche: " + e.getMessage());
        }
        return null;
    }

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
            System.err.println("ERREUR lors de la récupération: " + e.getMessage());
        }
        return liste;
    }
}