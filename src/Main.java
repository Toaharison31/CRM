import models.Client;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to CRM Analytics Dashboard");

        // INSERTION AO ANATY CLAS/TABLE Client 
        /***Exemple nanaovana test ito */
        Client client = new Client("NJAKANERA Nostos Duk'S Stakkino", "jhenStakkino@gmail.com", "0388086533", "Filatex", "Ankatso", "actif");
        System.out.println(client.insertClient());
    }
}
