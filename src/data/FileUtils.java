package data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Utility class for file operations.
 * Factorizes common file reading patterns used in View classes.
 */
public class FileUtils {

    /**
     * Reads a file and applies an action to each non-empty line.
     * Returns true if at least one line was processed.
     */
    public static boolean readLines(EntityType type, Consumer<String> lineAction) {
        File file = new File(type.getFilePath());
        if (!file.exists()) {
            return false;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    lineAction.accept(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error reading " + type + " file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads a file and returns the first line that starts with the given ID.
     */
    public static String findLineById(EntityType type, String id) {
        File file = new File(type.getFilePath());
        if (!file.exists()) {
            return null;
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(id + ",")) {
                    return line;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * Prints a table header with the given columns.
     */
    public static void printTableHeader(String... columns) {
        System.out.println("\n========================================================================================================");
        StringBuilder header = new StringBuilder();
        StringBuilder separator = new StringBuilder();
        
        for (String col : columns) {
            header.append(String.format("%-20s | ", col));
            separator.append("---------------------|");
        }
        
        System.out.println(header.toString());
        System.out.println(separator.toString());
    }
}