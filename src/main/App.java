package main;

import data.CustomerView;
import data.OrderView;
import data.ProductView;
import main.controller.CustomerController;
import main.controller.OrderController;
import main.controller.ProductController;
import main.util.InputHelper;

/**
 * Main application class for the Moonbucks Retail Order Management System.
 * This class handles the user interface and menu navigation only.
 * Business logic is delegated to controller classes.
 */
public class App {
    private static String currentCustomerID = null;

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("     MOONBUCKS RETAIL ORDER MANAGEMENT SYSTEM");
        System.out.println("================================================================");
        
        boolean running = true;
        while (running) {
            System.out.println("\n================================================================");
            System.out.println("                    MAIN MENU");
            System.out.println("================================================================");
            System.out.println("  1. Admin Login\n  2. Customer Login\n  3. Exit");
            System.out.println("================================================================");
            
            switch (InputHelper.readChoice()) {
                case "1": adminMenu(); break;
                case "2": customerMenu(); break;
                case "3": 
                    System.out.println("\nThank you for using Moonbucks System!");
                    running = false; 
                    break;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
        InputHelper.close();
    }

    private static void adminMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n================================================================");
            System.out.println("                    ADMIN MENU");
            System.out.println("================================================================");
            System.out.println("  1. Manage Customers\n  2. Manage Products\n  3. Manage Orders\n  4. Logout");
            System.out.println("================================================================");
            
            switch (InputHelper.readChoice()) {
                case "1": customerManagementMenu(); break;
                case "2": productManagementMenu(); break;
                case "3": orderManagementMenu(); break;
                case "4": System.out.println("Admin logout successful."); inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerMenu() {
        System.out.println("\n--- Customer Login ---");
        System.out.println("\nAvailable customers:");
        CustomerView.viewAllCustomersCompact();
        
        String customerID = InputHelper.readInputWithCancel("\nEnter your Customer ID");
        if (customerID == null) return;
        
        String customerLine = data.DataManager.searchSilent(data.EntityType.CUSTOMER, customerID);
        if (customerLine == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        currentCustomerID = customerID;
        String[] parts = customerLine.split(",");
        System.out.println("\nWelcome, " + parts[1].trim() + "!");
        
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n================================================================");
            System.out.println("              CUSTOMER MENU - " + parts[1].trim().toUpperCase());
            System.out.println("================================================================");
            System.out.println("  1. View My Profile");
            System.out.println("  2. Edit My Profile");
            System.out.println("  3. View Available Products");
            System.out.println("  4. Place New Order");
            System.out.println("  5. View My Orders");
            System.out.println("  6. Edit My Order");
            System.out.println("  7. Delete My Order");
            System.out.println("  8. Logout");
            System.out.println("================================================================");
            
            switch (InputHelper.readChoice()) {
                case "1": CustomerView.viewCustomer(currentCustomerID); break;
                case "2": CustomerController.editMyProfile(currentCustomerID); break;
                case "3": ProductView.viewAllProductsCompact(); break;
                case "4": OrderController.placeNewOrder(currentCustomerID); break;
                case "5": OrderController.viewMyOrders(currentCustomerID); break;
                case "6": OrderController.editMyOrder(currentCustomerID); break;
                case "7": OrderController.deleteMyOrder(currentCustomerID); break;
                case "8": 
                    System.out.println("Logout successful.");
                    currentCustomerID = null;
                    inMenu = false; 
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerManagementMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n================================================================");
            System.out.println("              CUSTOMER MANAGEMENT");
            System.out.println("================================================================");
            System.out.println("  1. Add Customer\n  2. Delete Customer\n  3. Edit Customer");
            System.out.println("  4. View All Customers\n  5. Search Customer\n  6. Back");
            System.out.println("================================================================");
            
            switch (InputHelper.readChoice()) {
                case "1": CustomerController.addCustomer(); break;
                case "2": CustomerController.deleteCustomer(); break;
                case "3": CustomerController.editCustomer(); break;
                case "4": CustomerView.viewAllCustomersCompact(); break;
                case "5": CustomerController.searchCustomer(); break;
                case "6": inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void productManagementMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n================================================================");
            System.out.println("              PRODUCT MANAGEMENT");
            System.out.println("================================================================");
            System.out.println("  1. Add Product\n  2. Delete Product\n  3. Edit Product");
            System.out.println("  4. View All Products\n  5. Search Product\n  6. Back");
            System.out.println("================================================================");
            
            switch (InputHelper.readChoice()) {
                case "1": ProductController.addProduct(); break;
                case "2": ProductController.deleteProduct(); break;
                case "3": ProductController.editProduct(); break;
                case "4": ProductView.viewAllProductsCompact(); break;
                case "5": ProductController.searchProduct(); break;
                case "6": inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void orderManagementMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n================================================================");
            System.out.println("              ORDER MANAGEMENT");
            System.out.println("================================================================");
            System.out.println("  1. Add Order\n  2. Delete Order\n  3. Edit Order");
            System.out.println("  4. View All Orders\n  5. Search Order\n  6. View Orders by Customer\n  7. Back");
            System.out.println("================================================================");
            
            switch (InputHelper.readChoice()) {
                case "1": OrderController.addOrder(); break;
                case "2": OrderController.deleteOrder(); break;
                case "3": OrderController.editOrder(); break;
                case "4": OrderView.viewAllOrdersCompact(); break;
                case "5": OrderController.searchOrder(); break;
                case "6": OrderController.viewOrdersByCustomer(); break;
                case "7": inMenu = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}