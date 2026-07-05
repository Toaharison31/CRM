package ui;

import dao.OpportuniteDAO;
import models.Opportunite;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OpportuniteFrame extends JFrame {
    // Interface de gestion des opportunités : affichage, filtre, ajout, modification et suppression.
    private final OpportuniteDAO opportuniteDAO = new OpportuniteDAO();
    private final OpportuniteTableModel tableModel = new OpportuniteTableModel();
    private final JTable opportuniteTable = new JTable(tableModel);

    public OpportuniteFrame() {
        super("Gestion des opportunités");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // panneau principal pour la vue opportunités
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Liste des opportunités");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(24, 62, 95));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Barre d'outils des opportunités : actions de gestion
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        // boutons de gestion des opportunités
        JButton addButton = createActionButton("➕", "Ajouter");
        JButton editButton = createActionButton("✏️", "Modifier");
        JButton deleteButton = createActionButton("🗑️", "Supprimer");
        JButton refreshButton = createActionButton("🔄", "Actualiser");

        addButton.addActionListener(e -> openOpportuniteDialog(null));
        editButton.addActionListener(e -> editSelectedOpportunite());
        deleteButton.addActionListener(e -> deleteSelectedOpportunite());
        refreshButton.addActionListener(e -> refreshOpportunites());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);
        rootPanel.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

        // Filtre et tri par étape pour afficher uniquement certaines opportunités
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        JComboBox<String> stepFilter = new JComboBox<>(new String[]{"Tous", "Prospect", "Qualifié", "Proposition", "Gagné", "Perdu"});
        stepFilter.addActionListener(e -> {
            String selected = (String) stepFilter.getSelectedItem();
            if ("Tous".equals(selected)) {
                refreshOpportunites();
            } else {
                tableModel.setOpportunites(opportuniteDAO.listerParEtape(selected));
            }
        });
        filterPanel.add(new JLabel("Étape : "));
        filterPanel.add(stepFilter);
        rootPanel.add(filterPanel, BorderLayout.AFTER_LAST_LINE);

        // Tableau des opportunités
        opportuniteTable.setRowHeight(28);
        opportuniteTable.setSelectionBackground(new Color(198, 227, 255));
        opportuniteTable.setSelectionForeground(Color.BLACK);
        opportuniteTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(opportuniteTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 228)));
        rootPanel.add(tableScroll, BorderLayout.CENTER);

        getContentPane().add(rootPanel);
        refreshOpportunites();
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

    // Recharge les opportunités depuis la base de données
    private void refreshOpportunites() {
        tableModel.setOpportunites(opportuniteDAO.rechercherTous());
    }

    private void editSelectedOpportunite() {
        int selectedRow = opportuniteTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une opportunité à modifier.");
            return;
        }
        int modelRow = opportuniteTable.convertRowIndexToModel(selectedRow);
        Opportunite selected = tableModel.getOpportuniteAt(modelRow);
        openOpportuniteDialog(selected);
    }

    private void deleteSelectedOpportunite() {
        int selectedRow = opportuniteTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une opportunité à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer cette opportunité ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int modelRow = opportuniteTable.convertRowIndexToModel(selectedRow);
        Opportunite selected = tableModel.getOpportuniteAt(modelRow);
        if (opportuniteDAO.supprimerOpportunite(selected.getId())) {
            JOptionPane.showMessageDialog(this, "Opportunité supprimée avec succès.");
            refreshOpportunites();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la suppression.");
        }
    }

    // Ouvre le formulaire d'ajout ou modification d'une opportunité
    private void openOpportuniteDialog(Opportunite opportunite) {
        JDialog dialog = new JDialog(this, opportunite == null ? "Ajouter une opportunité" : "Modifier l’opportunité", true);
        dialog.setSize(520, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formulaire
        JTextField clientIdField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField valueField = new JTextField();
        JComboBox<String> stepField = new JComboBox<>(new String[]{"Prospect", "Qualifié", "Proposition", "Gagné", "Perdu"});
        JTextField dateField = new JTextField();
        JTextField creatorField = new JTextField();

        if (opportunite != null) {
            clientIdField.setText(String.valueOf(opportunite.getId_client()));
            titleField.setText(opportunite.getTitre());
            valueField.setText(String.valueOf(opportunite.getValeur()));
            stepField.setSelectedItem(opportunite.getEtape());
            dateField.setText(opportunite.getDate_cloture() != null ? opportunite.getDate_cloture().toString() : "");
            creatorField.setText(String.valueOf(opportunite.getCree_par()));
        }

        addLabelAndField(formPanel, gbc, 0, 0, "ID client", clientIdField);
        addLabelAndField(formPanel, gbc, 0, 1, "Titre", titleField);
        addLabelAndField(formPanel, gbc, 0, 2, "Valeur", valueField);
        addLabelAndField(formPanel, gbc, 0, 3, "Étape", stepField);
        addLabelAndField(formPanel, gbc, 0, 4, "Date clôture", dateField);
        addLabelAndField(formPanel, gbc, 0, 5, "Créé par", creatorField);

        // Panneau des actions du formulaire d'opportunité
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> {
            String clientIdText = clientIdField.getText().trim();
            String title = titleField.getText().trim();
            String valueText = valueField.getText().trim();
            String step = String.valueOf(stepField.getSelectedItem());
            String dateText = dateField.getText().trim();
            String creatorText = creatorField.getText().trim();

            if (clientIdText.isEmpty() || title.isEmpty() || valueText.isEmpty() || step.isEmpty() || dateText.isEmpty() || creatorText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tous les champs sont obligatoires.");
                return;
            }

            try {
                Opportunite oppToSave = opportunite != null ? opportunite : new Opportunite();
                oppToSave.setId_client(Integer.parseInt(clientIdText));
                oppToSave.setTitre(title);
                oppToSave.setValeur(Double.parseDouble(valueText));
                oppToSave.setEtape(step);
                oppToSave.setDate_cloture(Date.valueOf(dateText));
                oppToSave.setCree_par(Integer.parseInt(creatorText));

                boolean ok;
                if (opportunite != null) {
                    ok = opportuniteDAO.modifierOpportunite(oppToSave);
                } else {
                    ok = opportuniteDAO.creerOpportunite(oppToSave);
                }

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "Opportunité enregistrée avec succès.");
                    refreshOpportunites();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Échec de l’enregistrement.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Vérifiez le format des champs numériques ou de la date (yyyy-mm-dd).");
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

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int x, int y, String labelText, JComponent component) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText + " :"), gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private static class OpportuniteTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "ID client", "Titre", "Valeur", "Étape", "Date clôture", "Créé par"};
        private List<Opportunite> opportunites = new ArrayList<>();

        void setOpportunites(List<Opportunite> opportunites) {
            this.opportunites = opportunites;
            fireTableDataChanged();
        }

        Opportunite getOpportuniteAt(int rowIndex) {
            return opportunites.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return opportunites.size();
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
            Opportunite opp = opportunites.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> opp.getId();
                case 1 -> opp.getId_client();
                case 2 -> opp.getTitre();
                case 3 -> opp.getValeur();
                case 4 -> opp.getEtape();
                case 5 -> opp.getDate_cloture();
                case 6 -> opp.getCree_par();
                default -> null;
            };
        }
    }
}
