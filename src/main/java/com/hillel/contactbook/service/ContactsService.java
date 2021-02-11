package com.hillel.contactbook.service;


import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.dto.contact.ContactResponse;

import java.util.List;


public interface ContactsService {

    List<Contact> getAllContacts();

    void remove(int index);

    ContactResponse add(Contact contact);

    List<Contact> searchByName(String nameStartsWith);

    List<Contact> searchByValue(String valuePart);

    boolean hasToken();

}
