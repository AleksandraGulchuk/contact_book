package com.hillel.contactbook.factory.menu;

import com.hillel.contactbook.menu.Menu;

public interface MenuBuilder {

    MenuBuilder add();
    MenuBuilder checkIn();
    MenuBuilder exit();
    MenuBuilder readAll();
    MenuBuilder remove();
    MenuBuilder searchByName();
    MenuBuilder searchByValue();
    MenuBuilder singIn();
    MenuBuilder singOut();
    Menu build();

}
