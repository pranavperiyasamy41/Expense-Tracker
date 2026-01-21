package com.tracker.dao;

import com.tracker.model.Category;
import com.tracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // CREATE
    public void addCategory(String name, String description) throws SQLException {
        String sql = "INSERT INTO category (name, description) VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.executeUpdate();
        }
    }

    // READ
    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();

        String sql = "SELECT category_id, name, description FROM category";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        }
        return list;
    }

    // UPDATE
    public void updateCategory(int id, String name, String description) throws SQLException {
        String sql = "UPDATE category SET name = ?, description = ? WHERE category_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM category WHERE category_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
