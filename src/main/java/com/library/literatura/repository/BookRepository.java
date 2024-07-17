package com.library.literatura.repository;

import com.library.literatura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleContainsIgnoreCase(String title);

    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> filterBooksByLanguage(String language);
}
