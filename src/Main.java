import javax.swing.SwingUtilities;
import ui.ClientFrame;
import ui.LoginFrame;
import ui.DashboardFrame;
import ui.ContactFrame;
import ui.OpportuniteFrame;
import ui.RapportFrame;
import ui.ActiviteFrame;

public class Main {
    // Méthode principale pour lancer l'application (mampiditra compte)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Lien vers la méthode showDashboard() dans LoginFrame (navigation vers le tableau de bord)
    public static void showDashboard() {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }

    // lien vers Client
    public static void showClients() {
        SwingUtilities.invokeLater(() -> new ClientFrame().setVisible(true));
    }

    // lien vers Contact
    public static void showContacts() {
        SwingUtilities.invokeLater(() -> new ContactFrame().setVisible(true));
    }

    // lien vers Opportunités
    public static void showOpportunites() {
        SwingUtilities.invokeLater(() -> new OpportuniteFrame().setVisible(true));
    }

    // lien vers Rapports
    public static void showRapports() {
        SwingUtilities.invokeLater(() -> new RapportFrame().setVisible(true));
    }

    // lien vers activités
    public static void showActivites() {
        SwingUtilities.invokeLater(() -> new ActiviteFrame().setVisible(true));
    }

}