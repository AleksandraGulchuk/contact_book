package com.hillel.contactbook.config;

import com.hillel.contactbook.annotations.PropAnnotation;
import lombok.Data;


@Data
public class AppSystemProperty {

    @PropAnnotation("contactbook.profile")
    private String profile;

    public String getProfileName() {
        return "app-" + profile + ".properties";
    }

}
