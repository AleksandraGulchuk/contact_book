package com.hillel.contactbook.factory.service;

import com.hillel.contactbook.config.ConfigLoader;
import com.hillel.contactbook.service.contacts.ContactsService;
import com.hillel.contactbook.service.contacts.DatabaseContactsService;
import com.hillel.contactbook.service.users.DatabaseUserService;
import com.hillel.contactbook.service.users.UserService;

public class DatabaseServiceFactory implements ServiceFactory {

    @Override
    public ContactsService createContactsService(String profileName) {
        ConfigLoader configLoader = new ConfigLoader();
        DatabaseContactsService contactsService = new DatabaseContactsService();
        configLoader.setFileProps(contactsService, profileName);
        contactsService.setDataSource();
        DatabaseUserService userService = (DatabaseUserService) createUserService(profileName);
        contactsService.setUserService(userService);
        userService.setDataSource(contactsService.getDataSource());
        return contactsService;
    }

    @Override
    public UserService createUserService(String profileName) {
        return new DatabaseUserService();
    }
}
