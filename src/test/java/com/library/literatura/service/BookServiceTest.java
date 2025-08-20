package com.library.literatura.service;

import com.library.literatura.models.Book;
import com.library.literatura.repository.BookRepository;
import com.library.literatura.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PersonRepository personRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, personRepository);
    }

    @Test
    void findBookByTitle_WithValidTitle_ShouldReturnBook() {
        // Arrange
        String title = "Test Book";
        Book mockBook = new Book();
        when(bookRepository.findByTitleContainsIgnoreCase(title)).thenReturn(Optional.of(mockBook));

        // Act
        Optional<Book> result = bookService.findBookByTitle(title);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockBook, result.get());
    }

    @Test
    void findBookByTitle_WithNullTitle_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.findBookByTitle(null);
        });
    }

    @Test
    void findBookByTitle_WithEmptyTitle_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.findBookByTitle("");
        });
    }

    @Test
    void getBooksByLanguage_WithNullLanguage_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBooksByLanguage(null);
        });
    }
}