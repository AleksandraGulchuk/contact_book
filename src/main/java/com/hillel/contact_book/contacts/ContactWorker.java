package com.hillel.contact_book.contacts;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactWorker {

    public Type defineTypeFromValue(String value) {
        if (value.matches("\\+38\\d{10}")) return Type.PHONE;
        if (value.matches("\\w+@\\w+\\.\\w+")) return Type.EMAIL;
        return null;
    }

    public Contact getContactFromLine(String currentLine) {
        Pattern pattern = Pattern.compile("(.+)(?:\\[\\w+:)(.+)(?:])");
        Matcher matcher = pattern.matcher(currentLine);
        if (matcher.find()) {
            String name = matcher.group(1);
            String phone = matcher.group(2);
            return new Contact(name, defineTypeFromValue(phone), phone);
        } else return null;
    }

    public boolean isIndex(int index, List<Contact> contactsList) {
        if (index > contactsList.size() || index <= 0) {
            System.out.println("Контакта с таким порядковым номером не существует!");
            return false;
        }
        return true;
    }

}
