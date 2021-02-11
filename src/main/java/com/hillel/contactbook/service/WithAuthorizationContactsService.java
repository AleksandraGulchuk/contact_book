package com.hillel.contactbook.service;

import com.hillel.contactbook.dto.user.CheckInResponse;
import com.hillel.contactbook.dto.user.SingInResponse;

public interface WithAuthorizationContactsService {

    CheckInResponse checkIn(String login, String password, String dateBorn);

    SingInResponse singIn(String login, String password);

    void singOut();

    boolean hasToken();

}
