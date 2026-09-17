package data;

public enum EntityType {
    CUSTOMER("data/customers.txt", "C"),
    PRODUCT("data/products.txt", "P"),
    ORDER("data/orders.txt", "O"),
    ORDER_ITEM("data/order_items.txt", "OI");
    
    private final String filePath;
    private final String prefix;
    
    EntityType(String filePath, String prefix) {
        this.filePath = filePath;
        this.prefix = prefix;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public String getPrefix() {
        return prefix;
    }
}