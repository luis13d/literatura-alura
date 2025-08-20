package com.library.literatura.service;

import com.library.literatura.models.*;
import com.library.literatura.repository.BookRepository;
import com.library.literatura.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    private final APIFetcher apiFetcher;
    private final DataConversor dataConversor;
    private static final String GUTENDX_BASE_URL = "https://gutendx.com/books";

    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
        this.apiFetcher = new APIFetcher();
        this.dataConversor = new DataConversor();
    }

    public Optional<Book> findBookByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return bookRepository.findByTitleContainsIgnoreCase(title.trim());
    }

    public Optional<Book> searchAndSaveBook(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        String normalizedTitle = title.trim();
        
        // Check if book already exists in database
        Optional<Book> existingBook = findBookByTitle(normalizedTitle);
        if (existingBook.isPresent()) {
            return existingBook;
        }

        // Search in external API
        Optional<BookData> bookData = searchBookInAPI(normalizedTitle);
        if (bookData.isEmpty()) {
            return Optional.empty();
        }

        // Create and save new book
        return Optional.of(createAndSaveBook(bookData.get()));
    }

    private Optional<BookData> searchBookInAPI(String title) {
        try {
            String searchUrl = GUTENDX_BASE_URL + "?search=" + title.toLowerCase().replace(" ", "+");
            String jsonResponse = apiFetcher.getData(searchUrl);
            Response response = dataConversor.getData(jsonResponse, Response.class);
            
            return response.books().stream()
                    .filter(book -> book.title().toLowerCase().contains(title.toLowerCase()))
                    .findFirst();
        } catch (Exception e) {
            throw new RuntimeException("Error searching book in API: " + e.getMessage(), e);
        }
    }

    private Book createAndSaveBook(BookData bookData) {
        List<Person> authors = bookData.authors().stream()
                .map(this::findOrCreatePerson)
                .collect(Collectors.toList());

        Book newBook = new Book(bookData, authors);
        return bookRepository.save(newBook);
    }

    private Person findOrCreatePerson(PersonData personData) {
        return personRepository.findByNameContainsIgnoreCase(personData.name())
                .orElseGet(() -> personRepository.save(new Person(Collections.singletonList(personData))));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        return bookRepository.filterBooksByLanguage(language.trim());
    }
}