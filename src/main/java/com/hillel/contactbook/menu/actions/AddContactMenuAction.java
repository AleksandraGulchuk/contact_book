package com.hillel.contactbook.menu.actions;


import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.contacts.ContactWorker;
import com.hillel.contactbook.contacts.Type;
import com.hillel.contactbook.dto.contact.ContactResponse;
import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.contacts.ContactsService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class AddContactMenuAction implements MenuAction {

    private final ContactsService contactsService;
    private final BufferedReader reader;


    @Override
    public void doAction() {
        ContactWorker contactWorker = new ContactWorker();
        try {
            System.out.println("Введите имя контакта: ");
            String name = reader.readLine();
            System.out.println("Введите номер телефона или адрес электронной почты: ");
            String value = reader.readLine();
            Type type = contactWorker.defineTypeFromValue(value);
            if (type == null) {
                System.out.println("Вы ввели некорректное значение!");
            } else {
                ContactResponse contactResponse = contactsService.add(new Contact(name, type, value));
                if (contactResponse.getStatus().equals("error")) {
                    System.out.println(contactResponse.getError());
                } else {
                    System.out.println("Контакт добавлен");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Добавить контакт";
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
