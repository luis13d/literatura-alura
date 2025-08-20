package com.library.literatura.menu;

import com.library.literatura.models.*;
import com.library.literatura.service.BookService;
import com.library.literatura.service.PersonService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Menu {

    private final Scanner scanner;
    private final BookService bookService;
    private final PersonService personService;
    private final MenuValidator validator;

    public Menu(BookService bookService, PersonService personService) {
        this.scanner = new Scanner(System.in);
        this.bookService = bookService;
        this.personService = personService;
        this.validator = new MenuValidator();
    }

    private void displayMenu() {
        System.out.println("""
                    **************************************
                    Elija la opción a través de su número:
                    1 - Buscar por título.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados.
                    4 - Listar autores vivos en un determinado año.
                    5 - Listar libros por idioma.
                    0 - Salir
                    **************************************
                    """);
    }

    public void show() {
        boolean exit = false;
        System.out.println("¡Bienvenido a Literatura!");
        
        while (!exit) {
            displayMenu();
            try {
                int option = validator.getValidIntegerInput(scanner, "Seleccione una opción: ", 0, 5);
                exit = executeOption(option);
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                System.out.println("Intente nuevamente.");
            }
        }
        
        System.out.println("¡Gracias por usar Literatura!");
        scanner.close();
    }

    private boolean executeOption(int option) {
        switch (option) {
            case 1 -> searchBookByTitle();
            case 2 -> listRegisteredBooks();
            case 3 -> listAuthors();
            case 4 -> listAuthorsByYear();
            case 5 -> listBooksByLanguage();
            case 0 -> {
                System.out.println("Cerrando la aplicación...");
                return true;
            }
            default -> System.out.println("Opción inválida");
        }
        return false;
    }

    private void searchBookByTitle() {
        try {
            String bookTitle = validator.getValidStringInput(scanner, "Escribe el nombre del libro que deseas buscar: ");
            
            // Check if book already exists
            Optional<Book> existingBook = bookService.findBookByTitle(bookTitle);
            if (existingBook.isPresent()) {
                System.out.println("El libro '" + existingBook.get().getTitle() + "' ya existe en la base de datos.");
                System.out.println("No se puede registrar el mismo libro más de una vez.");
                System.out.println(existingBook.get());
                return;
            }

            System.out.println("Libro no encontrado en base de datos, buscando en internet...");
            
            Optional<Book> newBook = bookService.searchAndSaveBook(bookTitle);
            if (newBook.isPresent()) {
                System.out.println("¡Libro encontrado y registrado exitosamente!");
                System.out.println(newBook.get());
            } else {
                System.out.println("Lo sentimos, no hemos encontrado el libro '" + bookTitle + "'.");
                System.out.println("Intente con un título diferente o más específico.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private void listRegisteredBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No hay libros registrados en la base de datos.");
                System.out.println("Pruebe buscar algunos libros primero.");
            } else {
                System.out.println("\n=== LIBROS REGISTRADOS ===");
                books.forEach(System.out::println);
                System.out.println("Total de libros: " + books.size());
            }
        } catch (Exception e) {
            System.out.println("Error al obtener la lista de libros: " + e.getMessage());
        }
    }

    private void listAuthors() {
        try {
            List<Person> authors = personService.getAllPersons();
            if (authors.isEmpty()) {
                System.out.println("No hay autores registrados en la base de datos.");
                System.out.println("Los autores se registran automáticamente al buscar libros.");
            } else {
                System.out.println("\n=== AUTORES REGISTRADOS ===");
                authors.forEach(System.out::println);
                System.out.println("Total de autores: " + authors.size());
            }
        } catch (Exception e) {
            System.out.println("Error al obtener la lista de autores: " + e.getMessage());
        }
    }

    private void listAuthorsByYear() {
        try {
            int year = validator.getValidIntegerInput(scanner, 
                "Ingrese el año para buscar autores vivos: ", 1, 2024);
            
            List<Person> authorsAlive = personService.getPersonsAliveInYear(year);
            if (authorsAlive.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + year + ".");
            } else {
                System.out.println("\n=== AUTORES VIVOS EN " + year + " ===");
                authorsAlive.forEach(System.out::println);
                System.out.println("Total de autores encontrados: " + authorsAlive.size());
            }
        } catch (Exception e) {
            System.out.println("Error al buscar autores por año: " + e.getMessage());
        }
    }

    private void listBooksByLanguage() {
        try {
            List<String> validLanguages = List.of("es", "en", "fr", "pt");
            
            System.out.println("""
                    Idiomas disponibles:
                    es - Español
                    en - Inglés
                    fr - Francés
                    pt - Português
                    """);
            
            String language = validator.getValidLanguageInput(scanner, validLanguages);
            
            List<Book> books = bookService.getBooksByLanguage(language);
            if (books.isEmpty()) {
                String languageName = getLanguageName(language);
                System.out.println("No se encontraron libros en " + languageName + ".");
            } else {
                String languageName = getLanguageName(language);
                System.out.println("\n=== LIBROS EN " + languageName.toUpperCase() + " ===");
                books.forEach(System.out::println);
                System.out.println("Total de libros encontrados: " + books.size());
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros por idioma: " + e.getMessage());
        }
    }
    
    private String getLanguageName(String code) {
        return switch (code) {
            case "es" -> "Español";
            case "en" -> "Inglés";
            case "fr" -> "Francés";
            case "pt" -> "Português";
            default -> code;
        };
    }
}