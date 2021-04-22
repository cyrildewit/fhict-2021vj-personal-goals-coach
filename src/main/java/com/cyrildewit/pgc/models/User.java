package com.cyrildewit.pgc.models;

import java.time.LocalDateTime;

public class User {
    protected int id;
    protected String uuid;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String email;
    protected LocalDateTime emailVerifiedAt;
    protected String password;

    public User() {}

    public User(String uuid, String firstName, String lastName, String phoneNumber, String email, LocalDateTime emailVerifiedAt, String password) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.password = password;
    }

    public User(int id, String uuid, String firstName, String lasttName, String phoneNumber, String email, LocalDateTime emailVerifiedAt, String password) {
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lasttName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}