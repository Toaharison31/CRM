package dao;

import models.Utilisateur;
import java.util.Scanner;

/**
 * Programme console pour créer un nouvel utilisateur manuellement.
 * Utilise UtilisateurDAO pour insérer directement en base (avec hachage sécurisé).
 */
public class CreateUtilisateur {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Création d'un nouvel utilisateur ===");

        System.out.print("Nom : ");
        String nom = sc.nextLine().trim();

        System.out.print("Email : ");
        String email = sc.nextLine().trim();

        String mot_de_passe;
        while (true) {
            System.out.print("Mot de passe : ");
            mot_de_passe = sc.nextLine();
            
            // Validation tsotra eto fa tsy mila an'izay Utils any ivelany
            if (mot_de_passe != null && mot_de_passe.trim().length() >= 4) {
                break;
            }
            System.out.println("Le mot de passe doit contenir au moins 8 caractères.");
        }

        System.out.print("Rôle (ex: admin, commercial, manager) : ");
        String role = sc.nextLine().trim();

        Utilisateur nouvelUtilisateur = new Utilisateur(0, nom, email, mot_de_passe, role, null);

        UtilisateurDAO dao = new UtilisateurDAO();
        boolean success = dao.ajouterUtilisateur(nouvelUtilisateur);

        if (success) {
            System.out.println("Utilisateur créé avec succès : " + email);
        } else {
            System.out.println("Échec de la création de l'utilisateur.");
        }

        sc.close();
    }
}