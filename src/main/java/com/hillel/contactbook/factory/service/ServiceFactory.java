package com.hillel.contactbook.factory.service;

import com.hillel.contactbook.service.contacts.ContactsService;
import com.hillel.contactbook.service.users.UserService;

public interface ServiceFactory {

    ContactsService createContactsService(String profileName);

    UserService createUserService(String profileName);

}
