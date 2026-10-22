package org.example.ui;

import org.example.dao.UserDao;
import org.example.model.Status;
import org.example.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final UserDao userDao;
    private final Scanner scanner;
    private final User currentUser;

    public AdminMenu(UserDao userDao, Scanner scanner, User currentUser) {
        this.userDao = userDao;
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ADMIN MENU (" + currentUser.getUsername() + ") ===");
            System.out.println("1. Review pending accounts");
            System.out.println("2. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> reviewPending();
                case "2" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void reviewPending() {
        List<User> pending = userDao.findByStatus(Status.PENDING);
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
            return;
        }
        for (User u : pending) {
            System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getFullName() + " | " + u.getRole());
        }
        System.out.print("Enter user ID to review (blank to cancel): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;

        try {
            int id = Integer.parseInt(input);
            System.out.print("(A)pprove or (R)eject? ");
            String choice = scanner.nextLine().trim().toUpperCase();
            if (choice.equals("A")) {
                System.out.println(userDao.updateStatus(id, Status.APPROVED) ? "Approved." : "Failed.");
            } else if (choice.equals("R")) {
                System.out.println(userDao.updateStatus(id, Status.REJECTED) ? "Rejected." : "Failed.");
            } else {
                System.out.println("Cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }
}
