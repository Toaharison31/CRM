package dao;

import databases.Connexion;
import models.Rapport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RapportDAO {
    /* Convertir une ligne les parametres du rapport */
    private void remplirRapport(PreparedStatement ps, Rapport rapport) throws SQLException {
        ps.setString(1, rapport.getType_rapport());
        ps.setInt(2, rapport.getGenere_par());
        ps.setObject(3, rapport.getDonnees(), java.sql.Types.OTHER);
    }
    /* Convertir une ligne ResultSet en objet Rapport */
    private Rapport mapResultSetToRapport(ResultSet rs) throws SQLException {
        return new Rapport(
                rs.getInt("id"),
                rs.getString("type_rapport"),
                rs.getInt("genere_par"),
                rs.getString("donnees"),
                rs.getTimestamp("cree_le")
        );
    }

    
    /* 1. UML: genererRapport() -> Mampiditra rapport vaovao ao amin'ny DB */
    public boolean genererRapport(Rapport rapport) {
        String sql = """
                INSERT INTO rapport(type_rapport, genere_par, donnees)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            remplirRapport(ps, rapport);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la génération du rapport : " + e.getMessage());
            return false;
        }
    }

    
    /* 2. UML: exporterPDF() -> Alaina ny rapport mialoha ny handefasana azy amin'ny lojika PDF */
    public Rapport exporterPDF(int id) {
        System.out.println("Préparation de l'exportation PDF pour le rapport ID : " + id);
        return rechercherParId(id); 
        // Rehefa azo ity Rapport ity any amin'ny Controller vao ampiasaina ny iText/JasperReports
    }

    
    /* 3. UML: exporterExcel() -> Alaina ny rapport mialoha ny handefasana azy amin'ny lojika Excel */
    public Rapport exporterExcel(int id) {
        System.out.println("Préparation de l'exportation Excel pour le rapport ID : " + id);
        return rechercherParId(id);
        // Rehefa azo ity Rapport ity any amin'ny Controller vao ampiasaina ny Apache POI
    }

    
    /* 4. UML: planifierRapport() -> Manova ny plan (type_rapport) ho an'ny tatitra iray */
    public boolean planifierRapport(int id, String nouveauType) {
        String sql = "UPDATE rapport SET type_rapport = ? WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouveauType); 
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la planification du rapport : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Supprimer un rapport */
    public boolean supprimerRapport(int id) {
        String sql = "DELETE FROM rapport WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du rapport : " + e.getMessage());
            return false;
        }
    }

    
    /* FANAMPIANA: Rechercher par ID */
    public Rapport rechercherParId(int id) {
        String sql = "SELECT * FROM rapport WHERE id = ?";
        try (Connection conn = Connexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRapport(rs);
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du rapport : " + e.getMessage());
        }
        return null;
    }

    
    /* FANAMPIANA: Rechercher tous les rapports */
    public List<Rapport> rechercherTous() {
        List<Rapport> liste = new ArrayList<>();
        String sql = "SELECT * FROM rapport ORDER BY id DESC";
        try (Connection conn = Connexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSetToRapport(rs));
            }
        } 
        catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des rapports : " + e.getMessage());
        }
        return liste;
    }
}