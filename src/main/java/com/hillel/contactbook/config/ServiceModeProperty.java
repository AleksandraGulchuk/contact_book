package com.hillel.contactbook.config;

import com.hillel.contactbook.annotations.PropAnnotation;
import com.hillel.contactbook.factory.service.ApiServiceFactory;
import com.hillel.contactbook.factory.service.FileServiceFactory;
import com.hillel.contactbook.factory.service.InMemoryServiceFactory;
import com.hillel.contactbook.factory.service.ServiceFactory;
import com.hillel.contactbook.service.contacts.ContactsService;
import lombok.Data;

@Data
public class ServiceModeProperty {

    @PropAnnotation("app.service.workmode")
    private String serviceWorkmode;

    public static ContactsService getContactsServiceFromProperties() {
        ConfigLoader configLoader = new ConfigLoader();
        String profileName = configLoader
                .getSystemProps(AppSystemProperty.class)
                .getProfileName();
        return configLoader
                .getFileProps(ServiceModeProperty.class, profileName)
                .selectContactsService(profileName);
    }

    private ContactsService selectContactsService(String profileName) {
        ContactsService contactsService;
        ServiceFactory serviceFactory;
        switch (serviceWorkmode) {
            case "memory":
                serviceFactory = new InMemoryServiceFactory();
                break;
            case "file":
                serviceFactory = new FileServiceFactory();
                break;
            case "api":
                serviceFactory = new ApiServiceFactory();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + serviceWorkmode);
        }
        contactsService = serviceFactory.createContactsService(profileName);
        contactsService.setUserService(serviceFactory.createUserService(profileName));
        return contactsService;
    }

}
