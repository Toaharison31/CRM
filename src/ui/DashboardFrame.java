package ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class DashboardFrame extends JFrame {
    // Tableau de bord principal avec les liens vers les différents modules.
    public DashboardFrame() {
        super("CRM Analytics - Dashboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Container page
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(20, 20, 20));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // titre label
        JLabel titleLabel = new JLabel("Tableau de bord CRM");
        titleLabel.setFont(new Font("Bell MT", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Conteneur des cartes du dashboard
        JPanel contentPanel = new JPanel(new GridLayout(2, 3));
        contentPanel.setBackground(new Color(240, 240, 240));

        // Cartes de navigation vers chaque module métier
        // Onglet clients, opportunités, rapports et activités
        contentPanel.add(createCard("Activités", "", () -> new ActiviteFrame().setVisible(true)));
        contentPanel.add(createCard("Contacts", "", () -> new ContactFrame().setVisible(true)));
        contentPanel.add(createCard("Opportunités", "", () -> new OpportuniteFrame().setVisible(true)));
        contentPanel.add(createCard("Rapports", "", () -> new RapportFrame().setVisible(true)));
        contentPanel.add(createCard("Clients", "", () -> new ClientFrame().setVisible(true)));
        contentPanel.add(createCard("Utilisateurs","", () -> {}));

        rootPanel.add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(rootPanel);
    }

    // Création des cartes du dashboard : chaque carte ouvre une fenêtre métier.
    private JPanel createCard(String title, String value, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(200, 200, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

         // Titre anakiefatra Ventes, Clients, Opportunités, Activités
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Bell MT", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 110, 0));

        // Valeur à ajouter any amin'ny bdd
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Bell MT", Font.PLAIN, 14));
        valueLabel.setForeground(new Color(240, 240, 240));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // emplacement an'ilay cart
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return card;
    }
}