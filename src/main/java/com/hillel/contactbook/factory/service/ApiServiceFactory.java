package com.hillel.contactbook.factory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.contactbook.config.ConfigLoader;
import com.hillel.contactbook.contacts.ContactWorker;
import com.hillel.contactbook.factory.httprequest.JsonHttpRequestFactory;
import com.hillel.contactbook.service.contacts.ApiContactsService;
import com.hillel.contactbook.service.contacts.ContactsService;
import com.hillel.contactbook.service.users.ApiUserService;
import com.hillel.contactbook.service.users.UserService;

import java.net.http.HttpClient;

public class ApiServiceFactory implements ServiceFactory {


    @Override
    public ContactsService createContactsService(String profileName) {
        ConfigLoader configLoader = new ConfigLoader();
        ContactsService contactsService = new ApiContactsService(
                HttpClient.newBuilder().build(),
                new ObjectMapper(),
                new ContactWorker(),
                new JsonHttpRequestFactory());
        configLoader.setFileProps(contactsService, profileName);
        return contactsService;
    }

    @Override
    public UserService createUserService(String profileName) {
        ConfigLoader configLoader = new ConfigLoader();
        UserService userService = new ApiUserService(
                HttpClient.newBuilder().build(),
                new ObjectMapper(),
                new JsonHttpRequestFactory());
        configLoader.setFileProps(userService, profileName);
        return userService;
    }
}
