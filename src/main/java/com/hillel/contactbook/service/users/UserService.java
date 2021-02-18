package com.hillel.contactbook.service.users;

import com.hillel.contactbook.dto.user.CheckInResponse;
import com.hillel.contactbook.dto.user.SingInResponse;
import com.hillel.contactbook.users.User;

import java.util.List;

public interface UserService {

    String getToken();

    boolean hasToken();

    CheckInResponse checkIn(User user);

    SingInResponse singIn(User user);

    void singOut();

    List<User> getAllUsers();

}
