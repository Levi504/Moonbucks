package model;

public class NonFragileProduct extends Product {
    //Fields
    private double packageCharge;

    //Constructor
    public NonFragileProduct(String productID,
                        String productName,
                        double productRate,
                        double packageCharge) {
        super(productID, productName, productRate);
        setPackageCharge(packageCharge);
        setProductRate(getProductRate() + packageCharge);
    }

    //Getters
    public double getPackageCharge() {
        return packageCharge;
    }

    //Setters
    public void setPackageCharge(double newPackageCharge) {
        if (newPackageCharge < 0) {
            throw new IllegalArgumentException("Package Charge cannot be below 0");
        }
        this.packageCharge = newPackageCharge;
    }
}