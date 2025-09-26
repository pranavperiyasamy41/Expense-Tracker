package com.model;
import java.time.LocalDate;

public class Expense {
    private int expenseId;
    private int categoryId;
    private double amount;
    private LocalDate expenseAt;
    private String note;

    public Expense(){
        this.expenseAt = LocalDate.now();
    }
    public Expense(int expenseId, int categoryId, double amount, LocalDate expenseAt, String note) {
        this.expenseId = expenseId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.expenseAt=expenseAt;
        this.note = note;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseAt() {
        return expenseAt;
    }

    public void setExpenseAt() {
        this.expenseAt = expenseAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}