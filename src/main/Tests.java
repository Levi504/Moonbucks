package main;

import data.DataManager;
import data.EntityType;
import model.Customer;

public class Tests {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("=== TEST 1 : Afficher les clients ===");
        System.out.println("========================================");
        DataManager.view(EntityType.CUSTOMER);

        System.out.println("\n========================================");
        System.out.println("=== TEST 2 : Ajouter un NOUVEAU client ===");
        System.out.println("========================================");
        Customer customer1 = new Customer("TEMP", "Alice Wonderland", "22 Rabbit Hole Street", "0700000001");
        boolean success1 = DataManager.add(customer1);
        System.out.println("Résultat : " + (success1 ? "SUCCÈS" : "ÉCHEC"));

        System.out.println("\n========================================");
        System.out.println("=== TEST 3 : Ajouter un DOUBLON (même téléphone) ===");
        System.out.println("========================================");
        Customer customer2 = new Customer("TEMP", "Alice Clone", "99 Different Street", "0700000001");
        boolean success2 = DataManager.add(customer2);
        System.out.println("Résultat : " + (success2 ? "SUCCÈS" : "ÉCHEC (attendu)"));

        System.out.println("\n========================================");
        System.out.println("=== TEST 4 : Ajouter un autre client valide ===");
        System.out.println("========================================");
        Customer customer3 = new Customer("TEMP", "Bob Builder", "55 Construction Ave", "0700000002");
        boolean success3 = DataManager.add(customer3);
        System.out.println("Résultat : " + (success3 ? "SUCCÈS" : "ÉCHEC"));

        System.out.println("\n========================================");
        System.out.println("=== TEST 5 : Afficher la liste finale ===");
        System.out.println("========================================");
        DataManager.view(EntityType.CUSTOMER);

        System.out.println("\n========================================");
        System.out.println("=== TEST 6 : Vérifier l'existence par téléphone ===");
        System.out.println("========================================");
        System.out.println("0700000001 existe ? " + DataManager.customerExistsByPhone("0700000001"));
        System.out.println("0700000002 existe ? " + DataManager.customerExistsByPhone("0700000002"));
        System.out.println("0999999999 existe ? " + DataManager.customerExistsByPhone("0999999999"));
    }
}