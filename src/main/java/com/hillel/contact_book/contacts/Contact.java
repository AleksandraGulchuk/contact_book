package com.hillel.contact_book.contacts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Integer id;
    private String name;
    private Type contactType;
    private String value;

    public Contact(String name, Type contactType, String value) {
        this.name = name;
        this.contactType = contactType;
        this.value = value;
    }

    @Override
    public String toString() {
        return "type: " + contactType.getValue() +
                ", name: " + name +
                ", contact: " + value +
                ", id: " + id;
    }

}
