package com.hillel.contactbook.factory.menu;

import com.hillel.contactbook.menu.Menu;
import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.menu.actions.*;
import com.hillel.contactbook.service.contacts.ContactsService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ContactsMenuBuilder implements MenuBuilder {

    private final List<MenuAction> actions = new ArrayList<>();
    private final ContactsService contactsService;
    private final BufferedReader reader;


    @Override
    public MenuBuilder add() {
        actions.add(new AddContactMenuAction(contactsService, reader));
        return this;
    }

    @Override
    public MenuBuilder checkIn() {
        actions.add(new CheckInMenuAction(contactsService, contactsService.getUserService(), reader));
        return this;
    }

    @Override
    public MenuBuilder exit() {
        actions.add(new ExitMenuAction());
        return this;
    }

    @Override
    public MenuBuilder readAll() {
        actions.add(new ReadAllContactsMenuAction(contactsService));
        return this;
    }

    @Override
    public MenuBuilder remove() {
        actions.add(new RemoveContactMenuAction(contactsService, reader));
        return this;
    }

    @Override
    public MenuBuilder searchByName() {
        actions.add(new SearchByNameMenuAction(contactsService, reader));
        return this;
    }

    @Override
    public MenuBuilder searchByValue() {
        actions.add(new SearchByValueMenuAction(contactsService, reader));
        return this;
    }

    @Override
    public MenuBuilder singIn() {
        actions.add(new SingInMenuAction(contactsService, contactsService.getUserService(), reader));
        return this;
    }

    @Override
    public MenuBuilder singOut() {
        actions.add(new SingOutMenuAction(contactsService, contactsService.getUserService()));
        return this;
    }

    @Override
    public Menu build() {
        return new Menu(actions, reader);
    }
}
