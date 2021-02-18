package com.hillel.contactbook.factory.service;

import com.hillel.contactbook.service.contacts.ContactsService;
import com.hillel.contactbook.service.contacts.InMemoryContactsService;
import com.hillel.contactbook.service.users.FictiveUserService;
import com.hillel.contactbook.service.users.UserService;

public class InMemoryServiceFactory implements ServiceFactory {

    @Override
    public ContactsService createContactsService(String profileName) {
        return new InMemoryContactsService();
    }

    @Override
    public UserService createUserService(String profileName) {
        return new FictiveUserService();
    }
}
