package com.hillel.contact_book.menu;

import com.hillel.contact_book.menu.actions.*;
import com.hillel.contact_book.service.ContactsService;
import com.hillel.contact_book.service.WithAuthorizationContactsService;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class MenuCollector {

    public static List<MenuAction> collectMenu(ContactsService contactsService, BufferedReader reader) {
        List<MenuAction> actions = new ArrayList<>();
        if (contactsService instanceof WithAuthorizationContactsService) {
            actions.add(new CheckInMenuAction((WithAuthorizationContactsService) contactsService, reader));
            actions.add(new SingInMenuAction((WithAuthorizationContactsService) contactsService, reader));
            actions.add(new SingOutMenuAction((WithAuthorizationContactsService) contactsService));
        }
        actions.add(new ReadAllContactsMenuAction(contactsService));
        actions.add(new AddContactMenuAction(contactsService, reader));
        actions.add(new SearchByNameMenuAction(contactsService, reader));
        actions.add(new SearchByValueMenuAction(contactsService, reader));
        actions.add(new ExitMenuAction());
        return actions;
    }
}
