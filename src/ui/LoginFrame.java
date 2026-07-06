package ui;

import dao.UtilisateurDAO;
import models.Utilisateur;

import javax.swing.*;
import java.awt.*;

/**
 * Frame d'authentification sécurisée avec protection contre brute force.
 * N'utilise JAMAIS d'identifiants codés en dur.
 */
public class LoginFrame extends JFrame {
    private final JTextField utilisateurField;
    private final JPasswordField mot_de_passeField;
    private int loginAttempts = 0; 
    private final int MAX_ATTEMPTS = 3;

    public LoginFrame() {
        super("CRM Analytics - Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550); 
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Authentification");
        titleLabel.setFont(new Font("Bell MT", Font.BOLD, 50));
        titleLabel.setForeground(new Color(0, 110, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel utilisateurLabel = new JLabel("Email :");
        utilisateurLabel.setFont(new Font("Bell MT", Font.BOLD, 15));
        utilisateurLabel.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(utilisateurLabel, gbc);

        utilisateurField = new JTextField(14);
        utilisateurField.setFont(new Font("Bell MT", Font.BOLD, 15));
        utilisateurField.setPreferredSize(new Dimension(220, 34));
        utilisateurField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 0), 2),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(utilisateurField, gbc);

        JLabel mot_de_passeLabel = new JLabel("Mot de passe :");
        mot_de_passeLabel.setFont(new Font("Bell MT", Font.BOLD, 15));
        mot_de_passeLabel.setForeground(new Color(30, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(mot_de_passeLabel, gbc);

        mot_de_passeField = new JPasswordField(14);
        mot_de_passeField.setFont(new Font("Bell MT", Font.BOLD, 15));
        mot_de_passeField.setPreferredSize(new Dimension(220, 34));
        mot_de_passeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 0), 2),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(mot_de_passeField, gbc);

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

        JButton registerButton = new JButton("Créer un compte (Admin Only)");
        registerButton.setFont(new Font("Bell MT", Font.PLAIN, 12));
        registerButton.setForeground(Color.GRAY);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> openInscriptionDialog());
        
        gbc.gridy = 4;
        mainPanel.add(registerButton, gbc);

        getContentPane().add(mainPanel);
        getRootPane().setDefaultButton(loginButton);
    }

    private void handleLogin() {
        String utilisateur = utilisateurField.getText().trim();
        String mot_de_passe = new String(mot_de_passeField.getPassword());

        if (utilisateur.isEmpty() || mot_de_passe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Veuillez remplir tous les champs.");
            return;
        }

        if (loginAttempts >= MAX_ATTEMPTS) {
            JOptionPane.showMessageDialog(this,
                    "🔒 Trop de tentatives échouées. L'application doit être redémarrée.",
                    "Application Verrouillée",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        Utilisateur utilisateurConnecte = utilisateurDAO.seConnecter(utilisateur, mot_de_passe);

        if (utilisateurConnecte != null) {
            loginAttempts = 0; 
            JOptionPane.showMessageDialog(this, "✓ Connexion réussie !");
            dispose();
        } else {
            loginAttempts++;
            int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
            
            if (remainingAttempts > 0) {
                JOptionPane.showMessageDialog(this,
                        "❌ Identifiants invalides.\n" +
                        "Tentatives restantes: " + remainingAttempts,
                        "Erreur d'authentification",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "🔒 Trop de tentatives échouées.\n" +
                        "La connexion est bloquée pour cette session.",
                        "Compte Verrouillé",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * NOHAVAOZINA: Mangataka ny mombamomba ny Admin aloha vao manokatra formulaire
     */
    private void openInscriptionDialog() {
        // 1. Mamorona formulaire kely hitakiana ny login an'ny Admin
        JDialog authDialog = new JDialog(this, "Vérification Administrateur", true);
        authDialog.setSize(350, 250);
        authDialog.setLocationRelativeTo(this);
        authDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField adminEmailF = new JTextField(15);
        JPasswordField adminMdpF = new JPasswordField(15);

        c.gridx = 0; c.gridy = 0; authDialog.add(new JLabel("Email Admin :"), c);
        c.gridx = 1; authDialog.add(adminEmailF, c);

        c.gridx = 0; c.gridy = 1; authDialog.add(new JLabel("Mot de passe :"), c);
        c.gridx = 1; authDialog.add(adminMdpF, c);

        JButton verifyBtn = new JButton("Vérifier");
        verifyBtn.setBackground(new Color(0, 110, 0));
        verifyBtn.setForeground(Color.WHITE);
        
        verifyBtn.addActionListener(ev -> {
            String email = adminEmailF.getText().trim();
            String mdp = new String(adminMdpF.getPassword());

            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur admin = dao.seConnecter(email, mdp);

            // Fanamarinana: tsy maintsy misy ny kaonty ARY tsy maintsy "admin" ny role-ny
            if (admin != null && "admin".equalsIgnoreCase(admin.getRole())) {
                authDialog.dispose(); // Akatona ity pejy kely ity
                showRealRegisterForm(); // Sokafy ilay tena formulaire fampidirana olona
            } else {
                JOptionPane.showMessageDialog(authDialog, "❌ Accès refusé. Vous devez être un administrateur.", "Erreur de sécurité", JOptionPane.ERROR_MESSAGE);
            }
        });

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        authDialog.add(verifyBtn, c);
        authDialog.setVisible(true);
    }

    /**
     * Ity ilay tena Formulaire fampidirana olona (tsy misokatra raha tsy nandalo fanamarinana teo ambony)
     */
    private void showRealRegisterForm() {
        JDialog dialog = new JDialog(this, "Inscription Nouvel Utilisateur", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField nomF = new JTextField(15);
        JTextField emailF = new JTextField(15);
        JPasswordField mdpF = new JPasswordField(15);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"admin", "commercial", "manager"});

        c.gridx = 0; c.gridy = 0; dialog.add(new JLabel("Nom :"), c);
        c.gridx = 1; dialog.add(nomF, c);

        c.gridx = 0; c.gridy = 1; dialog.add(new JLabel("Email :"), c);
        c.gridx = 1; dialog.add(emailF, c);

        c.gridx = 0; c.gridy = 2; dialog.add(new JLabel("Mot de passe :"), c);
        c.gridx = 1; dialog.add(mdpF, c);

        c.gridx = 0; c.gridy = 3; dialog.add(new JLabel("Rôle :"), c);
        c.gridx = 1; dialog.add(roleCombo, c);

        JButton saveBtn = new JButton("Enregistrer l'utilisateur");
        saveBtn.setBackground(new Color(0, 110, 0));
        saveBtn.setForeground(Color.WHITE);
        
        saveBtn.addActionListener(ev -> {
            String nom = nomF.getText().trim();
            String email = emailF.getText().trim();
            String mdp = new String(mdpF.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "❌ Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String validationError = utils.Validation.validatePasswordStrength(mdp);
            if (!validationError.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "❌ " + validationError, "Mot de passe faible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Utilisateur nouvelU = new Utilisateur(0, nom, email, mdp, role, null);
            UtilisateurDAO dao = new UtilisateurDAO();
            
            if (dao.ajouterUtilisateur(nouvelU)) {
                JOptionPane.showMessageDialog(dialog, "✅ Utilisateur créé avec succès !");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "❌ Échec de la création de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, c);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}