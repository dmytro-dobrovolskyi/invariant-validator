package com.github.dmytro.dobrovolskyi.validator.example;

import com.github.dmytro.dobrovolskyi.validator.InvariantValidator;
import com.github.dmytro.dobrovolskyi.validator.example.data.Address;
import com.github.dmytro.dobrovolskyi.validator.example.data.Passport;
import com.github.dmytro.dobrovolskyi.validator.example.data.Person;

import java.time.LocalDate;
import java.util.UUID;

public class Example {
    private final InvariantValidator personValidator;

    public Example(Person person) {
        this.personValidator = new PersonValidator(person).get();
    }

    public void printIsValid() {
        System.out.println(personValidator.isValid());
    }

    public void throwIllegalArgumentExceptionIfNotValid() {
        personValidator.throwIfNotValid(IllegalStateException::new);
    }

    public void printErrorMessageIfNotValid() {
        personValidator.ifNotValid(System.out::println);
    }

    public void printErrorMessageIfPresent() {
        personValidator.getErrorMessage().ifPresent(System.out::println);
    }

    public static void main(String[] args) {
        Person person = new Person(
                "Sherlock Holmes",
                new Passport(UUID.randomUUID().toString(), LocalDate.of(2014, 4, 19), LocalDate.of(2024, 4, 19)),
                39
        );
        person.addAddress(new Address("UK", "London", "221B Baker Street"));

        Example example = new Example(person);

        example.printIsValid();
        example.throwIllegalArgumentExceptionIfNotValid();
        example.printErrorMessageIfNotValid();
        example.printErrorMessageIfPresent();
    }
}
