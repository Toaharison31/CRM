package ui;

import dao.UtilisateurDAO;
import models.Utilisateur;
import utils.LoginAttemptTracker;

import javax.swing.*;
import java.awt.*;

/**
 * Frame d'authentification sécurisée avec protection contre brute force.
 * N'utilise JAMAIS d'identifiants codés en dur.
 */
public class LoginFrame extends JFrame {
    private final JTextField utilisateurField;
    private final JPasswordField mot_de_passeField;
    // private int loginAttempts = 0;

    public LoginFrame() {
        super("CRM Analytics - Login");

        // Configuration de la fenêtre principale
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Création du panneau principal avec GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre de la fenêtre de connexion
        JLabel titleLabel = new JLabel("Authentification");
        titleLabel.setFont(new Font("Bell MT", Font.BOLD, 50));
        titleLabel.setForeground(new Color(0, 110, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Label et champ de nom d'utilisateur / email
        JLabel utilisateurLabel = new JLabel("Email :");
        utilisateurLabel.setFont(new Font("Bell MT", Font.BOLD, 15));
        utilisateurLabel.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(utilisateurLabel, gbc);

        // Champ de nom d'utilisateur
        utilisateurField = new JTextField(14);
        utilisateurField.setFont(new Font("Bell MT", Font.BOLD, 15));
        utilisateurField.setPreferredSize(new Dimension(220, 34));
        utilisateurField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 0), 2),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(utilisateurField, gbc);

        // Label et champ de mot de passe
        JLabel mot_de_passeLabel = new JLabel("Mot de passe :");
        mot_de_passeLabel.setFont(new Font("Bell MT", Font.BOLD, 15));
        mot_de_passeLabel.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(mot_de_passeLabel, gbc);

        // Champ de mot de passe
        mot_de_passeField = new JPasswordField(14);
        mot_de_passeField.setFont(new Font("Bell MT", Font.BOLD, 15));
        mot_de_passeField.setPreferredSize(new Dimension(220, 34));
        mot_de_passeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 0), 2),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(mot_de_passeField, gbc);

        // Bouton de connexion
        JButton loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Bell MT", Font.BOLD, 15));
        loginButton.setBackground(new Color(0, 110, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(true);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(loginButton, gbc);

        // Ajouter le panneau principal à la fenêtre
        getContentPane().add(mainPanel);
        getRootPane().setDefaultButton(loginButton);
    }

    /**
     * Gère la connexion de l'utilisateur avec sécurité.
     * ✓ Vérifie les comptes verrouillés
     * ✓ Limite les tentatives
     * ✓ Compare les mots de passe de manière sécurisée
     */
    private void handleLogin() {
        String utilisateur = utilisateurField.getText().trim();
        String mot_de_passe = new String(mot_de_passeField.getPassword());

        // Vérification des champs vides
        if (utilisateur.isEmpty() || mot_de_passe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si le compte est verrouillé
        if (LoginAttemptTracker.isAccountLocked(utilisateur)) {
            long minutesRemaining = LoginAttemptTracker.getMinutesUntilUnlock(utilisateur);
            JOptionPane.showMessageDialog(this,
                    "🔒 Compte verrouillé. Réessayez dans " + minutesRemaining + " minute(s).",
                    "Compte Verrouillé",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authentifie l'utilisateur via DAO
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        Utilisateur utilisateurConnecte = utilisateurDAO.seConnecter(utilisateur, mot_de_passe);

        if (utilisateurConnecte != null) {
            // Connexion réussie
            LoginAttemptTracker.recordSuccessfulLogin(utilisateur);
            JOptionPane.showMessageDialog(this, "✓ Connexion réussie !");
            
            // Ferme la fenêtre de login et ouvre le dashboard
            dispose();
            new DashboardFrame().setVisible(true);
        } else {
            // Connexion échouée
            LoginAttemptTracker.recordFailedAttempt(utilisateur);
            
            int remainingAttempts = LoginAttemptTracker.getAttemptsRemaining(utilisateur);
            if (remainingAttempts > 0) {
                JOptionPane.showMessageDialog(this,
                        "❌ Identifiants invalides.\n" +
                        "Tentatives restantes: " + remainingAttempts,
                        "Erreur d'authentification",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "🔒 Trop de tentatives échouées.\n" +
                        "Votre compte a été verrouillé pour 15 minutes.",
                        "Compte Verrouillé",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // Nettoie les vieilles tentatives (prévient fuites mémoire)
        LoginAttemptTracker.cleanup();
    }

    /**
     * Méthode principale pour lancer l'application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}