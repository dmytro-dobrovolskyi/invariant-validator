package com.github.dmytro.dobrovolskyi.validator.example.data;

import java.time.LocalDate;

public class Passport {
    private final String id;
    private final LocalDate issueDate;
    private final LocalDate expires;

    public Passport(String id, LocalDate issueDate, LocalDate expires) {
        this.id = id;
        this.issueDate = issueDate;
        this.expires = expires;
    }

    public String getId() {
        return id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getExpiringDate() {
        return expires;
    }
}
