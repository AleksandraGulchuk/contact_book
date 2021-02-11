package com.hillel.contactbook.config;

import com.hillel.contactbook.annotations.PropAnnotation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ConfigLoader {

    public <T> T getSystemProps(Class<T> clazz) {
        Object object = createObject(clazz);
        extractedProps(object, System.getProperties());
        return (T) object;
    }

    public <T> T getFileProps(Class<T> clazz, String filePropName) {
        try (InputStream inputStream = new FileInputStream(filePropName)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Object object = createObject(clazz);
            extractedProps(object, properties);
            return (T) object;
        } catch (IOException e) {
            throw new RuntimeException("Fail load properties file " + filePropName, e);
        }
    }

    private void extractedProps(Object object, Properties properties) {
        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PropAnnotation.class)) {
                PropAnnotation annotation = field.getAnnotation(PropAnnotation.class);
                String propName = annotation.value();
                String value = properties.getProperty(propName);
                field.setAccessible(true);
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Object createObject(Class clazz) {
        try {
            Constructor constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Need default constructor", e);
        }
    }
}
