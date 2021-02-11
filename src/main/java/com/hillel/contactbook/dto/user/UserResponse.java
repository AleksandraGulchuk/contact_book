package com.hillel.contactbook.dto.user;

import com.hillel.contactbook.users.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String status;
    private List<User> users;
    private String error;
}
