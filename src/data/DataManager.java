package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import model.*;

public class DataManager {

    // ========================================
    // HELPER METHODS
    // ========================================

    public static String generateNextID(EntityType type) {
        File file = new File(type.getFilePath());
        if (!file.exists()) return null;

        int maxID = 0;
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    int currentID = Integer.parseInt(line.split(",")[0].substring(1));
                    if (currentID > maxID) maxID = currentID;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file for ID generation");
        }
        return type.getPrefix() + String.format("%03d", maxID + 1);
    }

    public static boolean customerExistsByPhone(String phone) {
        return existsInFile(EntityType.CUSTOMER, phone, 3, false);
    }

    public static boolean productExistsByName(String name) {
        return existsInFile(EntityType.PRODUCT, name, 1, true);
    }

    public static boolean customerExistsByID(String id) {
        return searchSilent(EntityType.CUSTOMER, id) != null;
    }

    /**
     * Generic method to check if a value exists in a file at a specific column
     */
    private static boolean existsInFile(EntityType type, String value, int column, boolean ignoreCase) {
        if (value == null || value.trim().isEmpty()) return false;
        File file = new File(type.getFilePath());
        if (!file.exists()) return false;

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length > column) {
                        String existing = parts[column].trim();
                        if (ignoreCase ? existing.equalsIgnoreCase(value) : existing.equals(value)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + type + " file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Silent search - no console output
     */
    public static String searchSilent(EntityType type, String id) {
        File file = new File(type.getFilePath());
        if (!file.exists()) return null;

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(id + ",")) return line;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    // ========================================
    // ADD METHODS
    // ========================================

    public static boolean add(Customer customer) {
        if (customerExistsByPhone(customer.getCustomerContact())) {
            System.out.println("Error: A customer with this phone number already exists.");
            return false;
        }
        String newID = generateNextID(EntityType.CUSTOMER);
        String line = newID + "," + customer.getCustomerName() + "," 
                    + customer.getCustomerAddress() + "," + customer.getCustomerContact();
        return writeToFile(EntityType.CUSTOMER, line, "Customer added successfully: " + newID);
    }

    public static boolean add(Product product) {
        if (productExistsByName(product.getProductName())) {
            System.out.println("Error: A product with this name already exists.");
            return false;
        }
        String newID = generateNextID(EntityType.PRODUCT);
        String line = newID + "," + product.getProductName() + "," 
                    + product.getProductRate() + "," + product.getPackageCharge() + "," + product.getType();
        return writeToFile(EntityType.PRODUCT, line, "Product added successfully: " + newID + " (" + product.getType() + ")");
    }

    public static boolean add(Order order) {
        if (order.getNumberOfItems() == 0) {
            System.out.println("Error: An order must contain at least one product.");
            return false;
        }
        String customerID = order.getCustomer().getCustomerID();
        if (!customerExistsByID(customerID)) {
            System.out.println("Error: Customer " + customerID + " does not exist.");
            return false;
        }

        String newOrderID = generateNextID(EntityType.ORDER);
        double total = order.calculateTotal();
        
        // Write order
        String orderLine = newOrderID + "," + customerID + "," + order.getNumberOfItems() + "," 
                         + String.format("%.2f", total).replace(",", ".");
        if (!writeToFile(EntityType.ORDER, orderLine, null)) return false;

        // Write order items
        try (PrintWriter writer = new PrintWriter(new FileWriter(EntityType.ORDER_ITEM.getFilePath(), true), true)) {
            for (OrderItem item : order.getOrderItems()) {
                writer.println(newOrderID + "," + item.getProduct().getProductID() + "," + item.getQuantity());
            }
        } catch (IOException e) {
            System.out.println("Error writing order items: " + e.getMessage());
            return false;
        }

        System.out.println("Order added successfully: " + newOrderID + " (Total: " + String.format("%.2f", total).replace(",", ".") + ")");
        return true;
    }

    /**
     * Generic method to write a line to a file
     */
    private static boolean writeToFile(EntityType type, String line, String successMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(type.getFilePath(), true), true)) {
            writer.println(line);
            if (successMessage != null) System.out.println(successMessage);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to " + type + " file: " + e.getMessage());
            return false;
        }
    }

    // ========================================
    // DELETE METHODS
    // ========================================

    public static boolean delete(EntityType type, String id) {
        if (searchSilent(type, id) == null) {
            System.out.println("Error: " + type + " with ID " + id + " does not exist.");
            return false;
        }
        if (!deleteFromFile(type, id)) return false;
        if (type == EntityType.ORDER) deleteOrderItems(id);
        System.out.println(type + " " + id + " deleted successfully.");
        return true;
    }

    private static void deleteOrderItems(String orderID) {
        deleteFromFile(EntityType.ORDER_ITEM, orderID);
    }

    /**
     * Generic method to delete lines starting with an ID from a file
     */
    private static boolean deleteFromFile(EntityType type, String id) {
        File originalFile = new File(type.getFilePath());
        File tempFile = new File(type.getFilePath() + ".tmp");

        try (Scanner scanner = new Scanner(originalFile, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile), true)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty() || !line.startsWith(id + ",")) {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error deleting from " + type + " file: " + e.getMessage());
            return false;
        }

        originalFile.delete();
        tempFile.renameTo(originalFile);
        return true;
    }

    // ========================================
    // EDIT METHODS
    // ========================================

    public static boolean edit(Customer customer) {
        String id = customer.getCustomerID();
        String existingLine = searchSilent(EntityType.CUSTOMER, id);
        
        if (existingLine == null) {
            System.out.println("Error: Customer " + id + " does not exist.");
            return false;
        }

        String oldPhone = existingLine.split(",")[3].trim();
        String newPhone = customer.getCustomerContact();
        if (!oldPhone.equals(newPhone) && customerExistsByPhone(newPhone)) {
            System.out.println("Error: Another customer already uses this phone number.");
            return false;
        }

        delete(EntityType.CUSTOMER, id);
        String line = id + "," + customer.getCustomerName() + "," 
                    + customer.getCustomerAddress() + "," + customer.getCustomerContact();
        return writeToFile(EntityType.CUSTOMER, line, "Customer " + id + " modified successfully.");
    }

    public static boolean edit(Product product) {
        String id = product.getProductID();
        String existingLine = searchSilent(EntityType.PRODUCT, id);
        
        if (existingLine == null) {
            System.out.println("Error: Product " + id + " does not exist.");
            return false;
        }

        String oldName = existingLine.split(",")[1].trim();
        String newName = product.getProductName();
        if (!oldName.equalsIgnoreCase(newName) && productExistsByName(newName)) {
            System.out.println("Error: Another product already has this name.");
            return false;
        }

        delete(EntityType.PRODUCT, id);
        String line = id + "," + product.getProductName() + "," + product.getProductRate() + "," 
                    + product.getPackageCharge() + "," + product.getType();
        return writeToFile(EntityType.PRODUCT, line, "Product " + id + " modified successfully.");
    }

    public static boolean edit(Order order) {
        String id = order.getOrderID();
        if (searchSilent(EntityType.ORDER, id) == null) {
            System.out.println("Error: Order " + id + " does not exist.");
            return false;
        }
        if (order.getNumberOfItems() == 0) {
            System.out.println("Error: An order must contain at least one product.");
            return false;
        }
        String customerID = order.getCustomer().getCustomerID();
        if (!customerExistsByID(customerID)) {
            System.out.println("Error: Customer " + customerID + " does not exist.");
            return false;
        }

        delete(EntityType.ORDER, id);
        double total = order.calculateTotal();

        String orderLine = id + "," + customerID + "," + order.getNumberOfItems() + "," 
                         + String.format("%.2f", total).replace(",", ".");
        if (!writeToFile(EntityType.ORDER, orderLine, null)) return false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(EntityType.ORDER_ITEM.getFilePath(), true), true)) {
            for (OrderItem item : order.getOrderItems()) {
                writer.println(id + "," + item.getProduct().getProductID() + "," + item.getQuantity());
            }
        } catch (IOException e) {
            System.out.println("Error modifying order items: " + e.getMessage());
            return false;
        }

        System.out.println("Order " + id + " modified successfully. (Total: " + String.format("%.2f", total).replace(",", ".") + ")");
        return true;
    }

    // ========================================
    // VIEW AND SEARCH METHODS
    // ========================================

    public static void view(EntityType type) {
        File file = new File(type.getFilePath());
        if (!file.exists()) {
            System.out.println("No " + type + " file found");
            return;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            System.out.println("=====" + type + " List=====");
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("No " + type + " file found");
        }
    }

    public static String search(EntityType type, String typeID) {
        File file = new File(type.getFilePath());
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
        } catch (IOException e) {
            System.out.println("No " + type + " file found");
            return null;
        }

        System.out.println("No " + type + " element found with ID: " + typeID);
        return null;
    }
}