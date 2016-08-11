package com.github.dmytro.dobrovolskyi.validator.example.data;

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

    public String getName() {
        return name;
    }

    public Passport getPassport() {
        return passport;
    }

    public int getAge() {
        return age;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
