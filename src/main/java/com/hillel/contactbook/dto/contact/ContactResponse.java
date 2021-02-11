package com.hillel.contactbook.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {

    private List<ContactForResponse> contacts;
    private String status;
    private String error;
    private String message;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactForResponse{
        private String value;
        private String name;
        private String type;
        private Integer id;
    }

}
