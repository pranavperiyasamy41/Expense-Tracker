package com.tracker.gui;

import com.tracker.dao.CategoryDAO;
import com.tracker.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CategoryGUI extends JFrame {

    private JTextField nameField;
    private JTextField descField;
    private JTable table;
    private DefaultTableModel model;

    private final CategoryDAO dao = new CategoryDAO();

    public CategoryGUI() {
        setTitle("Category Manager");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadCategories();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));

        form.add(new JLabel("Name:"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Description:"));
        descField = new JTextField();
        form.add(descField);

        JButton addBtn = new JButton("ADD");
        JButton updateBtn = new JButton("UPDATE");
        JButton deleteBtn = new JButton("DELETE");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Description"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addCategory());
        updateBtn.addActionListener(e -> updateCategory());
        deleteBtn.addActionListener(e -> deleteCategory());

        table.getSelectionModel().addListSelectionListener(e -> fillForm());
    }

    private void loadCategories() {
        try {
            model.setRowCount(0);
            List<Category> list = dao.getAllCategories();
            for (Category c : list) {
                model.addRow(new Object[]{
                        c.getCategoryId(),
                        c.getName(),
                        c.getDescription()
                });
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void addCategory() {
        try {
            dao.addCategory(
                    nameField.getText().trim(),
                    descField.getText().trim()
            );
            clearForm();
            loadCategories();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void updateCategory() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);
            dao.updateCategory(
                    id,
                    nameField.getText().trim(),
                    descField.getText().trim()
            );
            loadCategories();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteCategory() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);
            dao.deleteCategory(id);
            clearForm();
            loadCategories();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        nameField.setText(model.getValueAt(row, 1).toString());
        descField.setText(
                model.getValueAt(row, 2) == null ? "" :
                        model.getValueAt(row, 2).toString()
        );
    }

    private void clearForm() {
        nameField.setText("");
        descField.setText("");
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
