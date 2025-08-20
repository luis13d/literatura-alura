package com.library.literatura.repository;

import com.library.literatura.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByNameContainsIgnoreCase(String name);
    
    @Query("SELECT p FROM Person p WHERE p.birth <= :year AND (p.death IS NULL OR p.death >= :year)")
    List<Person> filterPersonsByYear(@Param("year") int year);

    @Query("SELECT p FROM Person p WHERE p.birth IS NOT NULL ORDER BY p.birth")
    List<Person> findAllOrderByBirthYear();

    @Query("SELECT p FROM Person p WHERE p.death IS NULL")
    List<Person> findLivingAuthors();
}