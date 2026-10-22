package org.example;

import org.example.dao.DatabaseInitializer;
import org.example.dao.UserDao;
import org.example.ui.Authentication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize database
        DatabaseInitializer.initialize();

        // 2. Instantiate shared DAOs & utilities
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);

        // 3. Delegate to Authentication
        new Authentication(userDao, scanner).start();

        scanner.close();
    }
}
