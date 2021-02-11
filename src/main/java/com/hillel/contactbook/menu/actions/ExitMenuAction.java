package com.hillel.contactbook.menu.actions;


import com.hillel.contactbook.menu.MenuAction;

public class ExitMenuAction implements MenuAction {

    @Override
    public void doAction() {
        System.out.println("Спасибо! Хорошего дня!");
    }

    @Override
    public String getName() {
        return "Выход";
    }

    @Override
    public boolean closeAfter() {
        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
