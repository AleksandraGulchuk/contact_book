package com.hillel.contactbook.menu.actions;

import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.contacts.ContactsService;

import java.io.BufferedReader;
import java.io.IOException;

public class RemoveContactMenuAction implements MenuAction {

    private final ContactsService contactsService;
    private final BufferedReader reader;

    public RemoveContactMenuAction(ContactsService contactsService, BufferedReader reader) {
        this.contactsService = contactsService;
        this.reader = reader;
    }

    @Override
    public void doAction() {
        System.out.println("Введите номер контакта: ");
        try {
            int index = Integer.parseInt(reader.readLine());
            contactsService.remove(index);
        } catch (NumberFormatException | IOException exception) {
            System.out.println("Вы ввели некорректное значение!");
        }
    }

    @Override
    public String getName() {
        return "Удалить контакт";
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