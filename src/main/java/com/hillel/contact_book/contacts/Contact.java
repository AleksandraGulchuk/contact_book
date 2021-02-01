package com.hillel.contact_book.contacts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private String name;
    private String type;
    private Type typeType;
    private String value;
    private int id;

    public Contact(String name, Type typeType, String value) {
        this.name = name;
        this.typeType = typeType;
        this.value = value;
    }

    @Override
    public String toString() {
        return "type: " + (type != null ? type : typeType.getValue()) +
                ", name: " + name +
                ", contact: " + value;
    }
}
