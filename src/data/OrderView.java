package data;

import java.util.Locale;

public class OrderView {

    public static void viewAllOrdersCompact() {
        System.out.println("\n============================================================================================================================");
        System.out.printf("%-8s | %-10s | %-20s | %-45s | %-10s%n", "OrderID", "CustID", "Customer", "Items", "Total");
        System.out.println("---------|------------|----------------------|-----------------------------------------------|------------");

        boolean hasOrders = FileUtils.readLines(EntityType.ORDER, line -> {
            String[] parts = line.split(",");
            String orderID = parts[0].trim();
            String customerID = parts[1].trim();
            String total = parts[3].trim();

            String customerLine = FileUtils.findLineById(EntityType.CUSTOMER, customerID);
            String customerName = customerLine != null ? customerLine.split(",")[1].trim() : "Unknown";
            String itemsStr = getItemsCompact(orderID);

            System.out.printf("%-8s | %-10s | %-20s | %-45s | %-10s%n",
                    orderID, customerID, customerName, itemsStr, total);
        });

        System.out.println("============================================================================================================================");
        if (!hasOrders) System.out.println("No orders found.");
    }

    public static void viewOrderCompact(String orderID) {
        String orderLine = FileUtils.findLineById(EntityType.ORDER, orderID);
        if (orderLine == null) {
            System.out.println("No order found with ID: " + orderID);
            return;
        }

        String[] parts = orderLine.split(",");
        String customerID = parts[1].trim();
        String total = parts[3].trim();

        String customerLine = FileUtils.findLineById(EntityType.CUSTOMER, customerID);
        String customerName = customerLine != null ? customerLine.split(",")[1].trim() : "Unknown";
        String itemsStr = getItemsCompact(orderID);

        System.out.println("\n============================================================================================================================");
        System.out.printf("%-8s | %-10s | %-20s | %-45s | %-10s%n", "OrderID", "CustID", "Customer", "Items", "Total");
        System.out.println("---------|------------|----------------------|-----------------------------------------------|------------");
        System.out.printf("%-8s | %-10s | %-20s | %-45s | %-10s%n", orderID, customerID, customerName, itemsStr, total);
        System.out.println("============================================================================================================================");
    }

    public static void viewOrdersByCustomer(String customerID) {
        System.out.println("\n============================================================================================================================");
        System.out.printf("%-8s | %-10s | %-20s | %-45s | %-10s%n", "OrderID", "CustID", "Customer", "Items", "Total");
        System.out.println("---------|------------|----------------------|-----------------------------------------------|------------");

        boolean hasOrders = FileUtils.readLines(EntityType.ORDER, line -> {
            String[] parts = line.split(",");
            String orderID = parts[0].trim();
            String orderCustomerID = parts[1].trim();

            if (orderCustomerID.equals(customerID)) {
                String total = parts[3].trim();
                String customerLine = FileUtils.findLineById(EntityType.CUSTOMER, customerID);
                String customerName = customerLine != null ? customerLine.split(",")[1].trim() : "Unknown";
                String itemsStr = getItemsCompact(orderID);

                System.out.printf("%-8s | %-10s | %-20s | %-45s | %-10s%n",
                        orderID, customerID, customerName, itemsStr, total);
            }
        });

        System.out.println("============================================================================================================================");
        if (!hasOrders) System.out.println("No orders found for customer " + customerID + ".");
    }

    private static String getItemsCompact(String orderID) {
        StringBuilder sb = new StringBuilder();
        
        FileUtils.readLines(EntityType.ORDER_ITEM, line -> {
            if (line.startsWith(orderID + ",")) {
                String[] parts = line.split(",");
                String productID = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());

                String productLine = FileUtils.findLineById(EntityType.PRODUCT, productID);
                String productName = productLine != null ? productLine.split(",")[1].trim() : "Unknown";

                if (sb.length() > 0) sb.append(", ");
                sb.append(productName).append(" x").append(quantity);
            }
        });

        return sb.length() > 0 ? sb.toString() : "No items";
    }
}