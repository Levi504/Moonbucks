package main.controller;

import data.DataManager;
import data.EntityType;
import data.CustomerView;
import data.ProductView;
import data.OrderView;
import model.Customer;
import model.Product;
import model.FragileProduct;
import model.NonFragileProduct;
import model.Order;
import model.OrderItem;
import main.util.InputHelper;

/**
 * Controller class for Order CRUD operations.
 * Handles all order-related actions for both Admin and Customer roles.
 */
public class OrderController {

    /**
     * Adds a new order to the system.
     */
    public static void addOrder() {
        System.out.println("\n--- Add a new order ---");
        System.out.println("\nAvailable customers:");
        CustomerView.viewAllCustomersCompact();
        
        String customerID = InputHelper.readInputWithCancel("\nCustomer ID");
        if (customerID == null) { System.out.println("Action cancelled."); return; }
        
        String customerLine = DataManager.searchSilent(EntityType.CUSTOMER, customerID);
        if (customerLine == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("\nAvailable products:");
        ProductView.viewAllProductsCompact();

        String[] parts = customerLine.split(",");
        Customer customer = new Customer(customerID, parts[1], parts[2], parts[3]);
        Order order = new Order("TEMP", customer);
        
        if (!addItemsToOrder(order, "TEMP")) {
            System.out.println("Action cancelled.");
            return;
        }
        DataManager.add(order);
    }

    /**
     * Deletes an order from the system.
     */
    public static void deleteOrder() {
        System.out.println("\n--- Delete an order ---");
        String id = InputHelper.readInputWithCancel("Order ID to delete");
        if (id == null) { System.out.println("Action cancelled."); return; }
        
        if (!InputHelper.confirmAction("Are you sure you want to delete order " + id + "?")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        DataManager.delete(EntityType.ORDER, id);
    }

    /**
     * Edits an existing order.
     */
    public static void editOrder() {
        System.out.println("\n--- Edit an order ---");
        String orderID = InputHelper.readInputWithCancel("Order ID to edit");
        if (orderID == null) { System.out.println("Action cancelled."); return; }
        
        String found = DataManager.searchSilent(EntityType.ORDER, orderID);
        if (found == null) return;
        
        System.out.println("\nAvailable customers:");
        CustomerView.viewAllCustomersCompact();
        
        String customerID = InputHelper.readInputWithCancel("\nNew customer ID (leave empty to keep current)");
        if (customerID == null) { System.out.println("Action cancelled."); return; }
        
        String[] parts = found.split(",");
        if (customerID.isEmpty()) customerID = parts[1];
        
        String customerLine = DataManager.searchSilent(EntityType.CUSTOMER, customerID);
        if (customerLine == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("\nAvailable products:");
        ProductView.viewAllProductsCompact();

        String[] custParts = customerLine.split(",");
        Customer customer = new Customer(customerID, custParts[1], custParts[2], custParts[3]);
        Order order = new Order(orderID, customer);
        
        if (!addItemsToOrder(order, orderID)) {
            System.out.println("Action cancelled.");
            return;
        }
        
        if (!InputHelper.confirmAction("Are you sure you want to save these changes?")) {
            System.out.println("Edition cancelled.");
            return;
        }
        DataManager.edit(order);
    }

    /**
     * Searches for an order by ID and displays its information.
     */
    public static void searchOrder() {
        System.out.println("\n--- Search an order ---");
        OrderView.viewOrderCompact(InputHelper.readInput("Order ID"));
    }

    /**
     * Views all orders for a specific customer (Admin function).
     */
    public static void viewOrdersByCustomer() {
        System.out.println("\n--- View orders by customer ---");
        OrderView.viewOrdersByCustomer(InputHelper.readInput("Customer ID"));
    }

    /**
     * Places a new order for the current customer (Customer-Only action).
     */
    public static void placeNewOrder(String currentCustomerID) {
        System.out.println("\n--- Place a New Order ---");
        System.out.println("Customer: " + currentCustomerID + " (you)");
        
        String customerLine = DataManager.searchSilent(EntityType.CUSTOMER, currentCustomerID);
        if (customerLine == null) return;

        System.out.println("\nAvailable products:");
        ProductView.viewAllProductsCompact();

        String[] parts = customerLine.split(",");
        Customer customer = new Customer(currentCustomerID, parts[1], parts[2], parts[3]);
        Order order = new Order("TEMP", customer);
        
        if (!addItemsToOrder(order, "TEMP")) {
            System.out.println("Action cancelled.");
            return;
        }
        DataManager.add(order);
    }

    /**
     * Views all orders belonging to the current customer (Customer-Only action).
     */
    public static void viewMyOrders(String currentCustomerID) {
        System.out.println("\n--- My Orders ---");
        OrderView.viewOrdersByCustomer(currentCustomerID);
    }

    /**
     * Edits an order belonging to the current customer (Customer-Only action).
     */
    public static void editMyOrder(String currentCustomerID) {
        System.out.println("\n--- My Orders ---");
        OrderView.viewOrdersByCustomer(currentCustomerID);
        
        String orderID = InputHelper.readInputWithCancel("\nEnter Order ID to edit");
        if (orderID == null) return;
        
        String found = DataManager.searchSilent(EntityType.ORDER, orderID);
        if (found == null || !found.split(",")[1].trim().equals(currentCustomerID)) {
            System.out.println("Error: This order does not belong to you.");
            return;
        }
        
        editOrder();
    }

    /**
     * Deletes an order belonging to the current customer (Customer-Only action).
     */
    public static void deleteMyOrder(String currentCustomerID) {
        System.out.println("\n--- My Orders ---");
        OrderView.viewOrdersByCustomer(currentCustomerID);
        
        String orderID = InputHelper.readInputWithCancel("\nEnter Order ID to delete");
        if (orderID == null) return;
        
        String found = DataManager.searchSilent(EntityType.ORDER, orderID);
        if (found == null || !found.split(",")[1].trim().equals(currentCustomerID)) {
            System.out.println("Error: This order does not belong to you.");
            return;
        }
        
        deleteOrder();
    }

    /**
     * Helper method to add items to an order interactively.
     */
    private static boolean addItemsToOrder(Order order, String orderID) {
        boolean addingItems = true;
        while (addingItems) {
            String productID = InputHelper.readInputWithCancel("\nProduct ID to add");
            if (productID == null) return false;
            
            String productLine = DataManager.searchSilent(EntityType.PRODUCT, productID);
            if (productLine == null) {
                System.out.println("Product not found.");
                continue;
            }

            String qtyStr = InputHelper.readInputWithCancel("Quantity");
            if (qtyStr == null) return false;
            
            int quantity;
            try {
                quantity = Integer.parseInt(qtyStr);
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity.");
                continue;
            }

            String[] prodParts = productLine.split(",");
            Product product = prodParts[4].trim().equals("FRAGILE")
                ? new FragileProduct(productID, prodParts[1].trim(), Double.parseDouble(prodParts[2].trim()))
                : new NonFragileProduct(productID, prodParts[1].trim(), Double.parseDouble(prodParts[2].trim()));

            order.addOrderItem(new OrderItem(product, orderID, quantity));
            
            String more = InputHelper.readInputWithCancel("Add another product? (y/n)");
            if (more == null) return false;
            if (!more.equalsIgnoreCase("y")) {
                addingItems = false;
            }
        }
        return true;
    }
}