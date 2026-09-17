package main;

import data.DataManager;
import data.EntityType;
import model.Customer;
import model.Product;
import model.FragileProduct;
import model.NonFragileProduct;

public class Tests {
    public static void main(String[] args) {
         System.out.println("\n========================================");
         System.out.println("=== TEST 4 : Ajouter un DOUBLON (même nom) ===");
         System.out.println("========================================");
         // "Tea" existe dans le fichier → doit être détecté comme doublon
         Product product3 = new NonFragileProduct("TEMP", "Tea", 20.00);
         boolean success3 = DataManager.add(product3);
         System.out.println("Résultat : " + (success3 ? "SUCCÈS" : "ÉCHEC (attendu)"));
    }
}