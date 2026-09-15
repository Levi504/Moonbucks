package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataManager {
    //Fields
    private static final String CUSTOMER_FILE_PATH = "data/customers.txt";
    private static final String PRODUCT_FILE_PATH = "data/products.txt";
    private static final String ORDER_FILE_PATH = "data/orders.txt";

    //Methods
    public static void add() {

    }

    public static void delete() {

    }

    public static void edit() {

    }

    public static void view(EntityType type) {
        String path = "";
        if (type == EntityType.CUSTOMER) {
            path = CUSTOMER_FILE_PATH;
        } else if (type == EntityType.PRODUCT) {
            path = PRODUCT_FILE_PATH;
        } else if (type == EntityType.ORDER) {
            path = ORDER_FILE_PATH;
        }
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No " + type + " file found");
        }
    }

    public static void search() {

    }
}