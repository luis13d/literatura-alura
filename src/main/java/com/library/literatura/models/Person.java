package com.library.literatura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birth;
    private Integer death;
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books;

    public Person(){}

    public Person(List<PersonData> authors) {
        this.name = authors.get(0).name();
        this.birth = authors.get(0).birth();
        this.death = authors.get(0).death();
    }

    @Override
    public String toString() {
        List<String> bookTitles = books.stream()
                .map(Book::getTitle)
                .toList();
        return String.format("""
                ----- Autor -----
                Autor: %s
                Fecha de nacimiento: %s
                Fecha de fallecimiento: %s
                Libros: %s
                ------------------
                """, name, birth, death, bookTitles);
    }

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
        this.name = name;
    }

    public Integer getBirth() {
        return birth;
    }

    public void setBirth(Integer birth) {
        this.birth = birth;
    }

    public Integer getDeath() {
        return death;
    }

    public void setDeath(Integer death) {
        this.death = death;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
