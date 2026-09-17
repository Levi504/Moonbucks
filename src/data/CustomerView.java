package data;

public class CustomerView {

    public static void viewAllCustomersCompact() {
        System.out.println("\n========================================================================================================");
        System.out.printf("%-10s | %-25s | %-40s | %-15s%n", "ID", "Name", "Address", "Phone");
        System.out.println("-----------|---------------------------|------------------------------------------|----------------");

        boolean hasCustomers = FileUtils.readLines(EntityType.CUSTOMER, line -> {
            String[] parts = line.split(",");
            System.out.printf("%-10s | %-25s | %-40s | %-15s%n",
                    parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
        });

        System.out.println("========================================================================================================");
        if (!hasCustomers) System.out.println("No customers found.");
    }

    public static void viewCustomer(String customerID) {
        String line = FileUtils.findLineById(EntityType.CUSTOMER, customerID);
        if (line == null) {
            System.out.println("No customer found with ID: " + customerID);
            return;
        }

        String[] parts = line.split(",");
        System.out.println("\n========================================================================================================");
        System.out.printf("%-10s | %-25s | %-40s | %-15s%n", "ID", "Name", "Address", "Phone");
        System.out.println("-----------|---------------------------|------------------------------------------|----------------");
        System.out.printf("%-10s | %-25s | %-40s | %-15s%n",
                parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
        System.out.println("========================================================================================================");
    }
}