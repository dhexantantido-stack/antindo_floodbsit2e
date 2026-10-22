package org.example.ui;

import org.example.dao.UserDao;
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
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> viewPendingUsers();
                case "3" -> approveUser();
                case "4" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n--- All System Users ---");
        printUserTable(userDao.getAllUsers());
    }

    private void viewPendingUsers() {
        System.out.println("\n--- Pending Registrations ---");
        printUserTable(userDao.getPendingUsers());
    }

    private void approveUser() {
        List<User> pending = userDao.getPendingUsers();
        System.out.println("\n--- Approve User Account ---");
        if (pending.isEmpty()) {
            System.out.println("No pending accounts to approve.");
            return;
        }
        printUserTable(pending);

        while (true) {
            System.out.print("Enter User ID to approve (0 to cancel): ");
            String input = scanner.nextLine().trim();

            int id;
            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (id == 0) {
                System.out.println("Approval cancelled.");
                return;
            }

            User target = findById(pending, id);
            if (target == null) {
                System.out.println("ID " + id + " is not in the pending list. Try again.");
                continue;
            }

            if (userDao.approveUser(id)) {
                System.out.println("SUCCESS: " + target.getUsername() + " (" + target.getRole()
                        + ") has been APPROVED.");
            } else {
                System.out.println("Approval failed. Please try again.");
            }
            return;
        }
    }

    private User findById(List<User> users, int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    /** Prints any list of users as an aligned table. */
    private void printUserTable(List<User> users) {
        String border = "+-------+-----------------+------------------+------------+";
        System.out.println(border);
        System.out.printf("| %-5s | %-15s | %-16s | %-10s |%n", "ID", "Username", "Role", "Status");
        System.out.println(border);
        if (users.isEmpty()) {
            System.out.printf("| %-52s |%n", "No records found.");
        } else {
            for (User u : users) {
                System.out.printf("| %-5d | %-15s | %-16s | %-10s |%n",
                        u.getId(), u.getUsername(), u.getRole(), u.getStatus());
            }
        }
        System.out.println(border);
    }
}