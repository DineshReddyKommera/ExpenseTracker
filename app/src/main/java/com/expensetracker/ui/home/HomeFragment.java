/**
 * Home Fragment which displays saved preferences and user daily expenses
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */

package com.expensetracker.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.DBHelper;
import com.expensetracker.HomeScreenActivity;
import com.expensetracker.ListOfExpensesAdapter;
import com.expensetracker.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class extends Fragment
 * is home screen of the application
 * where user can check his total savings through progress bar
 * cumulative savings status
 * and add new daily savings
 */
public class HomeFragment extends Fragment{

    String date;
    DateFormat dateFormat;
    FloatingActionButton addDailyExpenseButton;
    RecyclerView dailyExpensesRecyclerView;
    boolean dailyExpense=true;
    ImageView datePicker;
    DateFormat dateFormatForCalender;
    TextView dailyExpenseStatus;
    SharedPreferences sharedPreferences;
    MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
    MaterialDatePicker picker;
    DBHelper mDBHelper;
    ProgressBar progressBar;
    String username;
    CalendarConstraints.Builder constraintsBuilder;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        addDailyExpenseButton=root.findViewById(R.id.add_daily_expense);
        dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        date=dateFormat.format(Calendar.getInstance().getTime());
        datePicker=root.findViewById(R.id.choose_expense_from_date);
        dailyExpenseStatus=root.findViewById(R.id.totalDailyExpense);
        dateFormatForCalender=new SimpleDateFormat("MMM dd, yyyy");

        sharedPreferences=getActivity().getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        dailyExpensesRecyclerView=root.findViewById(R.id.daily_expenses_list);
        progressBar=root.findViewById(R.id.savingsProgressBar);
        addDailyExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mDBHelper.totalnoOfSavedExpenses(username)==0) {
                    Toast.makeText(getContext(),"Add minimum of one saved expense in your settings",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(getActivity(), AddDailyExpenseActivity.class);
                    intent.putExtra("ACTIVITY_TYPE","INSERT_DAILY_SAVED");
                    startActivity(intent);
                }

            }
        });
        mDBHelper=new DBHelper(getContext());
        picker=builder.build();
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());
            }
        });
        try {
            progressBar.setProgress(mDBHelper.savingsProgress(username));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                String datePicked="";
                try {
                    datePicked=dateFormat.format(dateFormatForCalender.parse(picker.getHeaderText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dailyExpensesRecyclerView.setAdapter(new ListOfExpensesAdapter((HomeScreenActivity) getActivity()
                        ,dailyExpense,datePicked));

                String dailySum="";
                if(mDBHelper.totalSavingsofADay(username,datePicked)!=0)
                    dailySum="Savings of Selected Date("+date+"): "+ mDBHelper.totalSavingsofADay(username,date)+"\n";
                int deviation=0;
                try {
                    deviation=mDBHelper.deviationFromCumulativeTarget(username);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(deviation>0){
                    dailySum+="\nYou are ahead of your cumulative savings target by "+deviation +". Hurray!!"+"\n";
                }
                else if(deviation==0){
                    dailySum+="\nBulls eye!! You have exactly reached your cumulative savings target"+"\n";;
                }
                else{
                    dailySum+="\nYou have backlog of target savings by "+deviation+ ".\nPlease adjust your expenses accordingly"+"\n";;
                }

                dailyExpenseStatus.setText(dailySum);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        int deviation=0;
        try {
            deviation=mDBHelper.deviationFromCumulativeTarget(username);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dailyExpensesRecyclerView.setAdapter(new ListOfExpensesAdapter((HomeScreenActivity) getActivity(),dailyExpense,date));

        String dailySum="";
                if(mDBHelper.totalSavingsofADay(username,date)!=0)
                    dailySum="Savings of Selected Date("+date+"): "+ mDBHelper.totalSavingsofADay(username,date)+"\n";
        if(deviation>0){
            dailySum+="\nYou are ahead of your cumulative savings target by "+deviation +". Hurray!!"+"\n";
        }
        else if(deviation==0){
            dailySum+="\nBulls eye!! You have exactly reached your cumulative savings target"+"\n";;
        }
        else{
            dailySum+="\nYou have backlog of target savings by "+deviation+ ".\nPlease adjust your expenses accordingly"+"\n";;
        }
        dailyExpenseStatus.setText(dailySum);
        try {
            progressBar.setProgress(mDBHelper.savingsProgress(username));
            progressBar.setTooltipText("Savings Till date: "+String.valueOf(mDBHelper.totalSavingsTilldate(username)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(mDBHelper.totalExpensesTilldate(username)==0){
            dailyExpenseStatus.setText("It is idle here add daily expenses by tapping + at bottom of screen");
        }
    }

}