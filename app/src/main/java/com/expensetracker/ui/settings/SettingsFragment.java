/**
 * Settings fragment is used to display saved expense list and also takes annual income,max daily limit and desired saving as inputs
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.DBHelper;
import com.expensetracker.Expense;
import com.expensetracker.HomeScreenActivity;
import com.expensetracker.ListOfExpensesAdapter;
import com.expensetracker.R;
import com.expensetracker.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private DBHelper mDBHelper;
    EditText mEdtDesiredSaving;
    EditText mEdtAnnualIncome;
    EditText mEdtMaximumDailyExpense;
    Button   mBtnSubmitFinances;
    RecyclerView savedExpensesRecyclerView;
    FloatingActionButton addSavedExpenseButton;
    private SharedPreferences prefs;
    String username;
    User userDetails;
    List<Expense> expenseList;
    boolean dailyExpense=false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        mDBHelper = new DBHelper(getContext());

        savedExpensesRecyclerView=root.findViewById(R.id.saved_expense_list);

        addSavedExpenseButton=root.findViewById(R.id.add_saved_expense);
        mEdtMaximumDailyExpense=root.findViewById(R.id.m_edt_maximum_daily_expense);
        mEdtAnnualIncome=root.findViewById(R.id.m_edt_annual_income);
        mEdtDesiredSaving=root.findViewById(R.id.m_edt_desired_saving);
        mBtnSubmitFinances=root.findViewById(R.id.m_btn_submit_finances);

        prefs = this.getActivity().getSharedPreferences("expensetracker", MODE_PRIVATE);

        username= prefs.getString("username", "");
        userDetails=mDBHelper.fetchUserDetails(username);

        mBtnSubmitFinances.setOnClickListener(new View.OnClickListener() {
            boolean result;
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(String.valueOf(mEdtAnnualIncome.getText()))){
                    mEdtAnnualIncome.setError("This field cannot be empty");
                }
                else if(TextUtils.isEmpty(String.valueOf(mEdtDesiredSaving.getText()))){
                    mEdtDesiredSaving.setError("This field cannot be empty");
                }
                else if(TextUtils.isEmpty(String.valueOf(mEdtMaximumDailyExpense.getText()))){
                    mEdtMaximumDailyExpense.setError("This field cannot be empty");
                }
                else {
                    int annualIncome = Integer.parseInt(String.valueOf(mEdtAnnualIncome.getText()));
                    int desiredSaving = Integer.parseInt(String.valueOf(mEdtDesiredSaving.getText()));
                    int maximumDailyExpense = Integer.parseInt(String.valueOf(mEdtMaximumDailyExpense.getText()));
                    if (annualIncome <= 0) {
                        mEdtAnnualIncome.setError("Please enter a positive number");
                    } else if (desiredSaving <= 0) {
                        mEdtDesiredSaving.setError("Please enter a positive number");
                    } else if (maximumDailyExpense < 0) {
                        mEdtMaximumDailyExpense.setError("Please enter a non - negative number");
                    } else if ((annualIncome - maximumDailyExpense * 365) <= desiredSaving) {
                        mEdtMaximumDailyExpense.setError("Adjust your maximum daily expense or savings goal");
                    } else {
                        result = mDBHelper.updateFinances(username, Integer.parseInt(String.valueOf(mEdtAnnualIncome.getText()))
                                , Integer.parseInt(String.valueOf(mEdtDesiredSaving.getText()))
                                , Integer.parseInt(String.valueOf(mEdtMaximumDailyExpense.getText())));
                        if (result)
                            Toast.makeText(getContext(), "Update is successful", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_LONG).show();
                        userDetails = mDBHelper.fetchUserDetails(username);
                        mEdtDesiredSaving.setText(String.valueOf(userDetails.getDesiredSaving()));
                        mEdtAnnualIncome.setText(String.valueOf(userDetails.getAnnualIncome()));
                        mEdtMaximumDailyExpense.setText(String.valueOf(userDetails.getMaximumDailyExpense()));
                    }
                }
            }
        });

        addSavedExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddExpenseActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEdtDesiredSaving.setText(String.valueOf(userDetails.getDesiredSaving()));
        mEdtAnnualIncome.setText(String.valueOf(userDetails.getAnnualIncome()));
        mEdtMaximumDailyExpense.setText(String.valueOf(userDetails.getMaximumDailyExpense()));
        savedExpensesRecyclerView.setAdapter(new ListOfExpensesAdapter((HomeScreenActivity) getActivity(),dailyExpense,""));
    }
}