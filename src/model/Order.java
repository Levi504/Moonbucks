package model;

import java.util.List;
import java.util.ArrayList;

public class Order {
    //Fields
    private String orderID;
    private Customer customer;
    private List<OrderItem> orderItems;

    //Constructor
    public Order(String orderID, Customer customer) {
        setOrderID(orderID);
        setCustomer(customer);
        this.orderItems = new ArrayList<>();
    }

    //Getters
    public String getOrderID() {
        return orderID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getNumberOfItems() {
        return orderItems.size();
    }

    //Setters
    public void setOrderID(String newOrderID) {
        if (newOrderID == null || newOrderID.trim().isEmpty()) {
            throw new IllegalArgumentException("OrderID cannot be empty");
        }
        this.orderID = newOrderID;
    }

    public void setCustomer(Customer newCustomer) {
        if (newCustomer == null) {
            throw new IllegalArgumentException("Customer is missing");
        }
        this.customer = newCustomer;
    }

    //Methods
    public void addOrderItem(OrderItem newOrderItem) {
        if (newOrderItem == null) {
            throw new IllegalArgumentException("OrderItem cannot be empty");
        }
        this.orderItems.add(newOrderItem);
    }

    public void removeOrderItem(OrderItem existingOrderItem) {
        if (existingOrderItem == null || !orderItems.contains(existingOrderItem)) {
            throw new IllegalArgumentException("OrderItem is empty or not in this order");
        }
        this.orderItems.remove(existingOrderItem);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            Product p = item.getProduct();
            total += (p.getProductRate() + p.getPackageCharge()) * item.getQuantity();
        }
        return total;
    }
}