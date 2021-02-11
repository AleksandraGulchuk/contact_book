package com.hillel.contactbook.menu.actions;


import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.WithAuthorizationContactsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingOutMenuAction implements MenuAction {
    private final WithAuthorizationContactsService contactsService;

    @Override
    public void doAction() {
        contactsService.singOut();
        System.out.println("Выход выполнен успешно");
    }

    @Override
    public String getName() {
        return "Выйти из аккаунта";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return contactsService.hasToken();
    }
}
