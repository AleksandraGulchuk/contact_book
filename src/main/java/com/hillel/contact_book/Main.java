package com.hillel.contact_book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hillel.contact_book.menu.Menu;
import com.hillel.contact_book.menu.MenuCollector;
import com.hillel.contact_book.service.ApiContactsService;
import com.hillel.contact_book.service.ContactsService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpClient;


public class Main {
    public static void main(String[] args) {

        ContactsService contactsService = new ApiContactsService(HttpClient.newBuilder().build(), new ObjectMapper());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Menu menu = new Menu(MenuCollector.collectMenu(contactsService, reader), reader);
        menu.run();

    }
}
