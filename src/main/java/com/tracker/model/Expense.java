package com.tracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {

    private int expenseId;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;
    private Integer categoryId; // nullable
    private String note;

    public Expense(int expenseId, BigDecimal amount, String description,
                   LocalDate expenseDate, Integer categoryId, String note) {
        this.expenseId = expenseId;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.categoryId = categoryId;
        this.note = note;
    }

    public Expense(BigDecimal amount, String description,
                   LocalDate expenseDate, Integer categoryId, String note) {
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
        this.categoryId = categoryId;
        this.note = note;
    }

    public int getExpenseId() { return expenseId; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public Integer getCategoryId() { return categoryId; }
    public String getNote() { return note; }
}
