package com.hillel.contact_book.menu.actions;


import com.hillel.contact_book.contacts.Contact;
import com.hillel.contact_book.menu.MenuAction;
import com.hillel.contact_book.service.ContactsService;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SearchByValueMenuAction implements MenuAction {

    private final ContactsService contactsService;
    private final BufferedReader reader;

    @Override
    public void doAction() {
        System.out.println("Введите часть номера телефона или адреса электронной почты: ");
        List<Contact> foundContacts = new ArrayList<>();
        try {
            foundContacts = contactsService.searchByValue(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (foundContacts.isEmpty()) {
            System.out.println("Контакты не найдены.");
        } else {
            System.out.println("Найдены контакты:");
            foundContacts.forEach(contact -> System.out.println(contact));
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
