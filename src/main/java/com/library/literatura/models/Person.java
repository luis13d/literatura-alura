package com.library.literatura.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "persons", indexes = {
    @Index(name = "idx_person_name", columnList = "name"),
    @Index(name = "idx_person_birth_death", columnList = "birth, death")
})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "birth_year")
    private Integer birth;

    @Column(name = "death_year")
    private Integer death;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    public Person() {}

    public Person(List<PersonData> authors) {
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors list cannot be null or empty");
        }
        
        PersonData author = authors.get(0);
        this.name = validateAndSetName(author.name());
        this.birth = author.birth();
        this.death = author.death();
        
        validateYears();
    }

    private String validateAndSetName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
        return name.trim();
    }

    private void validateYears() {
        if (birth != null && birth < 0) {
            throw new IllegalArgumentException("Birth year cannot be negative");
        }
        if (death != null && death < 0) {
            throw new IllegalArgumentException("Death year cannot be negative");
        }
        if (birth != null && death != null && death < birth) {
            throw new IllegalArgumentException("Death year cannot be before birth year");
        }
    }

    public boolean isAliveInYear(int year) {
        if (birth == null) return false;
        if (year < birth) return false;
        return death == null || year <= death;
    }

    @Override
    public String toString() {
        List<String> bookTitles = books.stream()
                .map(Book::getTitle)
                .toList();
        
        String birthYear = (birth != null) ? birth.toString() : "Desconocido";
        String deathYear = (death != null) ? death.toString() : "Vivo";
        
        return String.format("""
                ----- AUTOR -----
                Autor: %s
                Fecha de nacimiento: %s
                Fecha de fallecimiento: %s
                Libros: %s
                Total de libros: %d
                ------------------
                """, name, birthYear, deathYear, bookTitles, bookTitles.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(id, person.id) &&
               Objects.equals(name, person.name) &&
               Objects.equals(birth, person.birth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birth);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateAndSetName(name);
    }

    public Integer getBirth() {
        return birth;
    }

    public void setBirth(Integer birth) {
        this.birth = birth;
        validateYears();
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
        validateYears();
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void setBooks(List<Book> books) {
        this.books = (books != null) ? new ArrayList<>(books) : new ArrayList<>();
    }

    public void addBook(Book book) {
        if (book != null && !books.contains(book)) {
            books.add(book);
            book.setPerson(this);
        }
    }

    public void removeBook(Book book) {
        if (book != null) {
            books.remove(book);
        }
    }
}
