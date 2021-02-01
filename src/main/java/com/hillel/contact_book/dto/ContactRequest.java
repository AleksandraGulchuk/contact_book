package com.hillel.contact_book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    private String type;
    private String value;
    private String name;
}
