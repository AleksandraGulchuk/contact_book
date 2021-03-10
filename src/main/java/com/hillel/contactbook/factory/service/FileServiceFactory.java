package com.hillel.contactbook.factory.service;

import com.hillel.contactbook.config.ConfigLoader;
import com.hillel.contactbook.service.contacts.ContactsService;
import com.hillel.contactbook.service.contacts.InFileContactsService;
import com.hillel.contactbook.service.users.FictiveUserService;
import com.hillel.contactbook.service.users.UserService;

public class FileServiceFactory implements ServiceFactory {

    @Override
    public ContactsService createContactsService(String profileName) {
        ContactsService contactsService = new ConfigLoader().getFileProps(InFileContactsService.class, profileName);
        contactsService.setUserService(createUserService(profileName));
        return contactsService;
    }

    @Override
    public UserService createUserService(String profileName) {
        return new FictiveUserService();
    }
}
