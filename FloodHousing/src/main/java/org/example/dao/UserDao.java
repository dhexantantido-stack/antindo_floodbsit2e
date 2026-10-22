package org.example.dao;

import org.example.model.Role;
import org.example.model.Status;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, full_name, role, status) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getStatus().name());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("[DB] Register failed: " + e.getMessage());
            return false;
        }
    }

    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            System.out.println("[DB] Lookup failed: " + e.getMessage());
            return null;
        }
    }

    public List<User> findByStatus(Status status) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Query failed: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int userId, Status status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("[DB] Update failed: " + e.getMessage());
            return false;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(Role.valueOf(rs.getString("role")));
        u.setStatus(Status.valueOf(rs.getString("status")));
        return u;
    }
}
