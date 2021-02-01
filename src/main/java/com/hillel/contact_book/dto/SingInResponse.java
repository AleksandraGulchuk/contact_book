package com.hillel.contact_book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingInResponse {
    private String status;
    private String error;
    private String token;
}
