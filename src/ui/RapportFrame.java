package ui;

import dao.RapportDAO;
import models.Rapport;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RapportFrame extends JFrame {
    // Interface de gestion des rapports : affichage, génération, replanification et suppression.
    private final RapportDAO rapportDAO = new RapportDAO();
    private final RapportTableModel tableModel = new RapportTableModel();
    private final JTable rapportTable = new JTable(tableModel);

    public RapportFrame() {
        super("Gestion des rapports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // panneau principal du frame des rapports
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Liste des rapports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(24, 62, 95));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Barre d'outils des rapports : création, replanification, suppression
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        // boutons de gestion des rapports
        JButton addButton = createActionButton("➕", "Générer");
        JButton editButton = createActionButton("✏️", "Replanifier");
        JButton deleteButton = createActionButton("🗑️", "Supprimer");
        JButton refreshButton = createActionButton("🔄", "Actualiser");

        addButton.addActionListener(e -> openRapportDialog(null));
        editButton.addActionListener(e -> editSelectedRapport());
        deleteButton.addActionListener(e -> deleteSelectedRapport());
        refreshButton.addActionListener(e -> refreshRapports());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);
        rootPanel.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

        // Configuration du tableau des rapports
        rapportTable.setRowHeight(28);
        rapportTable.setSelectionBackground(new Color(198, 227, 255));
        rapportTable.setSelectionForeground(Color.BLACK);
        rapportTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(rapportTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 228)));
        rootPanel.add(tableScroll, BorderLayout.CENTER);

        getContentPane().add(rootPanel);
        refreshRapports();
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

    // Recharge la liste des rapports depuis la base de données
    private void refreshRapports() {
        tableModel.setRapports(rapportDAO.rechercherTous());
    }

    private void editSelectedRapport() {
        int selectedRow = rapportTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un rapport à replanifier.");
            return;
        }
        int modelRow = rapportTable.convertRowIndexToModel(selectedRow);
        Rapport rapport = tableModel.getRapportAt(modelRow);
        openRapportDialog(rapport);
    }

    private void deleteSelectedRapport() {
        int selectedRow = rapportTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un rapport à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce rapport ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int modelRow = rapportTable.convertRowIndexToModel(selectedRow);
        Rapport rapport = tableModel.getRapportAt(modelRow);
        if (rapportDAO.supprimerRapport(rapport.getId())) {
            JOptionPane.showMessageDialog(this, "Rapport supprimé avec succès.");
            refreshRapports();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la suppression.");
        }
    }

    // Ouvre le dialogue pour générer ou replanifier un rapport
    private void openRapportDialog(Rapport rapport) {
        boolean edition = rapport != null;
        JDialog dialog = new JDialog(this, edition ? "Replanifier le rapport" : "Générer un rapport", true);
        dialog.setSize(520, 360);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formulaire
        JComboBox<String> typeField = new JComboBox<>(new String[]{"Hébdomadaire", "Mensuel", "Par Commercial"});
        JTextField generatedByField = new JTextField();
        JTextArea dataArea = new JTextArea(4, 20);
        dataArea.setLineWrap(true);
        dataArea.setWrapStyleWord(true);
        JScrollPane dataScroll = new JScrollPane(dataArea);

        if (edition) {
            typeField.setSelectedItem(rapport.getType_rapport());
            generatedByField.setText(String.valueOf(rapport.getGenere_par()));
            dataArea.setText(rapport.getDonnees());
            generatedByField.setEnabled(false);
            dataArea.setEnabled(false);
        }

        addField(formPanel, gbc, 0, 0, "Type de rapport", typeField);
        addField(formPanel, gbc, 0, 1, "Généré par", generatedByField);
        addField(formPanel, gbc, 0, 2, "Données", dataScroll);

        // Panneau des actions du formulaire de rapport
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton(edition ? "Replanifier" : "Générer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> {
            String type = String.valueOf(typeField.getSelectedItem());
            String genereParText = generatedByField.getText().trim();
            String donnees = dataArea.getText().trim();

            if (type.isEmpty() || genereParText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Le type de rapport et l'utilisateur générateur sont obligatoires.");
                return;
            }

            try {
                boolean ok;
                if (edition) {
                    ok = rapportDAO.planifierRapport(rapport.getId(), type);
                } else {
                    Rapport nouveau = new Rapport();
                    nouveau.setType_rapport(type);
                    nouveau.setGenere_par(Integer.parseInt(genereParText));
                    nouveau.setDonnees(donnees);
                    ok = rapportDAO.genererRapport(nouveau);
                }

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, edition ? "Rapport replanifié avec succès." : "Rapport généré avec succès.");
                    refreshRapports();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Échec de l’enregistrement.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Le champ 'Généré par' doit être un entier.");
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

    private void addField(JPanel panel, GridBagConstraints gbc, int x, int y, String labelText, JComponent component) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText + " :"), gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private static class RapportTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Type", "Généré par", "Données", "Créé le"};
        private List<Rapport> rapports = new ArrayList<>();

        void setRapports(List<Rapport> rapports) {
            this.rapports = rapports;
            fireTableDataChanged();
        }

        Rapport getRapportAt(int rowIndex) {
            return rapports.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return rapports.size();
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
            Rapport rapport = rapports.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> rapport.getId();
                case 1 -> rapport.getType_rapport();
                case 2 -> rapport.getGenere_par();
                case 3 -> rapport.getDonnees();
                case 4 -> rapport.getCree_le();
                default -> null;
            };
        }
    }
}
