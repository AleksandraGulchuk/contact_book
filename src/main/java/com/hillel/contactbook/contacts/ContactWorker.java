package com.hillel.contactbook.contacts;

import com.hillel.contactbook.dto.contact.ContactResponse;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactWorker {

    public Type defineTypeFromValue(String value) {
        if (value.matches("\\+38\\d{10}")) return Type.PHONE;
        if (value.matches("\\w+@\\w+\\.\\w+")) return Type.EMAIL;
        return null;
    }

    public Contact getContactFromFileLine(String currentLine) {
        Pattern pattern = Pattern.compile("(\\d+).(.+)(?:\\[\\w+:)(.+)(?:])");
        Matcher matcher = pattern.matcher(currentLine);
        if (matcher.find()) {
            Integer id = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2);
            String phone = matcher.group(3);
            return new Contact(id, name, defineTypeFromValue(phone), phone);
        } else return null;
    }

    public boolean isIndex(int index, List<Contact> contactsList) {
        if (index > contactsList.size() || index <= 0) {
            System.out.println("Контакта с таким порядковым номером не существует!");
            return false;
        }
        return true;
    }

    public Contact getContactFromContactForResponse(ContactResponse.ContactForResponse contactForResponse) {
        return new Contact(contactForResponse.getId(), contactForResponse.getName(),
                Type.valueOf(contactForResponse.getType().toUpperCase(Locale.ROOT)),
                contactForResponse.getValue());
    }

    public ContactResponse.ContactForResponse getContactForResponseFromContact(Contact contact) {
        Integer id = contact.getId();
        String name = contact.getName();
        String type = contact.getContactType().getValue();
        String value = contact.getValue();
        return new ContactResponse.ContactForResponse(value, name, type, id);
    }

}
