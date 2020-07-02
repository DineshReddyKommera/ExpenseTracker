/**
 * Add Expense Activity is used for adding expense types
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expensetracker.DBHelper;
import com.expensetracker.Expense;
import com.expensetracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private DBHelper mDBHelper;
    private EditText mExpenseName, mExpenseAmount;
    private Button mBtnAddExpense;
    SharedPreferences sharedPreferences;
    String username;
    Expense expense;
    DateFormat dateFormat;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_expenses);
        sharedPreferences=getSharedPreferences("expensetracker",MODE_PRIVATE);
        dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        date= dateFormat.format(Calendar.getInstance().getTime());
        expense=new Expense();
        mDBHelper = new DBHelper(this);
        prefs = getSharedPreferences("expensetracker", MODE_PRIVATE);
        username = prefs.getString("username", "");
        mBtnAddExpense = findViewById(R.id.button_addexpense);
        mExpenseName = findViewById(R.id.text_expensetitle);
        mExpenseAmount = findViewById(R.id.text_expenseamount);
        mBtnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(String.valueOf(String.valueOf(mExpenseName.getText())))){
                    mExpenseName.setError("This field cannot be empty");
                }
                else {
                    expense.setCreatedDate(date);
                    if(!TextUtils.isEmpty(String.valueOf(mExpenseAmount.getText())))
                        expense.setExpenseAmount(Integer.valueOf(String.valueOf(mExpenseAmount.getText())));
                    expense.setUsername(username);
                    expense.setExpenseName(String.valueOf(mExpenseName.getText()));
                    boolean result = mDBHelper.addSavedExpense(expense);
                    if (result)
                        onBackPressed();
                    else
                        Toast.makeText(getApplicationContext()
                                , "Data insertion failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}