package main.controller;

import data.DataManager;
import data.EntityType;
import data.CustomerView;
import model.Customer;
import main.util.InputHelper;

/**
 * Controller class for Customer CRUD operations.
 * Handles all customer-related actions for both Admin and Customer roles.
 */
public class CustomerController {

    /**
     * Adds a new customer to the system.
     */
    public static void addCustomer() {
        System.out.println("\n--- Add a new customer ---");
        String name = InputHelper.readInputWithCancel("Name");
        if (name == null) { System.out.println("Action cancelled."); return; }
        
        String address = InputHelper.readInputWithCancel("Address");
        if (address == null) { System.out.println("Action cancelled."); return; }
        
        String phone = InputHelper.readInputWithCancel("Phone");
        if (phone == null) { System.out.println("Action cancelled."); return; }
        
        DataManager.add(new Customer("TEMP", name, address, phone));
    }

    /**
     * Deletes a customer from the system.
     */
    public static void deleteCustomer() {
        System.out.println("\n--- Delete a customer ---");
        String id = InputHelper.readInputWithCancel("Customer ID to delete");
        if (id == null) { System.out.println("Action cancelled."); return; }
        
        if (!InputHelper.confirmAction("Are you sure you want to delete customer " + id + "?")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        DataManager.delete(EntityType.CUSTOMER, id);
    }

    /**
     * Edits an existing customer's information.
     */
    public static void editCustomer() {
        System.out.println("\n--- Edit a customer ---");
        String id = InputHelper.readInputWithCancel("Customer ID to edit");
        if (id == null) { System.out.println("Action cancelled."); return; }
        
        String found = DataManager.searchSilent(EntityType.CUSTOMER, id);
        if (found == null) return;

        String[] parts = found.split(",");
        String name = InputHelper.readInputWithCancel("New name (leave empty to keep current)");
        if (name == null) { System.out.println("Action cancelled."); return; }
        
        String address = InputHelper.readInputWithCancel("New address (leave empty to keep current)");
        if (address == null) { System.out.println("Action cancelled."); return; }
        
        String phone = InputHelper.readInputWithCancel("New phone (leave empty to keep current)");
        if (phone == null) { System.out.println("Action cancelled."); return; }
        
        if (name.isEmpty()) name = parts[1];
        if (address.isEmpty()) address = parts[2];
        if (phone.isEmpty()) phone = parts[3];
        
        if (!InputHelper.confirmAction("Are you sure you want to save these changes?")) {
            System.out.println("Edition cancelled.");
            return;
        }
        DataManager.edit(new Customer(id, name, address, phone));
    }

    /**
     * Searches for a customer by ID and displays their information.
     */
    public static void searchCustomer() {
        System.out.println("\n--- Search a customer ---");
        CustomerView.viewCustomer(InputHelper.readInput("Customer ID"));
    }

    /**
     * Edits the current customer's profile (Customer-Only action).
     */
    public static void editMyProfile(String currentCustomerID) {
        System.out.println("\n--- Edit My Profile ---");
        String found = DataManager.searchSilent(EntityType.CUSTOMER, currentCustomerID);
        if (found == null) return;

        String[] parts = found.split(",");
        String name = InputHelper.readInputWithCancel("New name (leave empty to keep current)");
        if (name == null) { System.out.println("Action cancelled."); return; }
        
        String address = InputHelper.readInputWithCancel("New address (leave empty to keep current)");
        if (address == null) { System.out.println("Action cancelled."); return; }
        
        String phone = InputHelper.readInputWithCancel("New phone (leave empty to keep current)");
        if (phone == null) { System.out.println("Action cancelled."); return; }
        
        if (name.isEmpty()) name = parts[1];
        if (address.isEmpty()) address = parts[2];
        if (phone.isEmpty()) phone = parts[3];
        
        if (!InputHelper.confirmAction("Are you sure you want to save these changes?")) {
            System.out.println("Edition cancelled.");
            return;
        }
        DataManager.edit(new Customer(currentCustomerID, name, address, phone));
    }
}