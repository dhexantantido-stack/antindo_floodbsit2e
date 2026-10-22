package org.example.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {}

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    username      TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    full_name     TEXT NOT NULL,
                    role          TEXT NOT NULL,
                    status        TEXT NOT NULL DEFAULT 'PENDING',
                    created_at    TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);

            // TODO: add the remaining tables from your approved ERD here
            // (use CREATE TABLE IF NOT EXISTS, parent tables first, with FOREIGN KEY clauses).

            System.out.println("[DB] Database initialized.");
        } catch (SQLException e) {
            throw new IllegalStateException("Database initialization failed: " + e.getMessage(), e);
        }
    }
}
