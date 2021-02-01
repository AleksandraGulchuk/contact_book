package com.hillel.contact_book.dto;

import com.hillel.contact_book.users.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String status;
    private List<User> users;
    private String error;
}
