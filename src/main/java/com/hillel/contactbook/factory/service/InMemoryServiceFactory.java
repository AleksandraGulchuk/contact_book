package com.hillel.contactbook.factory.service;

import com.hillel.contactbook.service.contacts.ContactsService;
import com.hillel.contactbook.service.contacts.InMemoryContactsService;
import com.hillel.contactbook.service.users.FictiveUserService;
import com.hillel.contactbook.service.users.UserService;

public class InMemoryServiceFactory implements ServiceFactory {

    @Override
    public ContactsService createContactsService(String profileName) {
        ContactsService contactsService = new InMemoryContactsService();
        contactsService.setUserService(createUserService(profileName));
        return contactsService;
    }

    @Override
    public UserService createUserService(String profileName) {
        return new FictiveUserService();
    }
}
