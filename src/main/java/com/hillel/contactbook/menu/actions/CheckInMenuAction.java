package com.hillel.contactbook.menu.actions;

import com.hillel.contactbook.dto.user.CheckInResponse;
import com.hillel.contactbook.menu.MenuAction;
import com.hillel.contactbook.service.WithAuthorizationContactsService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class CheckInMenuAction implements MenuAction {

    private final WithAuthorizationContactsService contactsService;
    private final BufferedReader reader;

    @Override
    public void doAction() {
        try {
            System.out.println("Введите логин:");
            String login = reader.readLine();
            System.out.println("Введите пароль:");
            String password = reader.readLine();
            System.out.println("Введите дату рождения (yyyy-MM-dd):");
            String dateBorn = reader.readLine();
            CheckInResponse checkInResponse = contactsService.checkIn(login, password, dateBorn);
            if (checkInResponse.getStatus().equals("error")) {
                System.out.println(checkInResponse.getError());
            } else {
                System.out.println("Регистация выполнена успешно");
                System.out.println("Выполните вход");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Зарегистрироваться";
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
