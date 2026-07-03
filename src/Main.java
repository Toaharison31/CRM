import models.Client;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to CRM Analytics Dashboard");

        // INSERTION AO ANATY CLAS/TABLE Client 
        Client client = new Client(1, "RANAIVOARISON Anjanirina Toavina", "toaharison@gmail.com", "0385241577", "YALP Dev", "Lot IB 68 Bis Antanambao", "déconnecté");
        System.out.println(client);
    }
}