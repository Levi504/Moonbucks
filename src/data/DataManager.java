package data;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import model.Customer;
import model.Product;
import model.Order;
import model.OrderItem;

public class DataManager {

    //Helper Methods
    public static String generateNextID(EntityType type) {
        String path = type.getFilePath();
        String prefix = type.getPrefix();
        File file = new File(path);

        int maxID = 0;

        if (!file.exists()) {
            System.out.println("No " + type + " file found");
            return null;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String existingID = parts[0];
                    String numberPart = existingID.substring(1);
                    int currentID = Integer.parseInt(numberPart);

                    if (currentID > maxID) {
                        maxID = currentID;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file for ID generation");
        }

        int nextID = maxID + 1;
        return prefix + String.format("%03d", nextID);
    }


    public static boolean customerExistsByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        File file = new File(EntityType.CUSTOMER.getFilePath());

        if (!file.exists()) {
            return false;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                
                if (parts.length >= 4) {
                    String existingPhone = parts[3].trim();
                    
                    if (existingPhone.equals(phone)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customer file: " + e.getMessage());
        }

        return false;
    }


    public static boolean productExistsByName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return false;
        }

        File file = new File(EntityType.PRODUCT.getFilePath());

        if (!file.exists()) {
            return false;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length >= 2) {
                    String existingName = parts[1].trim();
                    if (existingName.equalsIgnoreCase(productName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading product file: " + e.getMessage());
        }

        return false;
    }


    public static boolean customerExistsByID(String customerID) {
        return search(EntityType.CUSTOMER, customerID) != null;
    }


    private static void deleteOrderItems(String orderID) {
        String path = EntityType.ORDER_ITEM.getFilePath();
        File originalFile = new File(path);
        File tempFile = new File(path + ".tmp");

        if (!originalFile.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(originalFile, StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile), true)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isEmpty() || !line.startsWith(orderID + ",")) {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Erreur lors de la suppression des OrderItems : " + e.getMessage());
            return;
        }

        originalFile.delete();
        tempFile.renameTo(originalFile);
    }


    //Methods
    public static boolean add(Customer customer) {
        if (customerExistsByPhone(customer.getCustomerContact())) {
            System.out.println("Erreur : Un client avec ce numéro de téléphone existe déjà.");
            return false;
        }
        
        String newID = generateNextID(EntityType.CUSTOMER);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(EntityType.CUSTOMER.getFilePath(), true), true)) {

            File file = new File(EntityType.CUSTOMER.getFilePath());

            String line = newID + "," 
                        + customer.getCustomerName() + "," 
                        + customer.getCustomerAddress() + "," 
                        + customer.getCustomerContact();
            writer.println(line);

            System.out.println("Client ajouté avec succès : " + newID);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture : " + e.getMessage());
            return false;
        }
    }


    public static boolean add(Product product) {
        if (productExistsByName(product.getProductName())) {
            System.out.println("Erreur : Un produit avec ce nom existe déjà.");
            return false;
        }

        String newID = generateNextID(EntityType.PRODUCT);

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.PRODUCT.getFilePath(), true), true)) {
            
            String line = newID + ","
                    + product.getProductName() + ","
                    + product.getProductRate() + ","
                    + product.getPackageCharge() + ","
                    + product.getType();
            writer.println(line);

            System.out.println("Produit ajouté avec succès : " + newID + " (" + product.getType() + ")");
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture : " + e.getMessage());
            return false;
        }
    }


    public static boolean add(Order order) {
        if (order.getNumberOfItems() == 0) {
            System.out.println("Erreur : Une commande doit contenir au moins un produit.");
            return false;
        }

        String customerID = order.getCustomer().getCustomerID();
        if (!customerExistsByID(customerID)) {
            System.out.println("Erreur : Le client " + customerID + " n'existe pas.");
            return false;
        }

        String newOrderID = generateNextID(EntityType.ORDER);

        double total = order.calculateTotal();

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.ORDER.getFilePath(), true), true)) {
            
            String line = newOrderID + ","
                    + customerID + ","
                    + order.getNumberOfItems() + ","
                    + String.format("%.2f", total).replace(",", ".");
            writer.println(line);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans orders.txt : " + e.getMessage());
            return false;
        }

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.ORDER_ITEM.getFilePath(), true), true)) {
            
            for (OrderItem item : order.getOrderItems()) {
                // Format : OrderID,ProductID,Quantity
                String line = newOrderID + ","
                        + item.getProduct().getProductID() + ","
                        + item.getQuantity();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans order_items.txt : " + e.getMessage());
            return false;
        }

        System.out.println("Commande ajoutée avec succès : " + newOrderID + " (Total : " + String.format("%.2f", total).replace(",", ".") + ")");
        return true;
    }

    public static boolean delete(EntityType type, String id) {
        String found = search(type, id);
        if (found == null) {
            System.out.println("Erreur : " + type + " avec l'ID " + id + " n'existe pas.");
            return false;
        }

        String path = type.getFilePath();
        File originalFile = new File(path);
        File tempFile = new File(path + ".tmp");

        try (Scanner scanner = new Scanner(originalFile, StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile), true)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().isEmpty() || !line.startsWith(id + ",")) {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture : " + e.getMessage());
            return false;
        }

        originalFile.delete();
        tempFile.renameTo(originalFile);

        if (type == EntityType.ORDER) {
            deleteOrderItems(id);
        }

        System.out.println(type + " " + id + " supprimé avec succès.");
        return true;
    }


    public static boolean edit(Customer customer) {
        String customerID = customer.getCustomerID();

        if (!customerExistsByID(customerID)) {
            System.out.println("Erreur : Le client " + customerID + " n'existe pas.");
            return false;
        }

        String existingLine = search(EntityType.CUSTOMER, customerID);
        String oldPhone = existingLine.split(",")[3].trim();
        String newPhone = customer.getCustomerContact();

        if (!oldPhone.equals(newPhone) && customerExistsByPhone(newPhone)) {
            System.out.println("Erreur : Un autre client utilise déjà ce numéro de téléphone.");
            return false;
        }

        delete(EntityType.CUSTOMER, customerID);

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.CUSTOMER.getFilePath(), true), true)) {
            String line = customerID + ","
                    + customer.getCustomerName() + ","
                    + customer.getCustomerAddress() + ","
                    + customer.getCustomerContact();
            writer.println(line);
            System.out.println("Client " + customerID + " modifié avec succès.");
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }


    public static boolean edit(Product product) {
        String productID = product.getProductID();

        // Étape 1 : Vérifier que le produit existe
        String existingLine = search(EntityType.PRODUCT, productID);
        if (existingLine == null) {
            System.out.println("Erreur : Le produit " + productID + " n'existe pas.");
            return false;
        }

        // Étape 2 : Vérifier que le nouveau nom n'est pas déjà utilisé par un AUTRE produit
        String oldName = existingLine.split(",")[1].trim();
        String newName = product.getProductName();

        if (!oldName.equalsIgnoreCase(newName) && productExistsByName(newName)) {
            System.out.println("Erreur : Un autre produit porte déjà ce nom.");
            return false;
        }

        // Étape 3 : Supprimer l'ancien produit
        delete(EntityType.PRODUCT, productID);

        // Étape 4 : Ajouter le nouveau produit (avec le même ID)
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.PRODUCT.getFilePath(), true), true)) {
            String line = productID + ","
                    + product.getProductName() + ","
                    + product.getProductRate() + ","
                    + product.getPackageCharge() + ","
                    + product.getType();
            writer.println(line);
            System.out.println("Produit " + productID + " modifié avec succès.");
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }


    public static boolean edit(Order order) {
        String orderID = order.getOrderID();

        // Étape 1 : Vérifier que la commande existe
        if (search(EntityType.ORDER, orderID) == null) {
            System.out.println("Erreur : La commande " + orderID + " n'existe pas.");
            return false;
        }

        // Étape 2 : Vérifier que la commande a au moins un item
        if (order.getNumberOfItems() == 0) {
            System.out.println("Erreur : Une commande doit contenir au moins un produit.");
            return false;
        }

        // Étape 3 : Vérifier que le client existe
        String customerID = order.getCustomer().getCustomerID();
        if (!customerExistsByID(customerID)) {
            System.out.println("Erreur : Le client " + customerID + " n'existe pas.");
            return false;
        }

        // Étape 4 : Supprimer l'ancienne commande (et ses OrderItems en cascade)
        delete(EntityType.ORDER, orderID);

        // Étape 5 : Calculer le nouveau total
        double total = order.calculateTotal();

        // Étape 6 : Écrire la nouvelle commande dans orders.txt
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.ORDER.getFilePath(), true), true)) {
            String line = orderID + ","
                    + customerID + ","
                    + order.getNumberOfItems() + ","
                    + String.format("%.2f", total).replace(",", ".");
            writer.println(line);
        } catch (IOException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }

        // Étape 7 : Écrire les nouveaux OrderItems dans order_items.txt
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EntityType.ORDER_ITEM.getFilePath(), true), true)) {
            for (OrderItem item : order.getOrderItems()) {
                String line = orderID + ","
                        + item.getProduct().getProductID() + ","
                        + item.getQuantity();
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la modification des OrderItems : " + e.getMessage());
            return false;
        }

        System.out.println("Commande " + orderID + " modifiée avec succès. (Total : " + String.format("%.2f", total).replace(",", ".") + ")");
        return true;
    }

    public static void view(EntityType type) {
        String path = type.getFilePath();
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("No " + type + " file found");
            return;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            System.out.println("=====" + type + " List=====");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("No " + type + " file found");
        }
    }


    public static String search(EntityType type, String typeID) {
        String path = type.getFilePath();
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("No " + type + " file found");
            return null;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(typeID + ",")) {
                    System.out.println("=====Found " + type + " Element=====");
                    System.out.println(line);
                    return line;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("No " + type + " file found");
            return null;
        }

        System.out.println("No " + type + " element found with ID: " + typeID);
        return null;
    }
}