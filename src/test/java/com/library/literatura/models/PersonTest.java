package com.library.literatura.models;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void isAliveInYear_WithBirthAndDeathYears_ShouldReturnCorrectResult() {
        // Arrange
        PersonData personData = new PersonData("Test Author", 1950, 2000);
        Person person = new Person(Collections.singletonList(personData));

        // Act & Assert
        assertTrue(person.isAliveInYear(1975)); // Between birth and death
        assertTrue(person.isAliveInYear(1950)); // Birth year
        assertTrue(person.isAliveInYear(2000)); // Death year
        assertFalse(person.isAliveInYear(1940)); // Before birth
        assertFalse(person.isAliveInYear(2010)); // After death
    }

    @Test
    void isAliveInYear_WithOnlyBirthYear_ShouldReturnCorrectResult() {
        // Arrange
        PersonData personData = new PersonData("Living Author", 1970, null);
        Person person = new Person(Collections.singletonList(personData));

        // Act & Assert
        assertTrue(person.isAliveInYear(1980)); // After birth, no death
        assertTrue(person.isAliveInYear(2024)); // Current year
        assertFalse(person.isAliveInYear(1960)); // Before birth
    }

    @Test
    void constructor_WithNullAuthors_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Person(null);
        });
    }

    @Test
    void constructor_WithEmptyAuthors_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Person(Collections.emptyList());
        });
    }

    @Test
    void constructor_WithInvalidYears_ShouldThrowException() {
        // Arrange - death before birth
        PersonData invalidPersonData = new PersonData("Invalid Author", 2000, 1950);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Person(Collections.singletonList(invalidPersonData));
        });
    }
}