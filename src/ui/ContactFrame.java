package ui;

import dao.ContactDAO;
import models.Contact;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ContactFrame extends JFrame {
    // Interface de gestion des contacts: affichage, création, modification et suppression.
    private final ContactDAO contactDAO = new ContactDAO();
    private final ContactTableModel tableModel = new ContactTableModel();
    private final JTable contactTable = new JTable(tableModel);

    public ContactFrame() {
        super("Gestion des contacts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // panneau principal du frame de contacts
        // Panneau racine qui contient l'ensemble du contenu du formulaire
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rootPanel.setBackground(new Color(245, 247, 250));

        // Titre de la page
        JLabel titleLabel = new JLabel("Liste des contacts");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(24, 62, 95));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        // Barre d'outils : actions disponibles sur les contacts (CRUD, actualisation)
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        // boutons de gestion du contact
        JButton addButton = createActionButton("➕", "Ajouter");
        JButton editButton = createActionButton("✏️", "Modifier");
        JButton deleteButton = createActionButton("🗑️", "Supprimer");
        JButton refreshButton = createActionButton("🔄", "Actualiser");

        addButton.addActionListener(e -> openContactDialog(null));
        editButton.addActionListener(e -> editSelectedContact());
        deleteButton.addActionListener(e -> deleteSelectedContact());
        refreshButton.addActionListener(e -> refreshContacts());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);
        rootPanel.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

        // Configuration du tableau de contacts
        contactTable.setRowHeight(28);
        contactTable.setSelectionBackground(new Color(198, 227, 255));
        contactTable.setSelectionForeground(Color.BLACK);
        contactTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(contactTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 223, 228)));
        rootPanel.add(tableScroll, BorderLayout.CENTER);

        getContentPane().add(rootPanel);
        refreshContacts();
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

    // Recharge la liste des contacts depuis la base de données et met à jour le tableau
    private void refreshContacts() {
        tableModel.setContacts(contactDAO.rechercherTous());
    }

    // Modifier le contact sélectionné
    private void editSelectedContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact à modifier.");
            return;
        }
        int modelRow = contactTable.convertRowIndexToModel(selectedRow);
        Contact selectedContact = tableModel.getContactAt(modelRow);
        openContactDialog(selectedContact);
    }

    // Supprime le contact sélectionné après confirmation
    private void deleteSelectedContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce contact ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int modelRow = contactTable.convertRowIndexToModel(selectedRow);
        Contact selectedContact = tableModel.getContactAt(modelRow);
        if (contactDAO.supprimerContact(selectedContact.getId())) {
            JOptionPane.showMessageDialog(this, "Contact supprimé avec succès.");
            refreshContacts();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la suppression.");
        }
    }

    // Ouvre le dialogue de création ou modification de contact
    private void openContactDialog(Contact contact) {
        JDialog dialog = new JDialog(this, contact == null ? "Ajouter un contact" : "Modifier le contact", true);
        dialog.setSize(500, 380);
        dialog.setLocationRelativeTo(this);

        // Construction du formulaire de contact
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formilaire Contact
        JTextField clientIdField = new JTextField();
        JComboBox<String> typeField = new JComboBox<>(new String[]{"Appel", "Email", "Réunion"});
        JTextArea notesArea = new JTextArea();
        JTextField dateField = new JTextField();
        JTextField creatorField = new JTextField();

        if (contact != null) {
            clientIdField.setText(String.valueOf(contact.getId_client()));
            typeField.setSelectedItem(contact.getType_contact());
            notesArea.setText(contact.getNotes());
            dateField.setText(contact.getDate_contact() != null ? contact.getDate_contact().toString() : "");
            creatorField.setText(String.valueOf(contact.getCree_par()));
        }

        addLabelAndField(formPanel, gbc, 0, 0, "ID client", clientIdField);
        addLabelAndField(formPanel, gbc, 0, 1, "Type", typeField);
        addLabelAndField(formPanel, gbc, 0, 2, "Notes", new JScrollPane(notesArea));
        addLabelAndField(formPanel, gbc, 0, 3, "Date", dateField);
        addLabelAndField(formPanel, gbc, 0, 4, "Créé par", creatorField);

        // Panneau des actions de formulaire (enregistrer / annuler)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> {
            String clientIdText = clientIdField.getText().trim();
            String type = String.valueOf(typeField.getSelectedItem());
            String notes = notesArea.getText().trim();
            String dateText = dateField.getText().trim();
            String creatorText = creatorField.getText().trim();

            if (clientIdText.isEmpty() || type.isEmpty() || dateText.isEmpty() || creatorText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Les champs ID client, type, date et créé par sont obligatoires.");
                return;
            }

            try {
                Contact contactToSave = contact != null ? contact : new Contact();
                contactToSave.setId_client(Integer.parseInt(clientIdText));
                contactToSave.setType_contact(type);
                contactToSave.setNotes(notes);
                contactToSave.setDate_contact(Date.valueOf(dateText));
                contactToSave.setCree_par(Integer.parseInt(creatorText));

                boolean ok;
                if (contact != null) {
                    ok = contactDAO.modifierContact(contactToSave);
                } else {
                    ok = contactDAO.ajouterContact(contactToSave);
                }

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "Contact enregistré avec succès.");
                    refreshContacts();
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

    private static class ContactTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "ID client", "Type", "Notes", "Date", "Créé par"};
        private List<Contact> contacts = new ArrayList<>();

        void setContacts(List<Contact> contacts) {
            this.contacts = contacts;
            fireTableDataChanged();
        }

        Contact getContactAt(int rowIndex) {
            return contacts.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return contacts.size();
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
            Contact contact = contacts.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> contact.getId();
                case 1 -> contact.getId_client();
                case 2 -> contact.getType_contact();
                case 3 -> contact.getNotes();
                case 4 -> contact.getDate_contact();
                case 5 -> contact.getCree_par();
                default -> null;
            };
        }
    }
}
