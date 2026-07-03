import models.Client;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nWelcome to CRM Analytics Dashboard\n");

        // INSERTION AO ANATY CLASS/TABLE Client
        Client client = new Client(1, "Marie Dupont", "marie.dupont@gmail.com", "0380000000", "Oracle", "Antananarivo", "actif");
        System.out.println(client + "\n");
    }
}
