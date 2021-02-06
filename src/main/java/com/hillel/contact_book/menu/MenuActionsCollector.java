package com.hillel.contact_book.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.contact_book.menu.actions.*;
import com.hillel.contact_book.service.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MenuActionsCollector {

    public static List<MenuAction> collectMenuActions(BufferedReader reader) {
        ContactsService contactsService = getContactsServiceFromProperties(getPropertiesPath());
        List<MenuAction> actions = new ArrayList<>();
        actions.add(new ReadAllContactsMenuAction(contactsService));
        actions.add(new AddContactMenuAction(contactsService, reader));
        actions.add(new SearchByNameMenuAction(contactsService, reader));
        actions.add(new SearchByValueMenuAction(contactsService, reader));
        actions.add(new ExitMenuAction());
        if (contactsService instanceof WithAuthorizationContactsService) {
            actions.add(new CheckInMenuAction((WithAuthorizationContactsService) contactsService, reader));
            actions.add(new SingInMenuAction((WithAuthorizationContactsService) contactsService, reader));
            actions.add(new SingOutMenuAction((WithAuthorizationContactsService) contactsService));
        }
        return actions;
    }

    private static URL getPropertiesPath() {
        ClassLoader classLoader = MenuActionsCollector.class.getClassLoader();
        URL resourceDevUrl = classLoader.getResource("properties/app-dev.properties");
        URL resourceProdUrl = classLoader.getResource("properties/app-prod.properties");
        String systemProperty = System.getProperty("contactbook.profile");
        if ("dev".equals(systemProperty)) return resourceDevUrl;
        else if ("prod".equals(systemProperty)) return resourceProdUrl;
        else throw new IllegalStateException("Unexpected value: " + systemProperty);
    }

    private static ContactsService getContactsServiceFromProperties(URL propertiesUrl) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(new File(propertiesUrl.toURI()))) {
            properties.load(fileInputStream);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        ContactsService contactsService;
        switch (properties.getProperty("app.service.workmode")) {
            case "memory":
                contactsService = new InMemoryContactsService();
                break;
            case "file":
                contactsService = new InFileContactsService(new File(properties.getProperty("file.path")));
                break;
            case "api":
                contactsService = new ApiContactsService(
                        HttpClient.newBuilder().build(),
                        new ObjectMapper(),
                        properties.getProperty("api.base-uri"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + properties.getProperty("app.service.workmode"));
        }
        return contactsService;
    }
}
