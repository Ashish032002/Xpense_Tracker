package com.xpensetracker.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.Date;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private double amount;
    private String category;
    private String paymentMode;
    private Date date;
    private String receiptImagePath;
    private String notes;

    public Expense(String title, double amount, String category, String paymentMode, Date date) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMode = paymentMode;
        this.date = date;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getReceiptImagePath() { return receiptImagePath; }
    public void setReceiptImagePath(String receiptImagePath) { this.receiptImagePath = receiptImagePath; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}