package com.hillel.contactbook.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {
    private String login;
    private String password;
    private String dateBorn;
    private String token;
    private LocalTime authorizationTime;

    public User(String login, String token, LocalTime authorizationTime) {
        this.login = login;
        this.token = token;
        this.authorizationTime = authorizationTime;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String dateBorn) {
        this.login = login;
        this.password = password;
        this.dateBorn = dateBorn;
    }

    public User(String login) {
        this.login = login;
    }
}
