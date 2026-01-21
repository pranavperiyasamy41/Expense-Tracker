package com.tracker.dao;

import com.tracker.model.Expense;
import com.tracker.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public void addExpense(Expense e) throws SQLException {

        String sql =
            "INSERT INTO expense (amount, description, expense_date, category_id, note) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, e.getAmount());
            ps.setString(2, e.getDescription());
            ps.setDate(3, Date.valueOf(e.getExpenseDate()));

            if (e.getCategoryId() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, e.getCategoryId());
            }

            ps.setString(5, e.getNote());
            ps.executeUpdate();
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {

        List<Expense> list = new ArrayList<>();

        String sql =
            "SELECT expense_id, amount, description, expense_date, category_id, note " +
            "FROM expense ORDER BY expense_date DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Expense(
                        rs.getInt("expense_id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("description"),
                        rs.getDate("expense_date").toLocalDate(),
                        (Integer) rs.getObject("category_id"),
                        rs.getString("note")
                ));
            }
        }
        return list;
    }
}
