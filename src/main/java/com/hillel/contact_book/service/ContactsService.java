package com.hillel.contact_book.service;


import com.hillel.contact_book.contacts.Contact;
import com.hillel.contact_book.dto.ContactResponse;

import java.util.List;


public interface ContactsService {

    List<Contact> getAllContacts();

    void remove(int index);

    ContactResponse add(Contact contact);

    List<Contact> searchByName(String nameStartsWith);

    List<Contact> searchByValue(String valuePart);

    boolean hasToken();

}
