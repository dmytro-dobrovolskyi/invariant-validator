package com.ddobrovolskyi.validator.example;

import java.util.Optional;

public class Example {
    public static void main(String[] args) {
        Person person = new Person("Sherlock Holmes", new Passport(), 39);
        person.addAddress(new Address("UK", "London", "221B Baker Street"));

        // Throws an exception with corresponding error message of the first invalid condition
        person.validator().throwIfNotValid(IllegalStateException::new);

        // Checks if all conditions are valid
        boolean isPersonValid = person.validator().isValid();

        // Takes Consumer which in turn takes the error message and will be executed only if there was any validation failures
        person.validator().ifNotValid(System.out::println);

        // Returns Optional of error message which will have value only if there was any validation failures
        Optional<String> errorMessage = person.validator().getErrorMessage();
    }
}
