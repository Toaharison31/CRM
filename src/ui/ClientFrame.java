package ui;

import dao.ClientDAO;
import models.Client;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClientFrame extends JFrame {
    // Interface de gestion des clients: affichage, recherche, ajout, modification et suppression.
    private final ClientDAO clientDAO = new ClientDAO();
    private final ClientTableModel tableModel = new ClientTableModel();
    private final JTable clientTable = new JTable(tableModel);

    public ClientFrame() {
        super("Gestion des clients");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // panneau principal du frame client
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Liste des clients");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(24, 62, 95));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Barre d'outils clients : boutons CRUD et recherche par nom
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        // boutons d'actions CRUD pour les clients
        JButton addButton = createCrudButton("➕", "Ajouter");
        JButton editButton = createCrudButton("✏️", "Modifier");
        JButton deleteButton = createCrudButton("🗑️", "Supprimer");
        JButton refreshButton = createCrudButton("🔄", "Actualiser");

        addButton.addActionListener(e -> openClientDialog(null));
        editButton.addActionListener(e -> editSelectedClient());
        deleteButton.addActionListener(e -> deleteSelectedClient());
        refreshButton.addActionListener(e -> refreshClients());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(toolbar, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(260, 32));
        searchField.setToolTipText("Rechercher par nom");
        searchField.addActionListener(e -> searchClients(searchField.getText()));

        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> searchClients(searchField.getText()));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.EAST);
        headerPanel.add(topPanel, BorderLayout.SOUTH);

        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // Tableau des clients affiché au centre de la fenêtre
        clientTable.setRowHeight(28);
        clientTable.setSelectionBackground(new Color(198, 227, 255));
        clientTable.setSelectionForeground(Color.BLACK);
        clientTable.getTableHeader().setReorderingAllowed(false);
        clientTable.setAutoCreateRowSorter(true);

        JScrollPane tableScroll = new JScrollPane(clientTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 228)));
        rootPanel.add(tableScroll, BorderLayout.CENTER);

        getContentPane().add(rootPanel);
        refreshClients();
    }

    // Création d'un bouton avec icône et texte pour le CRUD
    private JButton createCrudButton(String icon, String text) {
        JButton button = new JButton(icon + " " + text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 255, 255));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 219), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Recharge la liste des clients depuis la base de données
    private void refreshClients() {
        tableModel.setClients(clientDAO.rechercherTous());
    }

    // Recherche un client par nom et met à jour le tableau
    private void searchClients(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            refreshClients();
        } else {
            tableModel.setClients(clientDAO.rechercherParNom(keyword.trim()));
        }
    }

    // Ouvre le formulaire de modification du client sélectionné
    private void editSelectedClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à modifier.");
            return;
        }
        int modelRow = clientTable.convertRowIndexToModel(selectedRow);
        Client selectedClient = tableModel.getClientAt(modelRow);
        openClientDialog(selectedClient);
    }

    // Supprime le client sélectionné après confirmation
    private void deleteSelectedClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce client ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int modelRow = clientTable.convertRowIndexToModel(selectedRow);
        Client selectedClient = tableModel.getClientAt(modelRow);
        if (clientDAO.supprimerClient(selectedClient.getId())) {
            JOptionPane.showMessageDialog(this, "Client supprimé avec succès.");
            refreshClients();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la suppression.");
        }
    }

    // Ouvre le dialogue d'ajout ou de modification d'un client
    private void openClientDialog(Client client) {
        JDialog dialog = new JDialog(this, client == null ? "Ajouter un client" : "Modifier le client", true);
        dialog.setSize(480, 360);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formulaire
        JTextField nomField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField telField = new JTextField();
        JTextField entrepriseField = new JTextField();
        JTextField adresseField = new JTextField();
        JComboBox<String> statutCombo = new JComboBox<>(new String[]{"Nouveau", "Potentiel", "Client", "Inactif"});

        if (client != null) {
            nomField.setText(client.getNom_complet());
            emailField.setText(client.getEmail());
            telField.setText(client.getTelephone());
            entrepriseField.setText(client.getEntreprise());
            adresseField.setText(client.getAdresse());
            statutCombo.setSelectedItem(client.getStatut());
        }

        addField(formPanel, gbc, 0, 0, "Nom complet");
        addField(formPanel, gbc, 0, 1, "Email");
        addField(formPanel, gbc, 0, 2, "Téléphone");
        addField(formPanel, gbc, 0, 3, "Entreprise");
        addField(formPanel, gbc, 0, 4, "Adresse");
        addField(formPanel, gbc, 0, 5, "Statut");

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addRow(formPanel, gbc, 0, nomField);
        addRow(formPanel, gbc, 1, emailField);
        addRow(formPanel, gbc, 2, telField);
        addRow(formPanel, gbc, 3, entrepriseField);
        addRow(formPanel, gbc, 4, adresseField);
        addRow(formPanel, gbc, 5, statutCombo);

        // Panneau des actions du formulaire de client
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String tel = telField.getText().trim();
            String entreprise = entrepriseField.getText().trim();
            String adresse = adresseField.getText().trim();
            String statut = String.valueOf(statutCombo.getSelectedItem());

            if (nom.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Le nom et l’email sont obligatoires.");
                return;
            }

            Client clientToSave = client != null ? client : new Client();
            clientToSave.setNom_complet(nom);
            clientToSave.setEmail(email);
            clientToSave.setTelephone(tel);
            clientToSave.setEntreprise(entreprise);
            clientToSave.setAdresse(adresse);
            clientToSave.setStatut(statut);

            boolean ok = clientDAO.sauvegarderClient(clientToSave);

            if (ok) {
                JOptionPane.showMessageDialog(dialog, "Client enregistré avec succès.");
                refreshClients();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Échec de l’enregistrement.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(formPanel, BorderLayout.CENTER);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int x, int y, String labelText) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText + " :");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(label, gbc);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, JComponent component) {
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private static class ClientTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Nom complet", "Email", "Téléphone", "Entreprise", "Adresse", "Statut"};
        private List<Client> clients = new ArrayList<>();

        void setClients(List<Client> clients) {
            this.clients = clients;
            fireTableDataChanged();
        }

        Client getClientAt(int rowIndex) {
            return clients.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return clients.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Client client = clients.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> client.getId();
                case 1 -> client.getNom_complet();
                case 2 -> client.getEmail();
                case 3 -> client.getTelephone();
                case 4 -> client.getEntreprise();
                case 5 -> client.getAdresse();
                case 6 -> client.getStatut();
                default -> null;
            };
        }
    }
}
