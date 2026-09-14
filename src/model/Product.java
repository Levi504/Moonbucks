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
        if (newProductName == null || newProductName.trim().isEmpty()) {
            throw new IllegalArgumentException("product name cannot be empty");
        }
        this.productName = newProductName;
    }

    public void setProductRate(double newProductRate) {
        if (newProductRate < 0) {
            throw new IllegalArgumentException("product rate cannot be below 0");
        }
        this.productRate = newProductRate;
    }
}