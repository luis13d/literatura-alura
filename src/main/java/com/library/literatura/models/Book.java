package com.library.literatura.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    @ManyToOne
    private Person person;
    private String language;
    private Integer downloads;

    public Book() {}

    public Book(BookData book, List<Person> personList) {
        this.title = book.title();
        this.person = personList.get(0);
        this.language = book.languages().get(0);
        this.downloads = book.downloads();
    }

    @Override
    public String toString() {
        return String.format("""
                ----- LIBRO -----
                Título: %s
                Autor: %s
                Idioma: %s
                Número de descargas: %s
                ------------------
                """, title, person.getName(), language, downloads);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getLanguages() {
        return language;
    }

    public void setLanguages(String language) {
        this.language = language;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
}