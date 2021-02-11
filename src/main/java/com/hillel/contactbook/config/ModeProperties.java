package com.hillel.contactbook.config;

import com.hillel.contactbook.annotations.PropAnnotation;
import com.hillel.contactbook.service.ApiContactsService;
import com.hillel.contactbook.service.ContactsService;
import com.hillel.contactbook.service.InFileContactsService;
import com.hillel.contactbook.service.InMemoryContactsService;
import lombok.Data;

@Data
public class ModeProperties {

    @PropAnnotation("app.service.workmode")
    private String serviceWorkmode;

    public ContactsService createContactService(ConfigLoader configLoader, String filePropName) {
        ContactsService contactsService;
        switch (serviceWorkmode) {
            case "memory":
                contactsService = new InMemoryContactsService();
                break;
            case "file":
                contactsService = configLoader.getFileProps(InFileContactsService.class, filePropName);
                break;
            case "api":
                contactsService = configLoader.getFileProps(ApiContactsService.class, filePropName);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + serviceWorkmode);
        }
        return contactsService;

    }

}
