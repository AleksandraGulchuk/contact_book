package com.hillel.contact_book;

import com.hillel.contact_book.menu.Menu;
import com.hillel.contact_book.menu.MenuActionsCollector;

import java.io.*;



public class Main {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Menu menu = new Menu(MenuActionsCollector.collectMenuActions(reader), reader);
        menu.run();

    }
}
