package com.ddobrovolskyi.validator.example;

public class Address {
    private final String state;
    private final String city;
    private final String street;

    public Address(String state, String city, String street) {
        this.state = state;
        this.city = city;
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }
}
