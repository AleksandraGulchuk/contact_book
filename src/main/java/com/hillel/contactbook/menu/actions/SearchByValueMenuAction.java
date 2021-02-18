package com.hillel.contactbook.menu.actions;


import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.contacts.ContactsService;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class SearchByValueMenuAction implements MenuAction {

    private final ContactsService contactsService;
    private final BufferedReader reader;

    @Override
    public void doAction() {
        System.out.println("Введите часть номера телефона или адреса электронной почты: ");
        List<Contact> foundContacts = Collections.emptyList();
        try {
            foundContacts = contactsService.searchByValue(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (foundContacts.isEmpty()) {
            System.out.println("Контакты не найдены.");
        } else {
            System.out.println("Найдены контакты:");
            foundContacts.forEach(System.out::println);
        }

    }

    @Override
    public String getName() {
        return "Поиск по части номера или адреса электронной почты";
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
