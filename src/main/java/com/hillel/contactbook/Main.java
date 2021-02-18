package com.hillel.contactbook;

import com.hillel.contactbook.menu.Menu;
import com.hillel.contactbook.factory.menu.MenuFactory;


public class Main {
    public static void main(String[] args) {

        Menu menu = new MenuFactory().buildMenu();
        menu.run();

    }
}
