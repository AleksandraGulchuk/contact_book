package com.hillel.contactbook.config;

import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.menu.actions.*;
import com.hillel.contactbook.service.*;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class MenuActionsCollector {

    public static List<MenuAction> collectMenuActions(BufferedReader reader) {
        ContactsService contactsService = getContactsServiceFromProperties();
        List<MenuAction> actions = new ArrayList<>();
        actions.add(new ReadAllContactsMenuAction(contactsService));
        actions.add(new AddContactMenuAction(contactsService, reader));
        actions.add(new SearchByNameMenuAction(contactsService, reader));
        actions.add(new SearchByValueMenuAction(contactsService, reader));
        actions.add(new RemoveContactMenuAction(contactsService, reader));
        actions.add(new ExitMenuAction());
        if (contactsService instanceof WithAuthorizationContactsService) {
            actions.add(new CheckInMenuAction((WithAuthorizationContactsService) contactsService, reader));
            actions.add(new SingInMenuAction((WithAuthorizationContactsService) contactsService, reader));
            actions.add(new SingOutMenuAction((WithAuthorizationContactsService) contactsService));
        }
        return actions;
    }

    private static ContactsService getContactsServiceFromProperties() {
        ConfigLoader configLoader = new ConfigLoader();
        AppProperties appProperties = configLoader.getSystemProps(AppProperties.class);
        String filePropName = appProperties.getFilePropName();
        ModeProperties modeProperties = configLoader.getFileProps(ModeProperties.class, filePropName);
        return modeProperties.createContactService(configLoader, filePropName);
    }

}
