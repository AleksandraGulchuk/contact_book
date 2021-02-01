package com.hillel.contact_book.dto;

import com.hillel.contact_book.contacts.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {

    private List<Contact> contacts;
    private String status;
    private String error;
    private String message;

}
