package main.util;

import java.util.Scanner;

/**
 * Utility class for handling user input.
 * Provides methods for reading input with validation and cancel support.
 */
public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Reads user's menu choice from console.
     */
    public static String readChoice() {
        System.out.print("Your choice: ");
        return scanner.nextLine().trim();
    }

    /**
     * Reads user input with a prompt.
     */
    public static String readInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Reads user input with cancel support.
     * Returns null if the user types "cancel".
     */
    public static String readInputWithCancel(String prompt) {
        System.out.print(prompt + " (type 'cancel' to abort): ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("cancel") ? null : input;
    }

    /**
     * Asks for confirmation before a critical action.
     * Returns true if user confirms with 'y'.
     */
    public static boolean confirmAction(String message) {
        System.out.print(message + " (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    /**
     * Closes the scanner when application exits.
     */
    public static void close() {
        scanner.close();
    }
}