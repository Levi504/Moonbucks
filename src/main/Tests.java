package main;

import data.DataManager;
import data.EntityType;
import model.*;

public class Tests {
    private static final String OK = "[OK]";
    private static final String FAIL = "[FAIL]";

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("          TESTS COMPLETS - MOONBUCKS SYSTEM");
        System.out.println("================================================================\n");

        // ========================================
        // SECTION 1 : TESTS CUSTOMER
        // ========================================
        System.out.println("================================================================");
        System.out.println("              SECTION 1 : CUSTOMERS");
        System.out.println("================================================================\n");

        System.out.println("TEST 1.1 : Afficher tous les clients");
        DataManager.view(EntityType.CUSTOMER);

        System.out.println("\nTEST 1.2 : Ajouter un nouveau client");
        Customer c1 = new Customer("TEMP", "John Doe", "123 Main St", "0711111111");
        System.out.println(DataManager.add(c1) ? OK + " SUCCES" : FAIL + " ECHEC");

        // ========================================
        // SECTION 2 : TESTS PRODUCT
        // ========================================
        System.out.println("\n================================================================");
        System.out.println("              SECTION 2 : PRODUCTS");
        System.out.println("================================================================\n");

        System.out.println("TEST 2.1 : Afficher tous les produits");
        DataManager.view(EntityType.PRODUCT);

        System.out.println("\nTEST 2.2 : Ajouter un produit NON-FRAGILE");
        Product p1 = new NonFragileProduct("TEMP", "Coffee", 10.00);
        System.out.println(DataManager.add(p1) ? OK + " SUCCES" : FAIL + " ECHEC");

        System.out.println("\nTEST 2.3 : Ajouter un produit FRAGILE");
        Product p2 = new FragileProduct("TEMP", "Glass", 20.00);
        System.out.println(DataManager.add(p2) ? OK + " SUCCES" : FAIL + " ECHEC");

        // ========================================
        // SECTION 3 : TESTS ORDER
        // ========================================
        System.out.println("\n================================================================");
        System.out.println("              SECTION 3 : ORDERS");
        System.out.println("================================================================\n");

        // ⭐ Utiliser les produits P004 et P005 qui viennent d'être ajoutés
        System.out.println("TEST 3.1 : Creer une commande avec 2 items");
        Customer customer = new Customer("C001", "Jean Dupont", "123 Rue", "0123456789");
        Order order1 = new Order("TEMP", customer);
        
        // ⭐ Créer des produits avec les vrais IDs (P004 et P005)
        Product realP1 = new NonFragileProduct("P004", "Coffee", 10.00);
        Product realP2 = new FragileProduct("P005", "Glass", 20.00);
        
        order1.addOrderItem(new OrderItem(realP1, "TEMP", 2));
        order1.addOrderItem(new OrderItem(realP2, "TEMP", 1));
        
        System.out.println("Nombre d'items : " + order1.getNumberOfItems());
        System.out.println("Total calcule : " + order1.calculateTotal());
        System.out.println("Attendu : (10+2)*2 + (20+5)*1 = 24 + 25 = 49.00");
        
        boolean success = DataManager.add(order1);
        System.out.println(success ? OK + " SUCCES" : FAIL + " ECHEC");

        // Afficher les commandes
        System.out.println("\nTEST 3.2 : Afficher toutes les commandes");
        DataManager.view(EntityType.ORDER);

        System.out.println("\nTEST 3.3 : Afficher tous les order items");
        DataManager.view(EntityType.ORDER_ITEM);

        // Tester avec un client inexistant
        System.out.println("\nTEST 3.4 : Commander avec un client INEXISTANT");
        Customer fakeCustomer = new Customer("C999", "Fake", "Nowhere", "0000000000");
        Order order2 = new Order("TEMP", fakeCustomer);
        order2.addOrderItem(new OrderItem(realP1, "TEMP", 1));
        boolean fail = DataManager.add(order2);
        System.out.println(fail ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (client inexistant detecte)");

        // Tester avec une commande vide
        System.out.println("\nTEST 3.5 : Commander avec une commande VIDE");
        Order emptyOrder = new Order("TEMP", customer);
        boolean fail2 = DataManager.add(emptyOrder);
        System.out.println(fail2 ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (commande vide detectee)");

        System.out.println("\n================================================================");
        System.out.println("                    FIN DES TESTS");
        System.out.println("================================================================");
    }
}