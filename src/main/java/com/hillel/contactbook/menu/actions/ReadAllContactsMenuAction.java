package com.hillel.contactbook.menu.actions;


import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.contacts.ContactsService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReadAllContactsMenuAction implements MenuAction {

    private final ContactsService contactsService;

    @Override
    public void doAction() {
        System.out.println("Ваш список контактов:");
        List<Contact> contacts = contactsService.getAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("Список пуст!");
        } else contacts.forEach(contact -> System.out.println(contact));
    }

    @Override
    public String getName() {
        return "Показать список";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return contactsService.hasToken();
    }
}
