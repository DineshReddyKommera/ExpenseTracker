/**
 * User Object for storing user details
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker;

public class User {
    String mEmail;
    String mUsername;
    String mPwd;
    String mPhone;
    int annualIncome;
    int desiredSaving;
    int maximumDailyExpense;
    public User(String username, String email, String pwd, String phone) {
        mUsername = username;
        mEmail = email;
        mPwd = pwd;
        mPhone = phone;
    }

    public User() {

    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPwd(String pwd) {
        mPwd = pwd;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }


    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPwd() {
        return mPwd;
    }

    public String getPhone() {
        return mPhone;
    }

    public int getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(int annualIncome) {
        this.annualIncome = annualIncome;
    }

    public int getDesiredSaving() {
        return desiredSaving;
    }

    public void setDesiredSaving(int desiredSaving) {
        this.desiredSaving = desiredSaving;
    }

    public int getMaximumDailyExpense() {
        return maximumDailyExpense;
    }

    public void setMaximumDailyExpense(int maximumDailyExpense) {
        this.maximumDailyExpense = maximumDailyExpense;
    }
}
