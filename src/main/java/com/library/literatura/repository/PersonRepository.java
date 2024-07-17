package com.library.literatura.repository;

import com.library.literatura.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByNameContainsIgnoreCase(String name);
    
    @Query("SELECT p FROM Person p WHERE p.birth <= :year AND p.death >= :year")
    List<Person> filterPersonsByYear(int year);
}