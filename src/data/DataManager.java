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

    public static void delete() {

    }

    public static void edit() {

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