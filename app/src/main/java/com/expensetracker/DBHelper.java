/**
 * A SQLite Helper class for performing CRUD operations
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */

package com.expensetracker;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Expensetracker.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PWD = "password";
    public static final String USERS_COLUMN_PHONE = "phone";
    public static final String USERS_COLUMN_ANNUAL_INCOME="annual_income";
    public static final String USERS_COLUMN_DESIRED_SAVING="desired_saving";
    public static final String USERS_COLUMN_MAXIMUM_DAILY_EXPENSE="maximum_daily_expense";

    public static final String SAVED_EXPENSES_TABLE_NAME="saved_expenses";
    public static final String SAVED_EXPENSES_COLUMN_EXPENSE_ID="saved_expense_id";
    public static final String SAVED_EXPENSES_COLUMN_EXPENSE_NAME="expense_name";
    public static final String SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT="expense_amount";
    public static final String SAVED_EXPENSES_COLUMN_USER="username";
    public static final String SAVED_EXPENSES_COLUMN_CREATED_DATE ="created_date";

    public static final String DAILY_EXPENSES_TABLE_NAME ="daily_expenses";
    public static final String DAILY_EXPENSES_COLUMN_EXPENSE_ID="daily_expense_id";
    public static final String DAILY_EXPENSES_COLUMN_CREATED_DATE="created_date";
    public static final String DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID="saved_expense_id";
    public static final String DAILY_EXPENSES_COLUMN_AMOUNT="expense_amount";
    DateFormat dateFormat;
    DateFormat dateComparisionFormat;

    private static final String CREATE_TABLE_SAVED_EXPENSES="CREATE TABLE "
            + SAVED_EXPENSES_TABLE_NAME + "(" + SAVED_EXPENSES_COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + SAVED_EXPENSES_COLUMN_EXPENSE_NAME  + " TEXT ,"
            + SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT + " INTEGER, "
            + SAVED_EXPENSES_COLUMN_USER + " TEXT, "
            + SAVED_EXPENSES_COLUMN_CREATED_DATE + " TEXT, "
            + " FOREIGN KEY ("+SAVED_EXPENSES_COLUMN_USER+") REFERENCES "+USERS_TABLE_NAME+"("+USERS_COLUMN_USERNAME+"))";


    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + USERS_TABLE_NAME + "(" + USERS_COLUMN_USERNAME + " TEXT PRIMARY KEY ,"
            + USERS_COLUMN_EMAIL + " TEXT,"
            + USERS_COLUMN_PWD + " TEXT,"
            + USERS_COLUMN_PHONE + " TEXT,"
            + USERS_COLUMN_ANNUAL_INCOME + " INTEGER, "
            + USERS_COLUMN_MAXIMUM_DAILY_EXPENSE + " INTEGER , "
            + USERS_COLUMN_DESIRED_SAVING + " INTEGER)";

    private static final String CREATE_TABLE_DAILY_EXPENSES = "CREATE TABLE "
            + DAILY_EXPENSES_TABLE_NAME + "(" + DAILY_EXPENSES_COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + DAILY_EXPENSES_COLUMN_CREATED_DATE  + " TEXT ,"
            + DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID + " INTEGER, "
            + DAILY_EXPENSES_COLUMN_AMOUNT + " INTEGER,"
            + " FOREIGN KEY ("+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID+") " +
              " REFERENCES "+SAVED_EXPENSES_TABLE_NAME+" ( "+SAVED_EXPENSES_COLUMN_EXPENSE_ID+"))";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        dateComparisionFormat=new SimpleDateFormat("yyyyMMdd");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_SAVED_EXPENSES);
        db.execSQL(CREATE_TABLE_DAILY_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_EXPENSES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SAVED_EXPENSES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }


    /*
     * Creating a user
     */
    public Boolean createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_USERNAME, user.getUsername());
        values.put(USERS_COLUMN_EMAIL, user.getEmail());
        values.put(USERS_COLUMN_PWD, user.getPwd());
        values.put(USERS_COLUMN_PHONE, user.getPhone());

        // insert row
        long status = db.insert(USERS_TABLE_NAME, null, values);
        if(status!=-1)
            return true;
        else
            return false;

    }

    /*
     * Checking if user already exists
     */
    public Boolean getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME + " WHERE "
                + USERS_COLUMN_USERNAME + " = '" + username + "'";


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        else
            return false;
    }
    /*
     * Validate User
     */
    public Boolean validateUser(String username,String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME + " WHERE "
                + USERS_COLUMN_USERNAME + " = '" + username + "'" + "AND " + USERS_COLUMN_PWD + " = '" + password + "'" ;


        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            if (c.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }
        else
            return false;
    }

    /**
     * Change @param previousPassword to @param newPassword of
     * @param username
     * @return true if password has been changed and vice versa
     */
    public boolean changePassword(String username,String previousPassword, String newPassword){
        int result=0;
        SQLiteDatabase db;
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(USERS_COLUMN_PWD,newPassword);

        result=db.update(USERS_TABLE_NAME,contentValues,USERS_COLUMN_USERNAME+"='"+username+"'",null);

        return result==1;
    }

    /**
     * Change @param previousEmail to @param newEmail of
     * @param username
     * @return true if email has been changed and vice versa
     */
    public boolean changeEmail(String username,String newEmail){
        int result=0;
        SQLiteDatabase db;
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(USERS_COLUMN_EMAIL,newEmail);

        result=db.update(USERS_TABLE_NAME,contentValues,USERS_COLUMN_USERNAME+"='"+username+"'",null);
        return result==1;
    }

    /**
     * Updating
     * @param annualIncome and @param desiredSaving
     * of @param username
     * @return true if successful and vice versa
     */
    public boolean updateFinances(String username, int annualIncome, int desiredSaving, int maximumDailyExpense){
        int result=0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(USERS_COLUMN_ANNUAL_INCOME,annualIncome);
        contentValues.put(USERS_COLUMN_DESIRED_SAVING,desiredSaving);
        contentValues.put(USERS_COLUMN_MAXIMUM_DAILY_EXPENSE,maximumDailyExpense);

        result=sqLiteDatabase.update(USERS_TABLE_NAME,contentValues,USERS_COLUMN_USERNAME+"='"+username+"'",null);


        return result==1;
    }

    /**
     * Adding saved
     * @param expense of the users
     * @return true if expense is added, false if expense could not be added/
     */
    public boolean addSavedExpense(Expense expense){
        boolean result;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SAVED_EXPENSES_COLUMN_EXPENSE_NAME, expense.getExpenseName());
        values.put(SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT, expense.getExpenseAmount());
        values.put(SAVED_EXPENSES_COLUMN_USER, expense.getUsername());
        values.put(SAVED_EXPENSES_COLUMN_CREATED_DATE, expense.getCreatedDate());

        // insert row
        long status = sqLiteDatabase.insert(SAVED_EXPENSES_TABLE_NAME, null, values);

        return status != -1;
    }

    /**
     * Adding daily
     * @param dailyExpense of the user
     * @return true if expense is added, false if expense could not be added/
     */
    public boolean addDailyExpense(Expense dailyExpense){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAILY_EXPENSES_COLUMN_AMOUNT, dailyExpense.getExpenseAmount());
        values.put(DAILY_EXPENSES_COLUMN_CREATED_DATE,dailyExpense.getCreatedDate());
        values.put(DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID,dailyExpense.getSavedExpenseID());
        // insert row
        long status = sqLiteDatabase.insert(DAILY_EXPENSES_TABLE_NAME, null, values);

        return status != -1;
    }

    /**
     * removes expense with
     * @param dailyExpenseId
     * @return true if expense got deleted, false if expense could not be deleted
     */
    public boolean deleteDailyExpense(int dailyExpenseId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
        return sqLiteDatabase.delete(DAILY_EXPENSES_TABLE_NAME,DAILY_EXPENSES_COLUMN_EXPENSE_ID+" = ?",new String[]{Integer.toString(dailyExpenseId)})>0;
    }

    /**
     * removes expense with
     * @param savedExpenseId
     * @return true if expense got deleted, false if expense could not be deleted
     */
    public boolean deleteSavedExpense(int savedExpenseId){
        Boolean result = false;
        try
        {
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
            result = sqLiteDatabase.delete(SAVED_EXPENSES_TABLE_NAME,SAVED_EXPENSES_COLUMN_EXPENSE_ID+" = ?",new String[]{Integer.toString(savedExpenseId)})>0;
        }
        catch (SQLException ex){
            result=false;
        }
        return result;
    }

    /**
     * updates DAILY_EXPENSES Table with
     * @param dailyExpenses
     * @return number of rows affected
     */
    public int updateExpense(Expense dailyExpenses){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        int result=1;
        final String updateDailyExpenses="UPDATE " + DAILY_EXPENSES_TABLE_NAME + " SET "
                + DAILY_EXPENSES_COLUMN_AMOUNT + " = " + dailyExpenses.getExpenseAmount()
                + " WHERE " + DAILY_EXPENSES_COLUMN_EXPENSE_ID + " = " + dailyExpenses.getDailyExpenseID();
        Cursor c=sqLiteDatabase.rawQuery(updateDailyExpenses,null);
        if (c != null) {
            if (c.getCount() > 0) {
                result=c.getCount();
            }
        }
        return result;
    }

    /**
     * updates SAVED_EXPENSE Table with
     * @param savedExpense
     * @return number of rows affected
     */
    public int updateSavedExpense(Expense savedExpense){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT,savedExpense.getExpenseAmount());
        contentValues.put(SAVED_EXPENSES_COLUMN_EXPENSE_NAME,savedExpense.getExpenseName());
        return sqLiteDatabase.update(SAVED_EXPENSES_TABLE_NAME,contentValues,
                SAVED_EXPENSES_COLUMN_EXPENSE_ID+" = "+savedExpense.getSavedExpenseID(),
                null);
    }

    /**
     * Takes @param username as input and
     * @return list of saved expenses of that user
     */
    public List<Expense> fetchSavedExpense(String username){
        List<Expense> savedExpenseList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        final String selectSavedExpense=" SELECT * FROM " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_COLUMN_USER
                + " = '"+ username +"'";

        Cursor cursor=sqLiteDatabase.rawQuery(selectSavedExpense,null);

        if (cursor.moveToFirst()){
            do{
                Expense savedExpense= new Expense();
                savedExpense.setExpenseName(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_NAME)));
                savedExpense.setUsername(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_USER)));
                savedExpense.setSavedExpenseID(cursor.getInt(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_ID)));
                savedExpense.setCreatedDate(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_CREATED_DATE)));
                savedExpense.setExpenseAmount(cursor.getInt(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_AMOUNT)));
                savedExpenseList.add(savedExpense);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return savedExpenseList;
    }

    /**
     * Fetched all expenses associated with
     * @param username created on @param date
     * @return
     */
    public List<Expense> fetchDailyExpense(String username,String date){
        /*select daily_expenses.daily_expense_id
        ,daily_expenses.expense_amount
        ,daily_expenses.created_date
        ,saved_expenses.expense_name
        from saved_expenses,daily_expenses
        where daily_expenses.saved_expense_id=saved_expenses.saved_expense_id
        and saved_expenses.username=username*/
        List<Expense> dailyExpensesList= new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        final String selectExpense="SELECT "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_EXPENSE_ID+" , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+" , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+" , "
                + SAVED_EXPENSES_TABLE_NAME+"."+SAVED_EXPENSES_COLUMN_EXPENSE_NAME+" , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " AND " + DAILY_EXPENSES_TABLE_NAME+ "."+DAILY_EXPENSES_COLUMN_CREATED_DATE + " = '" + date+"'";


        Cursor cursor=sqLiteDatabase.rawQuery(selectExpense,null);
        if (cursor.moveToFirst()){
            do{
                Expense dailyExpenses=new Expense();
                dailyExpenses.setDailyExpenseID(cursor.getInt(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_EXPENSE_ID)));
                dailyExpenses.setSavedExpenseID(cursor.getInt(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID)));
                dailyExpenses.setCreatedDate(cursor.getString(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_CREATED_DATE)));
                dailyExpenses.setExpenseAmount(cursor.getInt(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_AMOUNT)));
                dailyExpenses.setExpenseName(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_NAME)));
                dailyExpensesList.add(dailyExpenses);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return dailyExpensesList;
    }

    /**
     * Fetched all expenses associated with
     * @param username that are added today
     * @return
     */
    public List<Expense> fetchDailyExpense(String username) {
        String today= dateFormat.format(Calendar.getInstance().getTime());
        return fetchDailyExpense(username,today);
    }

    public User fetchUserDetails(String username){
        User user=new User();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        final String selectUser="SELECT "
                + USERS_COLUMN_DESIRED_SAVING +" , "
                + USERS_COLUMN_ANNUAL_INCOME  +" , "
                + USERS_COLUMN_EMAIL +" , "
                +USERS_COLUMN_MAXIMUM_DAILY_EXPENSE
                +" FROM "+USERS_TABLE_NAME
                +" WHERE "
                + USERS_COLUMN_USERNAME + " = '" + username + "'";

        Cursor cursor=sqLiteDatabase.rawQuery(selectUser,null);
        if (cursor.moveToFirst()){
                user.setAnnualIncome(cursor.getInt(cursor.getColumnIndex(USERS_COLUMN_ANNUAL_INCOME)));
                user.setDesiredSaving(cursor.getInt(cursor.getColumnIndex(USERS_COLUMN_DESIRED_SAVING)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_EMAIL)));
                user.setMaximumDailyExpense(cursor.getInt(cursor.getColumnIndex(USERS_COLUMN_MAXIMUM_DAILY_EXPENSE)));
        }
        cursor.close();
        return user;
    }

    /**
     * Fetches data of amount each item contribute to total expense between range
     * @param fromDate
     * @param toDate
     * @param username
     * @return
     * @throws ParseException
     */
    public List<ChartDataUnit> fetchItemizedReport(String fromDate, String toDate, String username) throws ParseException {
        /*
        select sum(daily_expenses.expense_amount),saved_expenses.expense_name
        from saved_expenses,daily_expenses
        where daily_expenses.saved_expense_id=saved_expenses.saved_expense_id
        and saved_expenses.username='abhi'
        group by daily_expenses.saved_expense_id
         having substr(daily_expenses.created_date,7)||substr(daily_expenses.created_date,1,2)||substr(daily_expenses.created_date,4,2)
           between '20202606' and '20202806'
         */

        List<ChartDataUnit> chartDataUnits=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String fromDateFormatted=dateComparisionFormat.format(dateFormat.parse(fromDate));
        String toDateFormatted=dateComparisionFormat.format(dateFormat.parse(toDate));
        ChartDataUnit chartDataUnit;
        final String fetchChartData="SELECT "
                + "SUM("+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+") , "
                + SAVED_EXPENSES_TABLE_NAME+"."+SAVED_EXPENSES_COLUMN_EXPENSE_NAME
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " +DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " GROUP BY " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " HAVING" + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",7)||"
                + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",4,2)||"
                + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",1,2) "
                + " BETWEEN "
                + "'"+fromDateFormatted+ "'"
                + " AND "
                + "'"+toDateFormatted+ "'";
        Cursor cursor=sqLiteDatabase.rawQuery(fetchChartData,null);
        if (cursor.moveToFirst()){
            do{
            chartDataUnit=new ChartDataUnit();
            chartDataUnit.setExpenseName(cursor.getString(cursor.getColumnIndex(SAVED_EXPENSES_COLUMN_EXPENSE_NAME)));
            chartDataUnit.setExpenseAmount(cursor.getInt(0));
            chartDataUnits.add(chartDataUnit);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return chartDataUnits;
    }

    /**
     * Fetches details of daily savings between selected range
     * @param fromDate
     * @param toDate
     * @param username
     * @return
     * @throws ParseException
     */
    public List<ChartDataUnit> fetchDailySavingsReport(String fromDate,String toDate,String username) throws ParseException {
        List<ChartDataUnit> chartDataUnits=new ArrayList<>();
        String fromDateFormatted=dateComparisionFormat.format(dateFormat.parse(fromDate));
        String toDateFormatted=dateComparisionFormat.format(dateFormat.parse(toDate));
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ChartDataUnit chartDataUnit;

        final String fetchChartData="SELECT "
                + "("+USERS_TABLE_NAME+"."+USERS_COLUMN_ANNUAL_INCOME+"/365)- SUM("+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+") , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " , " + USERS_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " AND " + USERS_TABLE_NAME+"."+USERS_COLUMN_USERNAME
                + " = " + SAVED_EXPENSES_TABLE_NAME+"."+SAVED_EXPENSES_COLUMN_USER
                + " GROUP BY " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE
                + " HAVING" + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",7)||"
                + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",4,2)||"
                + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",1,2) "
                + " BETWEEN "
                + "'"+fromDateFormatted+ "'"
                + " AND "
                + "'"+toDateFormatted+ "'";
        Cursor cursor=sqLiteDatabase.rawQuery(fetchChartData,null);
        if (cursor.moveToFirst()){
            do{
                chartDataUnit=new ChartDataUnit();
                chartDataUnit.setDate(cursor.getString(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_CREATED_DATE)));
                chartDataUnit.setExpenseAmount(cursor.getInt(0));
                chartDataUnits.add(chartDataUnit);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return chartDataUnits;
    }

    /**
     * Fetches data of daily expenses of user between selected date range
     * @param fromDate
     * @param toDate
     * @param username
     * @return
     * @throws ParseException
     */
    public List<ChartDataUnit> fetchDailyExpenseReport(String fromDate,String toDate,String username) throws ParseException {
        List<ChartDataUnit> chartDataUnits=new ArrayList<>();
        String fromDateFormatted=dateComparisionFormat.format(dateFormat.parse(fromDate));
        String toDateFormatted=dateComparisionFormat.format(dateFormat.parse(toDate));
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ChartDataUnit chartDataUnit;

        final String fetchChartData="SELECT "
                + "SUM("+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+") , "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " +DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " GROUP BY " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE
                + " HAVING" + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",7)||"
                + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",4,2)||"
                + " SUBSTR("+DAILY_EXPENSES_TABLE_NAME +"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+",1,2) "
                + " BETWEEN "
                + "'"+fromDateFormatted+ "'"
                + " AND "
                + "'"+toDateFormatted+ "'";
        Cursor cursor=sqLiteDatabase.rawQuery(fetchChartData,null);
        if (cursor.moveToFirst()){
            do{
                chartDataUnit=new ChartDataUnit();
                chartDataUnit.setDate(cursor.getString(cursor.getColumnIndex(DAILY_EXPENSES_COLUMN_CREATED_DATE)));
                chartDataUnit.setExpenseAmount(cursor.getInt(0));
                chartDataUnits.add(chartDataUnit);
            }while(cursor.moveToNext());

        }
        cursor.close();
        return chartDataUnits;
    }

    /**
     * This method returns total savings of selected input parameter date
     * @param username
     * @param date
     * @return
     */
    public int totalSavingsofADay(String username, String date){
        int sum=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
//        if(totalDailyExpenses(username,date)==0)
//            return fetchUserDetails(username).getAnnualIncome()/365;

        final String calculateSum="SELECT "
                + "("+USERS_TABLE_NAME+"."+USERS_COLUMN_ANNUAL_INCOME+"/365)- SUM("+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+")"
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " , " + USERS_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " AND " + SAVED_EXPENSES_TABLE_NAME + "." + SAVED_EXPENSES_COLUMN_USER
                + " = " + USERS_TABLE_NAME+ "." + USERS_COLUMN_USERNAME
                + " AND " + DAILY_EXPENSES_TABLE_NAME+ "."+DAILY_EXPENSES_COLUMN_CREATED_DATE + " = '" + date+"'";
        Cursor cursor=sqLiteDatabase.rawQuery(calculateSum,null);
        if(cursor.moveToFirst()){
            sum=cursor.getInt(0);
        }
        return sum;
    }

    /**
     * This method gives the percentage of savings till date
     * @param username
     * @return
     * @throws ParseException
     */
    public int savingsProgress(String username) throws ParseException {
        int sum=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        int count=noOfDaysSinceGoalStarted(username);
        if(count==0){
            return 0;
        }

        final String calculateSum=" SELECT 100*("
                + totalSavingsTilldate(username)
                +")/"
                +USERS_TABLE_NAME+"."+USERS_COLUMN_DESIRED_SAVING
                + " FROM " +USERS_TABLE_NAME
                + " WHERE " + USERS_TABLE_NAME+ "." +USERS_COLUMN_USERNAME+ " = '" + username +"'";
        Cursor cursor=sqLiteDatabase.rawQuery(calculateSum,null);
        if(cursor.moveToFirst()){
            sum=cursor.getInt(0);
        }
        return sum;
    }

    /**
     * This method gives the total number of days since goal started
     * @param username
     * @return
     * @throws ParseException
     */
    public int noOfDaysSinceGoalStarted(String username) throws ParseException {
        String startDate="";
        int count=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        final String calculateSum="SELECT "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " +DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " ORDER BY " + DAILY_EXPENSES_COLUMN_EXPENSE_ID;

        Cursor cursor=sqLiteDatabase.rawQuery(calculateSum,null);
        if(cursor.moveToFirst()){
            startDate=cursor.getString(0);
            count= Period.between(dateFormat.parse(startDate).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(),LocalDate.now()).getDays();
        }

        return count+1;
    }

    /**
     * This method returns sum of expenses of a user till date
     * @param username
     * @return
     */
    public int totalExpensesTilldate(String username){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        int totalExpensesSum=0;
        final String calculateTotalExpensesSum="SELECT "
                + " SUM("+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_AMOUNT+")"
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID;

        Cursor cursor=sqLiteDatabase.rawQuery(calculateTotalExpensesSum,null);
        if(cursor.moveToFirst()){
            totalExpensesSum=cursor.getInt(0);
        }
        return totalExpensesSum;
    }

    /**
     * This method gives sum of income of a user till date
     * @param username
     * @return
     * @throws ParseException
     */
    public int totalIncomeTillDate(String username) throws ParseException {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        int totalTargetSum=0;
        int count=noOfDaysSinceGoalStarted(username);
        if(count==0){
            return 0;
        }
        final String calculateTotalExpensesSum="SELECT "
                + count+"*("+USERS_TABLE_NAME+"."+USERS_COLUMN_ANNUAL_INCOME+"/365)"
                + " FROM "+ USERS_TABLE_NAME
                + " WHERE " + USERS_TABLE_NAME+ "." +USERS_COLUMN_USERNAME+ " = '" + username +"'";

        Cursor cursor=sqLiteDatabase.rawQuery(calculateTotalExpensesSum,null);
        if(cursor.moveToFirst()){
            totalTargetSum=cursor.getInt(0);
        }
        return totalTargetSum;
    }

    public int totalSavingsTilldate(String username) throws ParseException {
        return totalIncomeTillDate(username)-totalExpensesTilldate(username);
    }

    public int deviationFromCumulativeTarget(String username) throws ParseException {
        int deviation=0;
        int noOfdaysSinceGoalStart=noOfDaysSinceGoalStarted(username);
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String fetchDeviation=" SELECT "
                +noOfdaysSinceGoalStart+"*("+USERS_TABLE_NAME+"."+USERS_COLUMN_MAXIMUM_DAILY_EXPENSE+")-"+totalExpensesTilldate(username)
                + " FROM "+ USERS_TABLE_NAME
                + " WHERE " + USERS_TABLE_NAME+ "." +USERS_COLUMN_USERNAME+ " = '" + username +"'";
        Cursor cursor=sqLiteDatabase.rawQuery(fetchDeviation,null);
        if(cursor.moveToFirst()){
            deviation=cursor.getInt(0);
        }
        return deviation;
    }

    public int totalnoOfSavedExpenses(String username){
        int count=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String fetchSavedExpensesCount=" SELECT "
                +"COUNT("+SAVED_EXPENSES_COLUMN_EXPENSE_ID+")"
                + " FROM "+ SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'";
        Cursor cursor=sqLiteDatabase.rawQuery(fetchSavedExpensesCount,null);
        if(cursor.moveToFirst()){
            count=cursor.getInt(0);
        }
        return count;
    }

    public int totalDailyExpenses(String username, String date){
        int count=0;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String fetchDailyExpensesCount=" SELECT "
                +" COUNT("+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_EXPENSE_ID+")"
                +" FROM " + DAILY_EXPENSES_TABLE_NAME
                +" , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE "+ SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " AND " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND "+DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE+"='"+date+"'";

        Cursor cursor=sqLiteDatabase.rawQuery(fetchDailyExpensesCount,null);
        if(cursor.moveToFirst())
        {
            count=cursor.getInt(0);
        }
        return count;
    }

    public String goalStartDate(String username){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String startDate="";
        final String goalStartDay="SELECT "
                + DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_CREATED_DATE
                + " FROM "+ DAILY_EXPENSES_TABLE_NAME
                + " , " + SAVED_EXPENSES_TABLE_NAME
                + " WHERE " + SAVED_EXPENSES_TABLE_NAME+ "." +SAVED_EXPENSES_COLUMN_USER+ " = '" + username +"'"
                + " AND " + SAVED_EXPENSES_TABLE_NAME +"."+SAVED_EXPENSES_COLUMN_EXPENSE_ID
                + " = " +DAILY_EXPENSES_TABLE_NAME+"."+DAILY_EXPENSES_COLUMN_SAVED_EXPENSE_ID
                + " ORDER BY " + DAILY_EXPENSES_COLUMN_EXPENSE_ID;

        Cursor cursor=sqLiteDatabase.rawQuery(goalStartDay,null);
        if(cursor.moveToFirst()) {
            startDate = cursor.getString(0);
        }
        return startDate;
    }
}