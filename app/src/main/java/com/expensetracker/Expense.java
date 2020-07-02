/**
 * This method is a POJO class which is used to maintain data of
 * saved expenses and daily expenses
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */

package com.expensetracker;

import java.io.Serializable;

public class Expense implements Serializable {
    public int expenseAmount;
    public String createdDate;
    private int savedExpenseID;
    private String expenseName;
    private String username;
    private int dailyExpenseID;

    public Expense(int expenseAmount, String createdDate, int savedExpenseID, String expenseName, String username, int dailyExpenseID) {
        this.expenseAmount = expenseAmount;
        this.createdDate = createdDate;
        this.savedExpenseID = savedExpenseID;
        this.expenseName = expenseName;
        this.username = username;
        this.dailyExpenseID = dailyExpenseID;
    }

    public Expense() {

    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getSavedExpenseID() {
        return savedExpenseID;
    }

    public void setSavedExpenseID(int savedExpenseID) {
        this.savedExpenseID = savedExpenseID;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDailyExpenseID() {
        return dailyExpenseID;
    }

    public void setDailyExpenseID(int dailyExpenseID) {
        this.dailyExpenseID = dailyExpenseID;
    }

    @Override
    public String toString() {
        return expenseName;
    }
}
