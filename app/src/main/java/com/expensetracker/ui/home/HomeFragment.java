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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.DBHelper;
import com.expensetracker.HomeScreenActivity;
import com.expensetracker.ListOfExpensesAdapter;
import com.expensetracker.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        addDailyExpenseButton=root.findViewById(R.id.add_daily_expense);
        dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        date=dateFormat.format(Calendar.getInstance().getTime());
        datePicker=root.findViewById(R.id.choose_expense_from_date);
        dailyExpenseStatus=root.findViewById(R.id.totalDailyExpense);
        dateFormatForCalender=new SimpleDateFormat("dd MMM yyyy");

        sharedPreferences=getActivity().getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        dailyExpensesRecyclerView=root.findViewById(R.id.daily_expenses_list);
        progressBar=root.findViewById(R.id.savingsProgressBar);
        addDailyExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddDailyExpenseActivity.class);
                intent.putExtra("ACTIVITY_TYPE","INSERT_DAILY_SAVED");
                startActivity(intent);

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

                String dailySum="Savings of Selected Date("+datePicked+"): "+ mDBHelper.totalSavingsofADay(username,datePicked);
                dailyExpenseStatus.setText(dailySum);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        dailyExpensesRecyclerView.setAdapter(new ListOfExpensesAdapter((HomeScreenActivity) getActivity(),dailyExpense,date));
        String dailySum="Savings of Selected Date("+date+"): "+ mDBHelper.totalSavingsofADay(username,date);
        dailyExpenseStatus.setText(dailySum);
        try {
            progressBar.setProgress(mDBHelper.savingsProgress(username));
            progressBar.setTooltipText("Savings Till date: "+String.valueOf(mDBHelper.totalSavingsTilldate(username)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}