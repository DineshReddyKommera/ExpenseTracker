/**
 * Chart Data Unit object model is used for as generating charts
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker;

public class ChartDataUnit {
    String date;
    String expenseName;
    int expenseAmount;

    public ChartDataUnit(String date, String expenseName, int expenseAmount) {
        this.date = date;
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
    }

    public ChartDataUnit() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    @Override
    public String toString() {
        return "ChartDataUnit{" +
                "date='" + date + '\'' +
                ", expenseName='" + expenseName + '\'' +
                ", expenseAmount=" + expenseAmount +
                '}';
    }
}
