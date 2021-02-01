package com.hillel.contact_book.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class AuthorizedUser {
    String login;
    String token;
    LocalTime authorizationTime;
}
