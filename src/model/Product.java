package model;

public class Product {
    //Fields
    private String productID;
    private String productName;
    private double productRate;
    private double packageCharge;

    //Constructor
    public Product(String productID, String productName, double productRate, double packageCharge) {
        this.productID = productID;
        setProductName(productName);
        setProductRate(productRate);
        setPackageCharge(packageCharge);

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

    public double getPackageCharge() {
        return packageCharge;
    }

    //Polymorph Method
    public String getType() {
        return "UNKNOWN"; 
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

    public void setPackageCharge(double newPackageCharge) {
        if (newPackageCharge < 0) {
            throw new IllegalArgumentException("Les frais d'emballage ne peuvent pas être négatifs.");
        }
        this.packageCharge = newPackageCharge;
    }
}