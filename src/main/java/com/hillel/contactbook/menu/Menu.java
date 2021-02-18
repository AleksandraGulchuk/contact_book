package com.hillel.contactbook.menu;

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
            if ((choice) > actionsToDisplay.size() || (choice) <= 0) {
                System.out.println("Вы ввели некорректное значение!");
                continue;
            }
            if (actionsToDisplay.get(choice - 1).isVisible()) {
                actionsToDisplay.get(choice - 1).doAction();
            }
            isCloseAfter = actionsToDisplay.get(choice - 1).closeAfter();
        }
    }

    private List<MenuAction> displayActionsName() {
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
            try {
                String choiceString = reader.readLine();
                if (validateMenuIndex(choiceString)) {
                    return Integer.parseInt(choiceString);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
