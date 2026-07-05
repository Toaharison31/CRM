package ui;

import dao.ActiviteDAO;
import models.Activite;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface de gestion des activités : affichage, création, modification et suppression.
 */
public class ActiviteFrame extends JFrame {
    private final ActiviteDAO activiteDAO = new ActiviteDAO();
    private final ActiviteTableModel tableModel = new ActiviteTableModel();
    private final JTable activiteTable = new JTable(tableModel);

    public ActiviteFrame() {
        super("Gestion des activités");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Panneau principal du frame activités
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 247, 250));

        // Titre de la page
        JLabel titleLabel = new JLabel("Liste des activités");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(24, 62, 95));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Barre d'outils : actions disponibles sur les activités (CRUD, actualisation)
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        // Boutons de gestion d'activité
        JButton addButton = createActionButton("➕", "Ajouter");
        JButton editButton = createActionButton("✏️", "Modifier");
        JButton deleteButton = createActionButton("🗑️", "Supprimer");
        JButton refreshButton = createActionButton("🔄", "Actualiser");

        addButton.addActionListener(e -> openActiviteDialog(null));
        editButton.addActionListener(e -> editSelectedActivite());
        deleteButton.addActionListener(e -> deleteSelectedActivite());
        refreshButton.addActionListener(e -> refreshActivites());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);
        rootPanel.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

        // Configuration du tableau d'activités
        activiteTable.setRowHeight(28);
        activiteTable.setSelectionBackground(new Color(198, 227, 255));
        activiteTable.setSelectionForeground(Color.BLACK);
        activiteTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(activiteTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 228)));
        rootPanel.add(tableScroll, BorderLayout.CENTER);

        getContentPane().add(rootPanel);
        refreshActivites();
    }

    private JButton createActionButton(String icon, String text) {
        JButton button = new JButton(icon + " " + text);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 219), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return button;
    }

    // Recharge la liste des activités depuis la base de données et met à jour le tableau
    private void refreshActivites() {
        tableModel.setActivites(activiteDAO.rechercherTous());
    }

    // Modifie l'activité sélectionnée
    private void editSelectedActivite() {
        int selectedRow = activiteTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une activité à modifier.");
            return;
        }
        int modelRow = activiteTable.convertRowIndexToModel(selectedRow);
        Activite selectedActivite = tableModel.getActiviteAt(modelRow);
        openActiviteDialog(selectedActivite);
    }

    // Supprime l'activité sélectionnée après confirmation
    private void deleteSelectedActivite() {
        int selectedRow = activiteTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une activité à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer cette activité ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int modelRow = activiteTable.convertRowIndexToModel(selectedRow);
        Activite selectedActivite = tableModel.getActiviteAt(modelRow);
        if (activiteDAO.supprimerActivite(selectedActivite.getId())) {
            JOptionPane.showMessageDialog(this, "Activité supprimée avec succès.");
            refreshActivites();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la suppression.");
        }
    }

    // Ouvre le dialogue de création ou modification d'activité
    private void openActiviteDialog(Activite activite) {
        JDialog dialog = new JDialog(this, activite == null ? "Ajouter une activité" : "Modifier l'activité", true);
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);

        // Construction du formulaire d'activité
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField utilisateurIdField = new JTextField();
        JTextField opportuniteIdField = new JTextField();
        JComboBox<String> typeField = new JComboBox<>(new String[]{"Appel", "Tâche", "Note"});
        JTextArea descriptionArea = new JTextArea();
        JTextField dateEcheanceField = new JTextField();
        JComboBox<String> statutCombo = new JComboBox<>(new String[]{"En attente", "Terminé", "Annulé"});

        if (activite != null) {
            utilisateurIdField.setText(String.valueOf(activite.getId_utilisateur()));
            opportuniteIdField.setText(String.valueOf(activite.getId_opportunite()));
            typeField.setSelectedItem(activite.getType_activite());
            descriptionArea.setText(activite.getDescription());
            dateEcheanceField.setText(activite.getDate_echeance() != null ? activite.getDate_echeance().toString() : "");
            statutCombo.setSelectedItem(activite.getStatut());
        }

        addLabelAndField(formPanel, gbc, 0, 0, "ID utilisateur", utilisateurIdField);
        addLabelAndField(formPanel, gbc, 0, 1, "ID opportunité", opportuniteIdField);
        addLabelAndField(formPanel, gbc, 0, 2, "Type", typeField);
        addLabelAndField(formPanel, gbc, 0, 3, "Description", new JScrollPane(descriptionArea));
        addLabelAndField(formPanel, gbc, 0, 4, "Date échéance", dateEcheanceField);
        addLabelAndField(formPanel, gbc, 0, 5, "Statut", statutCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> {
            String utilisateurIdText = utilisateurIdField.getText().trim();
            String opportuniteIdText = opportuniteIdField.getText().trim();
            String type = String.valueOf(typeField.getSelectedItem());
            String description = descriptionArea.getText().trim();
            String dateEcheanceText = dateEcheanceField.getText().trim();
            String statut = String.valueOf(statutCombo.getSelectedItem());

            if (utilisateurIdText.isEmpty() || opportuniteIdText.isEmpty() || type.isEmpty() || dateEcheanceText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Les champs ID utilisateur, ID opportunité, type et date sont obligatoires.");
                return;
            }

            try {
                Activite activiteToSave = activite != null ? activite : new Activite();
                activiteToSave.setId_utilisateur(Integer.parseInt(utilisateurIdText));
                activiteToSave.setId_opportunite(Integer.parseInt(opportuniteIdText));
                activiteToSave.setType_activite(type);
                activiteToSave.setDescription(description);
                activiteToSave.setDate_echeance(Date.valueOf(dateEcheanceText));
                activiteToSave.setStatut(statut);

                boolean ok;
                if (activite != null) {
                    ok = activiteDAO.modifierActivite(activiteToSave);
                } else {
                    ok = activiteDAO.creerActivite(activiteToSave);
                }

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "Activité enregistrée avec succès.");
                    refreshActivites();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Échec de l'enregistrement.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Vérifiez le format des champs numériques ou de la date (yyyy-mm-dd).");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Affichage du dialogue d'édition / création
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(formPanel, BorderLayout.CENTER);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int x, int y, String labelText, JComponent component) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText + " :"), gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private static class ActiviteTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "ID utilisateur", "ID opportunité", "Type", "Description", "Date échéance", "Statut"};
        private List<Activite> activites = new ArrayList<>();

        void setActivites(List<Activite> activites) {
            this.activites = activites;
            fireTableDataChanged();
        }

        Activite getActiviteAt(int rowIndex) {
            return activites.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return activites.size();
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
            Activite activite = activites.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> activite.getId();
                case 1 -> activite.getId_utilisateur();
                case 2 -> activite.getId_opportunite();
                case 3 -> activite.getType_activite();
                case 4 -> activite.getDescription();
                case 5 -> activite.getDate_echeance();
                case 6 -> activite.getStatut();
                default -> null;
            };
        }
    }
}
