package org.example.ui;

import org.example.dao.UserDao;
import org.example.model.Role;
import org.example.model.Status;
import org.example.model.User;

import java.util.Scanner;

/** Handles login, registration and role routing. No SQL here: all data access goes through UserDao. */
public class Authentication {

    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== FLOOD HOUSING SYSTEM ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }

        Role role = chooseRole();
        if (role == null) return;

        User user = new User(username, password, fullName, role, Status.PENDING);
        if (userDao.register(user)) {
            System.out.println("Registered as " + role + ". Status: PENDING (wait for admin approval).");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private Role chooseRole() {
        Role[] roles = Role.values();
        System.out.println("Select role:");
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i]);
        }
        System.out.print("Choose: ");
        try {
            int n = Integer.parseInt(scanner.nextLine().trim());
            if (n >= 1 && n <= roles.length) return roles[n - 1];
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Invalid role.");
        return null;
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDao.findByUsername(username);
        if (user == null || !password.equals(user.getPassword())) {
            System.out.println("Invalid username or password.");
            return;
        }
        if (user.getStatus() == Status.PENDING) {
            System.out.println("Your account is still PENDING approval.");
            return;
        }
        if (user.getStatus() == Status.REJECTED) {
            System.out.println("Your account was REJECTED. Contact the administrator.");
            return;
        }

        System.out.println("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        routeByRole(user);
    }

    private void routeByRole(User user) {
        switch (user.getRole()) {
            case ADMIN -> new AdminMenu(userDao, scanner, user).show();
            case HOUSING_OFFICER -> new HousingOfficerMenu(userDao, scanner, user).show();
            case RESIDENT -> new ResidentMenu(userDao, scanner, user).show();
        }
    }
}
