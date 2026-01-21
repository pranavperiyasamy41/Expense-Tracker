package com.tracker.gui;

import com.tracker.dao.CategoryDAO;
import com.tracker.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CategoryGUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private CategoryDAO categoryDAO = new CategoryDAO();

    public CategoryGUI() {
        setTitle("Category List");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadCategories();
    }

    private void initUI() {
        tableModel = new DefaultTableModel(
                new Object[]{"Category ID", "Name", "Description"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadCategories() {
        try {
            tableModel.setRowCount(0);
            List<Category> list = categoryDAO.getAllCategories();
            for (Category c : list) {
                tableModel.addRow(new Object[]{
                        c.getCategoryId(),
                        c.getName(),
                        c.getDescription()
                });
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
