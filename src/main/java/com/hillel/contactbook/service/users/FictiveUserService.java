package com.hillel.contactbook.service.users;

import com.hillel.contactbook.dto.user.CheckInResponse;
import com.hillel.contactbook.dto.user.SingInResponse;
import com.hillel.contactbook.users.User;

import java.util.Collections;
import java.util.List;

public class FictiveUserService implements UserService {
    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean hasToken() {
        return true;
    }

    @Override
    public CheckInResponse checkIn(User user) {
        throw new UnsupportedOperationException("CheckIn not supported");
    }

    @Override
    public SingInResponse singIn(User user) {
        throw new UnsupportedOperationException("SingIn not supported");
    }

    @Override
    public void singOut() {
        throw new UnsupportedOperationException("SingOut not supported");
    }

    @Override
    public List<User> getAllUsers() {
        return Collections.emptyList();
    }
}
