package com.hillel.contact_book.service;


import com.hillel.contact_book.contacts.Contact;
import com.hillel.contact_book.contacts.ContactWorker;
import com.hillel.contact_book.dto.ContactResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryContactsService implements ContactsService {

    private final List<Contact> contactsList = new ArrayList<>();

    @Override
    public List<Contact> getAllContacts() {
        return contactsList;
    }

    @Override
    public void remove(int index) {
        ContactWorker contactWorker = new ContactWorker();
        if (contactWorker.isIndex(index, contactsList)) {
            Contact removedContact = contactsList.remove(index - 1);
            System.out.println(removedContact + " удален из телефонной книги.");
        }
    }

    @Override
    public ContactResponse add(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        if (contactsList.contains(contact)) {
            contactResponse.setStatus("error");
            contactResponse.setMessage(contact + " уже существует в телефонной книге!");
        } else {
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
