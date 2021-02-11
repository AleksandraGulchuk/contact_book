package com.hillel.contactbook.menu.actions;

import com.hillel.contactbook.dto.user.SingInResponse;
import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.WithAuthorizationContactsService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class SingInMenuAction implements MenuAction {

    private final WithAuthorizationContactsService contactsService;
    private final BufferedReader reader;

    @Override
    public void doAction() {
        try {
            System.out.println("Введите логин:");
            String login = reader.readLine();
            System.out.println("Введите пароль:");
            String password = reader.readLine();
            SingInResponse singInResponse = contactsService.singIn(login, password);
            if (singInResponse.getStatus().equals("error")) {
                System.out.println(singInResponse.getError());
            } else {
                System.out.println("Вход выполнен успешно");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Войти";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return !contactsService.hasToken();
    }


}
