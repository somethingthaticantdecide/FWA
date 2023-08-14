package edu.school21.cinema.models;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}
