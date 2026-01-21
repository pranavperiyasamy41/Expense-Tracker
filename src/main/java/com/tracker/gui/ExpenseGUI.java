package com.tracker.gui;

import com.tracker.dao.CategoryDAO;
import com.tracker.dao.ExpenseDAO;
import com.tracker.model.Category;
import com.tracker.model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpenseGUI extends JFrame {

    private JComboBox<Category> categoryBox;
    private JTextField amountField, descField, noteField, dateField;
    private JTable table;
    private DefaultTableModel model;

    private final ExpenseDAO expenseDAO = new ExpenseDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public ExpenseGUI() {
        setTitle("Expense Tracker");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadCategories();
        loadExpenses();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridLayout(2, 5, 5, 5));

        categoryBox = new JComboBox<>();
        amountField = new JTextField();
        descField = new JTextField();
        noteField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString());

        form.add(new JLabel("Category"));
        form.add(new JLabel("Amount"));
        form.add(new JLabel("Description"));
        form.add(new JLabel("Note"));
        form.add(new JLabel("Date (YYYY-MM-DD)"));

        form.add(categoryBox);
        form.add(amountField);
        form.add(descField);
        form.add(noteField);
        form.add(dateField);

        JButton addBtn = new JButton("ADD");
        JButton updateBtn = new JButton("UPDATE");
        JButton deleteBtn = new JButton("DELETE");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        model = new DefaultTableModel(
                new Object[] { "ID", "Amount", "Description", "Date", "Category ID", "Note" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addExpense());
        updateBtn.addActionListener(e -> updateExpense());
        deleteBtn.addActionListener(e -> deleteExpense());

        table.getSelectionModel().addListSelectionListener(e -> fillForm());
    }

    /* ================= LOAD DATA ================= */

    private void loadCategories() {
        try {
            categoryBox.removeAllItems();
            for (Category c : categoryDAO.getAllCategories()) {
                categoryBox.addItem(c);
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadExpenses() {
        try {
            model.setRowCount(0);
            List<Expense> list = expenseDAO.getAllExpenses();
            for (Expense e : list) {
                model.addRow(new Object[] {
                        e.getExpenseId(),
                        e.getAmount(),
                        e.getDescription(),
                        e.getExpenseDate(),
                        e.getCategoryId(),
                        e.getNote()
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    /* ================= ADD ================= */

    private void addExpense() {
        try {
            Category c = (Category) categoryBox.getSelectedItem();

            if (amountField.getText().isEmpty() || descField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Fill all required fields",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Expense e = new Expense(
                    new BigDecimal(amountField.getText().trim()),
                    descField.getText().trim(),
                    LocalDate.parse(dateField.getText().trim()),
                    c == null ? null : c.getCategoryId(),
                    noteField.getText().trim());

            expenseDAO.addExpense(e);
            clearForm();
            loadExpenses();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    /* ================= UPDATE ================= */

    private void updateExpense() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Select an expense to update",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) model.getValueAt(row, 0);
            Expense e = buildExpense(id);
            expenseDAO.updateExpense(e);
            clearForm();
            loadExpenses();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /* ================= DELETE ================= */

    private void deleteExpense() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Select an expense to delete",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) model.getValueAt(row, 0);
            expenseDAO.deleteExpense(id);
            clearForm();
            loadExpenses();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /* ================= HELPERS ================= */

    private Expense buildExpense(Integer id) {
        Category c = (Category) categoryBox.getSelectedItem();

        return new Expense(
                id,
                new BigDecimal(amountField.getText().trim()),
                descField.getText().trim(),
                LocalDate.parse(dateField.getText().trim()),
                c == null ? null : c.getCategoryId(),
                noteField.getText().trim());
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0)
            return;

        amountField.setText(model.getValueAt(row, 1).toString());
        descField.setText(model.getValueAt(row, 2).toString());
        dateField.setText(model.getValueAt(row, 3).toString());
        noteField.setText(
                model.getValueAt(row, 5) == null ? "" : model.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        amountField.setText("");
        descField.setText("");
        noteField.setText("");
        dateField.setText(LocalDate.now().toString());
        table.clearSelection();
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
