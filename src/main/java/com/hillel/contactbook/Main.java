package com.hillel.contactbook;

import com.hillel.contactbook.menu.Menu;
import com.hillel.contactbook.config.MenuActionsCollector;

import java.io.*;


public class Main {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Menu menu = new Menu(MenuActionsCollector.collectMenuActions(reader), reader);
        menu.run();

    }
}
