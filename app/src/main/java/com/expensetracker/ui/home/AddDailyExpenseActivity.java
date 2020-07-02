/**
 * Add Daily Expense Activity is used for adding daily expense
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expensetracker.DBHelper;
import com.expensetracker.Expense;
import com.expensetracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDailyExpenseActivity extends AppCompatActivity {

    Spinner mDailyExpenseSpinner;
    private SharedPreferences prefs;
    private DBHelper mDBHelper;
    private EditText mDailyExpenseAmount;
    private Button mBtnAddDailyExpense;
    String username;
    ArrayAdapter<Expense> mDailyExpenseAdapter;
    List<Expense> mDailyExpenseArrayList = new ArrayList<>();
    Expense dailyExpense, expenseType;
    DateFormat dateFormat;
    String date;
    boolean result;
    String activityType;
    Expense expenseFromPrevious;
    Intent source;
    String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_daily_expenses);
        source = getIntent();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(Calendar.getInstance().getTime());
        mDBHelper = new DBHelper(this);
        prefs = getSharedPreferences("expensetracker", MODE_PRIVATE);
        username = prefs.getString("username", "");
        mDailyExpenseArrayList = mDBHelper.fetchSavedExpense(username);
        mBtnAddDailyExpense = findViewById(R.id.button_adddailyexpense);
        mDailyExpenseAmount = findViewById(R.id.text_dailyexpenseamount);
        mDailyExpenseSpinner = findViewById(R.id.spinner_dailyexpense);

        mDailyExpenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDailyExpenseArrayList);
        mDailyExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDailyExpenseSpinner.setAdapter(mDailyExpenseAdapter);
        mDailyExpenseSpinner.setPrompt("Select Expense Type");
        mDailyExpenseSpinner.setSelection(0);

        activityType = source.getStringExtra("ACTIVITY_TYPE");
        expenseFromPrevious = (Expense) source.getSerializableExtra("EXPENSE");

        mDailyExpenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = (Expense) parent.getSelectedItem();
                mDailyExpenseAmount.setText(Integer.toString(selectedExpense.getExpenseAmount()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!activityType.equals("INSERT_DAILY_SAVED")) {
            getSupportActionBar().setTitle(R.string.update_expense);
            int counter = 0;
            for (Expense expense : mDailyExpenseArrayList) {
                if (expense.getExpenseName().equals(expenseFromPrevious.getExpenseName())) {
                    mDailyExpenseSpinner.setSelection(counter);
                    mDailyExpenseSpinner.setEnabled(false);
                    mDailyExpenseAmount.setText(String.valueOf(expenseFromPrevious.getExpenseAmount()));
                    mBtnAddDailyExpense.setText("Update Expense");
                }
                counter++;
            }
        }

        mBtnAddDailyExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseType = (Expense) mDailyExpenseSpinner.getSelectedItem();
                if (!TextUtils.isEmpty(String.valueOf(mDailyExpenseAmount.getText())) && Integer.valueOf(String.valueOf(mDailyExpenseAmount.getText()))>0) {
                    if (activityType.equals("UPDATE_DAILY_EXPENSE")) {
                        expenseFromPrevious.setExpenseAmount(Integer.valueOf(String.valueOf(mDailyExpenseAmount.getText())));
                        int status = mDBHelper.updateExpense(expenseFromPrevious);
                        if (status > 0) {
                            Log.d(TAG, "Daily expense updated!!");
                            onBackPressed();
                        } else {
                            Toast.makeText(
                                    getApplicationContext()
                                    , "Expense Update failed"
                                    , Toast.LENGTH_LONG
                            ).show();
                            Log.d(TAG, "Expense Update failed");
                        }
                    } else if (activityType.equals("UPDATE_SAVED_EXPENSE")) {
                        expenseFromPrevious.setExpenseAmount(Integer.valueOf(String.valueOf(mDailyExpenseAmount.getText())));
                        int status = mDBHelper.updateSavedExpense(expenseFromPrevious);
                        if (status > 0) {
                            Log.d(TAG, "Daily expense updated!!");
                            onBackPressed();
                        } else {
                            Toast.makeText(
                                    getApplicationContext()
                                    , "Expense Update failed"
                                    , Toast.LENGTH_LONG
                            ).show();
                            Log.d(TAG, "Expense Update failed");
                        }
                    } else {
                        dailyExpense = new Expense();
                        dailyExpense.setSavedExpenseID(expenseType.getSavedExpenseID());
                        dailyExpense.setExpenseAmount(Integer.valueOf(String.valueOf(mDailyExpenseAmount.getText())));
                        dailyExpense.setCreatedDate(date);
                        result = mDBHelper.addDailyExpense(dailyExpense);
                        if (result) {
                            Log.d(TAG, "Daily expense added!!");
                            onBackPressed();
                        } else {
                            Toast.makeText(
                                    getApplicationContext()
                                    , "Expense addition failed"
                                    , Toast.LENGTH_LONG
                            ).show();
                            Log.d(TAG, "Expense addition failed");
                        }
                    }
                }
                else{
                    mDailyExpenseAmount.setError("Cannot be blank or non-positive number");
                }
            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate activity menu items.
        if (activityType.equals("UPDATE_SAVED_EXPENSE") || activityType.equals("UPDATE_DAILY_EXPENSE")) {
            getMenuInflater().inflate(R.menu.fragment_menu_items, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        boolean showMessage = false;

        String message = "";

        if (itemId == R.id.fragment_menu_delete) {
            if (activityType.equals("UPDATE_SAVED_EXPENSE")) {
                if (mDBHelper.deleteSavedExpense(expenseFromPrevious.getSavedExpenseID())) {
                    onBackPressed();
                } else {
                    message = "Make sure you don't have any daily items with this saved expense type";
                    showMessage=true;
                }
            } else if (activityType.equals("UPDATE_DAILY_EXPENSE")) {
                if (mDBHelper.deleteDailyExpense(expenseFromPrevious.getDailyExpenseID())) {
                    onBackPressed();
                } else {
                    message = "Unable to delete the daily expense item";
                    showMessage=true;
                }
            }

        }

        if(showMessage) {
            AlertDialog alertDialog = new AlertDialog.Builder(AddDailyExpenseActivity.this).create();
            alertDialog.setMessage(message);
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}