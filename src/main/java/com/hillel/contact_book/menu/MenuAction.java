package com.hillel.contact_book.menu;


public interface MenuAction {

    void doAction();

    String getName();

    boolean closeAfter();

    boolean isVisible();

}
