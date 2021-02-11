package com.hillel.contactbook.config;

import com.hillel.contactbook.annotations.PropAnnotation;
import lombok.Data;


@Data
public class AppProperties {

    @PropAnnotation("contactbook.profile")
    private String profile;

    public String getFilePropName() {
        return "app-" + profile + ".properties";
    }

}
