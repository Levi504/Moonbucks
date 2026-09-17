package data;

import java.util.Locale;

public class ProductView {

    public static void viewAllProductsCompact() {
        System.out.println("\n========================================================================================================");
        System.out.printf(Locale.US, "%-10s | %-25s | %-10s | %-12s | %-12s%n", "ID", "Name", "Price", "Pkg Charge", "Type");
        System.out.println("-----------|---------------------------|------------|--------------|-------------");

        boolean hasProducts = FileUtils.readLines(EntityType.PRODUCT, line -> {
            String[] parts = line.split(",");
            System.out.printf(Locale.US, "%-10s | %-25s | %-10.2f | %-12.2f | %-12s%n",
                    parts[0].trim(), parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    Double.parseDouble(parts[3].trim()),
                    parts[4].trim());
        });

        System.out.println("========================================================================================================");
        if (!hasProducts) System.out.println("No products found.");
    }

    public static void viewProduct(String productID) {
        String line = FileUtils.findLineById(EntityType.PRODUCT, productID);
        if (line == null) {
            System.out.println("No product found with ID: " + productID);
            return;
        }

        String[] parts = line.split(",");
        System.out.println("\n========================================================================================================");
        System.out.printf(Locale.US, "%-10s | %-25s | %-10s | %-12s | %-12s%n", "ID", "Name", "Price", "Pkg Charge", "Type");
        System.out.println("-----------|---------------------------|------------|--------------|-------------");
        System.out.printf(Locale.US, "%-10s | %-25s | %-10.2f | %-12.2f | %-12s%n",
                parts[0].trim(), parts[1].trim(),
                Double.parseDouble(parts[2].trim()),
                Double.parseDouble(parts[3].trim()),
                parts[4].trim());
        System.out.println("========================================================================================================");
    }
}