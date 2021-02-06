package com.hillel.contact_book.service;

import com.hillel.contact_book.dto.user.CheckInResponse;
import com.hillel.contact_book.dto.user.SingInResponse;

public interface WithAuthorizationContactsService {

    CheckInResponse checkIn(String login, String password, String dateBorn);

    SingInResponse singIn(String login, String password);

    void singOut();

    boolean hasToken();

}
