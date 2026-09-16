package data;

import java.io.File;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class DataManager {
    //Methods
    public static String generateNextID(EntityType type) {
        String path = getFilePath(type);
        String prefix = getPrefix(type);
        File file = new File(path);

        int maxID = 0;

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        String existingID = parts[0];
                        String numberPart = existingID.substring(1);
                        int currentID = Integer.parseInt(numberPart);

                        if (currentID > maxID) {
                            maxID = currentID;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file for ID generation");
            }
        }

        int nextID = maxID + 1;
        return prefix + String.format("%03d", nextID);
    }

    public static void add() {
        
    }

    public static void delete() {

    }

    public static void edit() {

    }


    public static void view(EntityType type) {
        String path = type.getFilePath();
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
        String path = type.getFilePath();
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