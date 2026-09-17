package model;

public class NonFragileProduct extends Product {
    public static final double NON_FRAGILE_CHARGE = 2.00;

    public NonFragileProduct(String productID, String productName, double productRate) {
        super(productID, productName, productRate, NON_FRAGILE_CHARGE);
    }

    @Override
    public String getType() {
        return "NON_FRAGILE";
    }
}