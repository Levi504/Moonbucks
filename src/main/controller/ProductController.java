package main.controller;

import data.DataManager;
import data.EntityType;
import data.ProductView;
import model.Product;
import model.FragileProduct;
import model.NonFragileProduct;
import main.util.InputHelper;

/**
 * Controller class for Product CRUD operations.
 * Handles all product-related actions.
 */
public class ProductController {

    /**
     * Adds a new product to the system.
     */
    public static void addProduct() {
        System.out.println("\n--- Add a new product ---");
        String name = InputHelper.readInputWithCancel("Name");
        if (name == null) { System.out.println("Action cancelled."); return; }
        
        String priceStr = InputHelper.readInputWithCancel("Price");
        if (priceStr == null) { System.out.println("Action cancelled."); return; }
        
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }
        
        String fragile = InputHelper.readInputWithCancel("Is the product fragile? (y/n)");
        if (fragile == null) { System.out.println("Action cancelled."); return; }
        
        Product product = fragile.equalsIgnoreCase("y") 
            ? new FragileProduct("TEMP", name, price)
            : new NonFragileProduct("TEMP", name, price);
        DataManager.add(product);
    }

    /**
     * Deletes a product from the system.
     */
    public static void deleteProduct() {
        System.out.println("\n--- Delete a product ---");
        String id = InputHelper.readInputWithCancel("Product ID to delete");
        if (id == null) { System.out.println("Action cancelled."); return; }
        
        if (!InputHelper.confirmAction("Are you sure you want to delete product " + id + "?")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        DataManager.delete(EntityType.PRODUCT, id);
    }

    /**
     * Edits an existing product's information.
     */
    public static void editProduct() {
        System.out.println("\n--- Edit a product ---");
        String id = InputHelper.readInputWithCancel("Product ID to edit");
        if (id == null) { System.out.println("Action cancelled."); return; }
        
        String found = DataManager.searchSilent(EntityType.PRODUCT, id);
        if (found == null) return;

        String[] parts = found.split(",");
        String name = InputHelper.readInputWithCancel("New name (leave empty to keep current)");
        if (name == null) { System.out.println("Action cancelled."); return; }
        
        String priceStr = InputHelper.readInputWithCancel("New price (leave empty to keep current)");
        if (priceStr == null) { System.out.println("Action cancelled."); return; }
        
        if (name.isEmpty()) name = parts[1];
        double price = priceStr.isEmpty() ? Double.parseDouble(parts[2]) : Double.parseDouble(priceStr);
        
        if (!InputHelper.confirmAction("Are you sure you want to save these changes?")) {
            System.out.println("Edition cancelled.");
            return;
        }
        
        Product product = parts[4].equals("FRAGILE")
            ? new FragileProduct(id, name, price)
            : new NonFragileProduct(id, name, price);
        DataManager.edit(product);
    }

    /**
     * Searches for a product by ID and displays its information.
     */
    public static void searchProduct() {
        System.out.println("\n--- Search a product ---");
        ProductView.viewProduct(InputHelper.readInput("Product ID"));
    }
}