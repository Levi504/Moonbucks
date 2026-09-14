package model;

public class Product {
    //Fields
    private String productID;
    private String productName;
    private double productRate;

    //Constructor
    public Product(String productID, String productName, double productRate) {
        this.productID = productID;
        setProductName(productName);
        setProductRate(productRate);

    }

    //Getters
    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductRate() {
        return productRate;
    }

    //Setters
    public void setProductName(String newProductName) {
        this.productName = newProductName;
    }

    public void setProductRate(double newProductRate) {
        this.productRate = newProductRate;
    }
}