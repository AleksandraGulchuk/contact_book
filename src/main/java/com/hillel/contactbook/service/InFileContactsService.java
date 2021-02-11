package com.hillel.contactbook.service;


import com.hillel.contactbook.annotations.PropAnnotation;
import com.hillel.contactbook.contacts.Contact;
import com.hillel.contactbook.contacts.ContactWorker;
import com.hillel.contactbook.dto.contact.ContactResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public class InFileContactsService implements ContactsService {

    @PropAnnotation("file.path")
    private String fileName;


    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contactsList = new ArrayList<>();
        createFile();
        ContactWorker contactWorker = new ContactWorker();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                Contact contact = contactWorker.getContactFromFileLine(currentLine);
                if (contact != null) contactsList.add(contact);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    @Override
    public void remove(int index) {
        List<Contact> contactsList = getAllContacts();
        ContactWorker contactWorker = new ContactWorker();
        if (contactWorker.isIndex(index, contactsList)) {
            Contact removedContact = contactsList.remove(index - 1);
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));) {
                if (removedContact != null) {
                    System.out.println(removedContact + " удален из телефонной книги.");
                }
                for (Contact contact : contactsList) {
                    bufferedWriter.write(contact.getId() + "."
                            + contact.getName()
                            + "[" + contact.getContactType().getValue()
                            + ":" + contact.getValue() + "]" + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ContactResponse add(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            List<Contact> contactsList = getAllContacts();
            if (contactsList.contains(contact)) {
                contactResponse.setStatus("error");
                contactResponse.setMessage(contact + " уже существует в телефонной книге!");
            } else {
                int id = contactsList.stream().map(Contact::getId)
                        .max(Comparator.comparingInt(a -> a))
                        .map(num -> num + 1).orElse(1);
                contact.setId(id);
                bufferedWriter.write(contact.getId() + "."
                        + contact.getName()
                        + "[" + contact.getContactType().getValue()
                        + ":" + contact.getValue() + "]" + "\n");
                contactResponse.setStatus("ok");
            }
        } catch (IOException e) {
            contactResponse.setStatus("error");
            contactResponse.setMessage(e.getMessage());
        }
        return contactResponse;
    }

    @Override
    public List<Contact> searchByName(String nameStartsWith) {
        List<Contact> contacts = getAllContacts();
        return contacts.stream()
                .filter(contact -> contact.getName().startsWith(nameStartsWith))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> searchByValue(String phonePart) {
        List<Contact> contacts = getAllContacts();
        return contacts.stream()
                .filter(contact -> contact.getValue().contains(phonePart))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasToken() {
        return true;
    }

    private void createFile() {
        try {
            new File(fileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}