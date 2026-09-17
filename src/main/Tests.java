package main;

import data.DataManager;
import data.EntityType;
import model.*;

public class Tests {
    private static final String OK = "[OK]";
    private static final String FAIL = "[FAIL]";

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("          TESTS EDIT - MOONBUCKS SYSTEM");
        System.out.println("================================================================\n");

        // ========================================
        // SECTION 1 : ETAT INITIAL
        // ========================================
        System.out.println("--- Clients avant modification ---");
        DataManager.view(EntityType.CUSTOMER);

        System.out.println("\n--- Produits avant modification ---");
        DataManager.view(EntityType.PRODUCT);

        // ========================================
        // SECTION 2 : EDIT CUSTOMER
        // ========================================
        System.out.println("\n================================================================");
        System.out.println("              SECTION 2 : EDIT CUSTOMER");
        System.out.println("================================================================\n");

        System.out.println("TEST 2.1 : Modifier un client EXISTANT (C001)");
        Customer modifiedCustomer = new Customer("C001", "Jean Dupont MODIFIE", "Nouvelle Adresse", "0123456789");
        boolean edit1 = DataManager.edit(modifiedCustomer);
        System.out.println(edit1 ? OK + " SUCCES" : FAIL + " ECHEC");

        System.out.println("\nTEST 2.2 : Modifier avec un telephone DEJA UTILISE");
        Customer conflictCustomer = new Customer("C001", "Jean", "Adresse", "0987654321"); // telephone de C002
        boolean edit2 = DataManager.edit(conflictCustomer);
        System.out.println(edit2 ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (conflit detecte)");

        System.out.println("\nTEST 2.3 : Modifier un client INEXISTANT");
        Customer fakeCustomer = new Customer("C999", "Fake", "Nowhere", "0000000000");
        boolean edit3 = DataManager.edit(fakeCustomer);
        System.out.println(edit3 ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (client inexistant)");

        System.out.println("\n--- Clients apres modification ---");
        DataManager.view(EntityType.CUSTOMER);

        // ========================================
        // SECTION 3 : EDIT PRODUCT
        // ========================================
        System.out.println("\n================================================================");
        System.out.println("              SECTION 3 : EDIT PRODUCT");
        System.out.println("================================================================\n");

        System.out.println("TEST 3.1 : Modifier un produit EXISTANT (P001)");
        Product modifiedProduct = new NonFragileProduct("P001", "Cappuccino MODIFIE", 20.00);
        boolean edit4 = DataManager.edit(modifiedProduct);
        System.out.println(edit4 ? OK + " SUCCES" : FAIL + " ECHEC");

        System.out.println("\nTEST 3.2 : Modifier avec un nom DEJA UTILISE");
        Product conflictProduct = new NonFragileProduct("P001", "Tea", 15.00); // nom de P003
        boolean edit5 = DataManager.edit(conflictProduct);
        System.out.println(edit5 ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (conflit detecte)");

        System.out.println("\nTEST 3.3 : Modifier un produit INEXISTANT");
        Product fakeProduct = new NonFragileProduct("P999", "Fake", 10.00);
        boolean edit6 = DataManager.edit(fakeProduct);
        System.out.println(edit6 ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (produit inexistant)");

        System.out.println("\n--- Produits apres modification ---");
        DataManager.view(EntityType.PRODUCT);

        // ========================================
        // SECTION 4 : EDIT ORDER
        // ========================================
        System.out.println("\n================================================================");
        System.out.println("              SECTION 4 : EDIT ORDER");
        System.out.println("================================================================\n");

        // Créer une commande pour tester sa modification
        System.out.println("TEST 4.0 : Creer une commande pour tester sa modification");
        Customer customer = new Customer("C001", "Jean Dupont MODIFIE", "Nouvelle Adresse", "0123456789");
        Order order1 = new Order("TEMP", customer);
        Product p1 = new NonFragileProduct("P001", "Cappuccino MODIFIE", 20.00);
        order1.addOrderItem(new OrderItem(p1, "TEMP", 2));
        DataManager.add(order1);

        System.out.println("\n--- Commandes avant modification ---");
        DataManager.view(EntityType.ORDER);

        System.out.println("\n--- OrderItems avant modification ---");
        DataManager.view(EntityType.ORDER_ITEM);

        // Modifier la commande
        System.out.println("\nTEST 4.1 : Modifier la commande O001");
        Product p2 = new FragileProduct("P002", "Glass Mug", 25.00);
        Order modifiedOrder = new Order("O001", customer);
        modifiedOrder.addOrderItem(new OrderItem(p1, "O001", 3));  // 3 au lieu de 2
        modifiedOrder.addOrderItem(new OrderItem(p2, "O001", 1));  // ajout d'un 2e item
        boolean edit7 = DataManager.edit(modifiedOrder);
        System.out.println(edit7 ? OK + " SUCCES" : FAIL + " ECHEC");

        System.out.println("\n--- Commandes apres modification ---");
        DataManager.view(EntityType.ORDER);

        System.out.println("\n--- OrderItems apres modification ---");
        DataManager.view(EntityType.ORDER_ITEM);

        System.out.println("\nTEST 4.2 : Modifier une commande INEXISTANTE");
        Order fakeOrder = new Order("O999", customer);
        fakeOrder.addOrderItem(new OrderItem(p1, "O999", 1));
        boolean edit8 = DataManager.edit(fakeOrder);
        System.out.println(edit8 ? FAIL + " ERREUR (devrait echouer)" : OK + " CORRECT (commande inexistante)");

        // ========================================
        // SECTION 5 : RESUME
        // ========================================
        System.out.println("\n================================================================");
        System.out.println("                    FIN DES TESTS");
        System.out.println("================================================================");
        System.out.println(OK + " Tous les tests de modification ont ete executes.");
    }
}