package com.github.dmytro.dobrovolskyi.validator.example;

import com.github.dmytro.dobrovolskyi.validator.InvariantValidator;
import com.github.dmytro.dobrovolskyi.validator.example.data.Passport;
import com.github.dmytro.dobrovolskyi.validator.example.data.Person;

import java.time.LocalDate;

public class PersonValidator {
    private final Person person;

    public PersonValidator(Person person) {
        this.person = person;
    }

    public InvariantValidator get() {
        return InvariantValidator.newInstance()
                .validateNotEmpty(person.getName(), "Person name must not be empty", person.getName())
                .validateNotNull(person.getPassport(), "Person %s must have a passport", person.getName())
                .validate(() -> person.getAge() >= 18, "Person %s must be at least 18 years old", person.getName())
                .validate(() -> !person.getAddresses().isEmpty(), "Person %s must have at least one address", person.getName())
                .compose(passportValidator())
                .compose(addressesValidator());
    }

    private InvariantValidator addressesValidator() {
        return person.getAddresses().stream()
                .map(address -> InvariantValidator.newInstance()
                        .validateNotEmpty(address.getState(), "State mut not be empty")
                        .validateNotEmpty(address.getCity(), "City must not be empty")
                        .validateNotEmpty(address.getStreet(), "Street must not be empty")
                )
                .reduce(InvariantValidator.newInstance(), InvariantValidator::compose);
    }

    private InvariantValidator passportValidator() {
        Passport passport = person.getPassport();

        return InvariantValidator.newInstance()
                .validateNotEmpty(passport.getId(), "Passport must have id")
                .validateNotNull(passport.getIssueDate(), "Passport %s issue date must be set", passport.getId())
                .validateNotNull(passport.getExpiringDate(), "Passport %s expiring date must be set", passport.getId())
                .validate(() -> passport.getIssueDate().isBefore(LocalDate.now()),
                        "Passport %s issue date must be before current date", passport.getId()
                )
                .validate(() -> passport.getExpiringDate().isAfter(LocalDate.now()),
                        "Passport %s issue date must be after current date", passport.getId()
                );
    }
}
