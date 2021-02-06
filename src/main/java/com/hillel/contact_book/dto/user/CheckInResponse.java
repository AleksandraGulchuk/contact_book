package com.hillel.contact_book.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInResponse {
    private String status;
    private String error;
    private String token;
}
