package com.library.literatura.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books", indexes = {
    @Index(name = "idx_book_title", columnList = "title"),
    @Index(name = "idx_book_language", columnList = "language")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(nullable = false, length = 10)
    private String language;

    @Column(nullable = false)
    private Integer downloads;

    public Book() {}

    public Book(BookData bookData, List<Person> authors) {
        if (bookData == null) {
            throw new IllegalArgumentException("BookData cannot be null");
        }
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors list cannot be null or empty");
        }
        if (bookData.languages() == null || bookData.languages().isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one language");
        }

        this.title = validateAndSetTitle(bookData.title());
        this.person = authors.get(0);
        this.language = bookData.languages().get(0);
        this.downloads = bookData.downloads() != null ? bookData.downloads() : 0;
    }

    private String validateAndSetTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        return title.trim();
    }

    @Override
    public String toString() {
        String authorName = (person != null) ? person.getName() : "Autor desconocido";
        return String.format("""
                ----- LIBRO -----
                Título: %s
                Autor: %s
                Idioma: %s
                Número de descargas: %s
                ------------------
                """, title, authorName, language, downloads);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(id, book.id) &&
               Objects.equals(title, book.title) &&
               Objects.equals(person, book.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, person);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = validateAndSetTitle(title);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        this.person = person;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        this.language = language.trim();
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        if (downloads == null || downloads < 0) {
            throw new IllegalArgumentException("Downloads must be zero or positive");
        }
        this.downloads = downloads;
    }
}