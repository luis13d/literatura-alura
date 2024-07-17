package com.library.literatura.menu;

import com.library.literatura.models.*;
import com.library.literatura.repository.BookRepository;
import com.library.literatura.repository.PersonRepository;
import com.library.literatura.service.APIFetcher;
import com.library.literatura.service.DataConversor;

import java.util.*;
import java.util.stream.Collectors;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private APIFetcher apiFetcher = new APIFetcher();
    private final String URL_BASE = "https://gutendex.com/books";
    private DataConversor conversor = new DataConversor();
    // private List<BookData> booksData = new ArrayList<>();
    private BookRepository bookRepository;
    private PersonRepository personRepository;

    public Menu(BookRepository bookRepository, PersonRepository personRepository ) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    private void menu() {
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
        while (!exit) {
            menu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                optionMenu(option);
                if (option == 0 ) exit = true;
            } catch (InputMismatchException e) {
                System.out.println("\nError: entrada no válida. Intenta de nuevo.");
                scanner.next();
            }
        }
    }

    private void optionMenu(int option) {
        switch (option) {
            case 1:
                getBookByTitle();
                break;
            case 2:
                getRegistedBooks();
                break;
            case 3:
                getPersons();
                break;
            case 4:
                getPersonsByYear();
                break;
            case 5:
                getBooksByLanguage();
                break;
            case 0:
                System.out.println("Cerrando la aplicación...");
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private Optional<BookData> getBookData(String bookTitle) {
        var json = apiFetcher.getData(URL_BASE + "?search=" + bookTitle.toLowerCase().replace(" ", "+"));
        Response data = conversor.getData(json, Response.class);
        List<BookData> books = conversor.getData(json, Response.class).books();

        return books.stream()
                .filter(b -> b.title().toLowerCase().contains(bookTitle.toLowerCase()))
                .findFirst();
    }

    private void getBookByTitle() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        String bookTitle = scanner.nextLine();
        Optional<Book> bookFromDB = bookRepository.findByTitleContainsIgnoreCase(bookTitle);

        if (bookFromDB.isPresent()) {
            System.out.println("El libro " + bookFromDB.get().getTitle() + " ya existe.");
            System.out.println("No se puede registrar el mismo libro más de una vez");
            System.out.println(bookFromDB.get());
            return;
        }

        System.out.println("Libro no está en base de datos, buscando en internet...");
        Optional<BookData> bookFromApi = getBookData(bookTitle);
        if (bookFromApi.isPresent())  {
            System.out.println("Libro encontrado, registrando...");
            List<Person> persons = bookFromApi.get().authors().stream()
                    .map(a -> personRepository.findByNameContainsIgnoreCase(a.name())
                            .orElseGet(() -> personRepository.save(new Person(Collections.singletonList(a)))))
                    .collect(Collectors.toList());

            Book newBook = new Book(bookFromApi.get(),persons);
            bookRepository.save(newBook);
            System.out.println(newBook);
            return;
        }

        System.out.println("No hemos encontrado el libro");
    }

    private void getRegistedBooks() {
        List<Book> books = bookRepository.findAll();
        books.forEach(System.out::println);
    }

    private void getPersons() {
        List<Person> persons = personRepository.findAll();
        persons.forEach(System.out::println);
    }

    private void getPersonsByYear() {
        System.out.println("Ingrese el año para buscar autores vivos en determinado año: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        List<Person> person = personRepository.filterPersonsByYear(year);
        if (!person.isEmpty()) {
            person.forEach(System.out::println);
            return;
        }
        System.out.println("No se han encontrado autores en ese periodo");
    }

    private void getBooksByLanguage() {
        List<String> languages = List.of("es", "en", "fr", "pt");
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugues
                """);
        String language = scanner.nextLine();

        while (!languages.contains(language)) {
            System.out.println("Opción invalida, ingresa un idioma de la lista: ");
            language = scanner.nextLine();
        }
        List<Book> book = bookRepository.filterBooksByLanguage(language);

        if (!book.isEmpty()) {
            book.forEach(System.out::println);
            return;
        }

        System.out.println("No se encontrar libros con ese idioma");
    }

}
