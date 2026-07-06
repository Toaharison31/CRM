import ui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Manomboka ny Interface Swing amin'ny fomba azo antoka (Thread-safe)
        SwingUtilities.invokeLater(() -> {
            System.out.println("🚀 Fandefasana ny CRM Analytics...");
            
            // Mamorona sy mampiseho ny pejy fidirana (Login)
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}