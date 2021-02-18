package com.hillel.contactbook.factory.menu;

import com.hillel.contactbook.config.ServiceModeProperty;
import com.hillel.contactbook.menu.Menu;
import com.hillel.contactbook.service.contacts.ApiContactsService;
import com.hillel.contactbook.service.contacts.ContactsService;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MenuFactory {

    public Menu buildMenu() {
        ContactsService contactsService = ServiceModeProperty.getContactsServiceFromProperties();
        if (contactsService instanceof ApiContactsService) {
            return createAuthServiceMenu(new ContactsMenuBuilder(
                    contactsService, new BufferedReader(new InputStreamReader(System.in))));
        } else
            return createWithoutAuthServiceMenu(new ContactsMenuBuilder(
                    contactsService, new BufferedReader(new InputStreamReader(System.in))));
    }

    private Menu createAuthServiceMenu(MenuBuilder menuBuilder) {
        return menuBuilder
                .checkIn()
                .singIn()
                .readAll()
                .add()
                .remove()
                .searchByName()
                .searchByValue()
                .singOut()
                .exit()
                .build();
    }

    private Menu createWithoutAuthServiceMenu(MenuBuilder menuBuilder) {
        return menuBuilder
                .readAll()
                .add()
                .remove()
                .searchByName()
                .searchByValue()
                .exit()
                .build();
    }

}


