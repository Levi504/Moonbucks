package model;

public class FragileProduct extends Product {
    public static final double FRAGILE_CHARGE = 5.00;

    public FragileProduct(String productID, String productName, double productRate) {
        super(productID, productName, productRate, FRAGILE_CHARGE);
    }

    @Override
    public String getType() {
        return "FRAGILE";
    }
}