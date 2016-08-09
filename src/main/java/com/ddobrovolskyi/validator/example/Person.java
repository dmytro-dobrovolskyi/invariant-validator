package com.ddobrovolskyi.validator.example;

import com.ddobrovolskyi.validator.InvariantValidator;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private final String name;
    private final Passport passport;
    private final int age;
    private List<Address> addresses = new ArrayList<>();

    public Person(String name, Passport passport, int age) {
        this.name = name;
        this.passport = passport;
        this.age = age;
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public InvariantValidator validator() {
        return InvariantValidator.newInstance()
                .validateNotEmpty(name, "Person name must not be empty")
                .validateNotNull(passport, "Person %s must have a passport", name)
                .validate(() -> age >= 18, "Person %s must be at least 18 years old", name)
                .validate(() -> !addresses.isEmpty(), "Person %s must have at least one address", name)
                .compose(addresses.stream()
                        .map(address -> InvariantValidator.newInstance()
                                .validateNotEmpty(address.getState(), "State mut not be empty")
                                .validateNotEmpty(address.getCity(), "City must not be empty")
                                .validateNotEmpty(address.getStreet(), "Street must not be empty")
                        )
                        .reduce(InvariantValidator.newInstance(), InvariantValidator::compose)
                );
    }
}
