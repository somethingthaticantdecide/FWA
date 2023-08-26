package edu.school21.cinema.models;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String phoneNumber, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
