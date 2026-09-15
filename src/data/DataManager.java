package data;

import java.io.File;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

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

    public static String pathSetter(EntityType type){
        String path = "";
        if (type == EntityType.CUSTOMER) {
            path = CUSTOMER_FILE_PATH;
        } else if (type == EntityType.PRODUCT) {
            path = PRODUCT_FILE_PATH;
        } else if (type == EntityType.ORDER) {
            path = ORDER_FILE_PATH;
        }
        return path;
    }

    public static void view(EntityType type) {
        String path = pathSetter(type);
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("No " + type + " file found");
            return;
        }
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            System.out.println("=====" + type + " List=====");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("No " + type + " file found");
        }
    }

    public static String search(EntityType type, String typeID) {
        String path = pathSetter(type);
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("No " + type + " file found");
            return null;
        }
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(typeID + ",")) {
                    System.out.println("=====Found " + type + " Element=====");
                    System.out.println(line);
                    return line;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("No " + type + " file found");
            return null;
        }
        System.out.println("No " + type + " element found with ID: " + typeID);
        return null;
    }
}