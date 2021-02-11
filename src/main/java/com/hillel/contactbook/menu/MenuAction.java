package com.hillel.contactbook.menu;


public interface MenuAction {

    void doAction();

    String getName();

    boolean closeAfter();

    boolean isVisible();

}
