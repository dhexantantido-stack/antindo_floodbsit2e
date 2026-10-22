package org.example.ui;

import org.example.dao.UserDao;
import org.example.model.User;

import java.util.Scanner;

public class ResidentMenu {

    private final UserDao userDao;
    private final Scanner scanner;
    private final User currentUser;

    public ResidentMenu(UserDao userDao, Scanner scanner, User currentUser) {
        this.userDao = userDao;
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== RESIDENT MENU (" + currentUser.getUsername() + ") ===");
            // TODO: add features for this role
            System.out.println("1. Logout");
            System.out.print("Choose: ");

            if (scanner.nextLine().trim().equals("1")) {
                running = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
