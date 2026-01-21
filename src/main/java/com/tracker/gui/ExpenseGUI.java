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
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField noteField;

    private JTable table;
    private DefaultTableModel tableModel;

    private CategoryDAO categoryDAO = new CategoryDAO();
    private ExpenseDAO expenseDAO = new ExpenseDAO();

    public ExpenseGUI() {
        setTitle("Expense Tracker");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadCategories();
        loadExpenses();
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Category:"));
        categoryBox = new JComboBox<>();
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        formPanel.add(amountField);

        formPanel.add(new JLabel("Note:"));
        noteField = new JTextField();
        formPanel.add(noteField);

        JButton addButton = new JButton("ADD EXPENSE");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Expense ID", "Amount", "Description", "Date", "Category ID", "Note"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> addExpense());
    }

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
            tableModel.setRowCount(0);
            List<Expense> list = expenseDAO.getAllExpenses();
            for (Expense e : list) {
                tableModel.addRow(new Object[]{
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

    private void addExpense() {
        try {
            Category category = (Category) categoryBox.getSelectedItem();
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            String description = descriptionField.getText().trim();
            String note = noteField.getText().trim();

            Expense expense = new Expense(
                    amount,
                    description,
                    LocalDate.now(),
                    category != null ? category.getCategoryId() : null,
                    note.isEmpty() ? null : note
            );

            expenseDAO.addExpense(expense);

            descriptionField.setText("");
            amountField.setText("");
            noteField.setText("");
            loadExpenses();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
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
