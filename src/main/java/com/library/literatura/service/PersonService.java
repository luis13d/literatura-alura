package com.library.literatura.service;

import com.library.literatura.models.Person;
import com.library.literatura.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public List<Person> getPersonsAliveInYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("Year must be a positive number");
        }
        return personRepository.filterPersonsByYear(year);
    }
}