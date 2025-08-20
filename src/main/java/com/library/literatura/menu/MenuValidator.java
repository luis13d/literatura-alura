package com.library.literatura.menu;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuValidator {

    public int getValidIntegerInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (input < min || input > max) {
                    System.out.println("Error: Ingrese un número entre " + min + " y " + max + ".");
                    continue;
                }
                
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada no válida. Ingrese un número.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public String getValidStringInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Error: La entrada no puede estar vacía.");
                continue;
            }
            
            if (input.length() < 2) {
                System.out.println("Error: Ingrese al menos 2 caracteres.");
                continue;
            }
            
            return input;
        }
    }

    public String getValidLanguageInput(Scanner scanner, List<String> validLanguages) {
        while (true) {
            System.out.print("Ingrese el código del idioma: ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.isEmpty()) {
                System.out.println("Error: La entrada no puede estar vacía.");
                continue;
            }
            
            if (!validLanguages.contains(input)) {
                System.out.println("Error: Idioma no válido. Opciones disponibles: " + validLanguages);
                continue;
            }
            
            return input;
        }
    }
}