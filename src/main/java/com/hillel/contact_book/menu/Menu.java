package com.hillel.contact_book.menu;

import lombok.RequiredArgsConstructor;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Menu {

    private final List<MenuAction> actions;
    private final BufferedReader reader;

    public void run() {
        boolean isCloseAfter = false;
        int choice;
        while (!isCloseAfter) {
            List<MenuAction> actionsToDisplay = displayActionsName();
            choice = getChoice();
            if (actionsToDisplay.get(choice - 1).isVisible()) {
                actionsToDisplay.get(choice - 1).doAction();
            }
            isCloseAfter = actionsToDisplay.get(choice - 1).closeAfter();
        }
    }

    private List<MenuAction>  displayActionsName() {
        List<MenuAction> actionsToDisplay = actions.stream()
                .filter(MenuAction::isVisible)
                .collect(Collectors.toList());
        for (int i = 0; i < actionsToDisplay.size(); i++) {
            System.out.println((i + 1) + " - " + actionsToDisplay.get(i).getName());
        }
        return actionsToDisplay;
    }

    private int getChoice() {
        while (true) {
            System.out.println("Сделайте свой выбор:");
            String choiceString = null;
            try {
                choiceString = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (validateMenuIndex(choiceString)) {
                return Integer.parseInt(choiceString);
            }
        }
    }

    private boolean validateMenuIndex(String choiceString) {
        int choice = -1;
        try {
            choice = Integer.parseInt(choiceString);
        } catch (NumberFormatException exception) {
            System.out.println("Вы ввели некорректное значение!");
        }
        return choice > 0 && choice <= actions.size();
    }

}
