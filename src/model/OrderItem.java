package model;

public class OrderItem{
    //Fields
    private String orderID;
    private Product product;
    private int quantity;

    //Constructor
    public OrderItem(Product product, String orderID, int quantity) {
        setOrderID(orderID);
        setProduct(product);
        setQuantity(quantity);
    }

    //Getters
    public String getOrderID() {
        return orderID;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    //Setters
    public void setOrderID(String newOrderID) {
        if(newOrderID == null || newOrderID.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderID = newOrderID;
    }

    public void setProduct(Product newProduct) {
        if(newProduct == null) {
            throw new IllegalArgumentException("Product is missing");
        }
        this.product = newProduct;
    }

    public void setQuantity(int newQuantity) {
        if(newQuantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0");
        }
        this.quantity = newQuantity;
    }
}