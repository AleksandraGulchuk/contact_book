package com.hillel.contactbook.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchByNameContactRequest {
    private String name;
}
