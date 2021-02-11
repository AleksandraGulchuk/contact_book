package com.hillel.contactbook.service;


import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.dto.contact.ContactResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryContactsService implements ContactsService {

    private final List<Contact> contactsList = new ArrayList<>();

    private int newId() {
        return contactsList.stream().map(Contact::getId)
                .max(Comparator.comparingInt(a -> a))
                .map(id -> id + 1).orElse(1);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactsList;
    }

    @Override
    public void remove(int index) {
        boolean result = false;
        for (int i = 0; i < contactsList.size(); i++) {
            if (contactsList.get(i).getId().equals(index)) {
                System.out.println("Контакт " + contactsList.get(i) + " удален из телефонной книги.");
                result = contactsList.remove(contactsList.get(i));
            }
        }
        if (!result) System.out.println("Контакт не найден");
    }

    @Override
    public ContactResponse add(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        if (contactsList.contains(contact)) {
            contactResponse.setStatus("error");
            contactResponse.setMessage(contact + " уже существует в телефонной книге!");
        } else {
            contact.setId(newId());
            contactsList.add(contact);
            contactResponse.setStatus("ok");
        }
        return contactResponse;
    }

    @Override
    public List<Contact> searchByName(String nameStartsWith) {
        List<Contact> contacts = getAllContacts();
        if (contacts.isEmpty()) {
            return contacts;
        }
        return contacts.stream()
                .filter(contact -> contact.getName().startsWith(nameStartsWith))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> searchByValue(String valuePart) {
        List<Contact> contacts = getAllContacts();
        if (contacts.isEmpty()) {
            return contacts;
        }
        return contacts.stream()
                .filter(contact -> contact.getValue().contains(valuePart))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasToken() {
        return true;
    }

}
