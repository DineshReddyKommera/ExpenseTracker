/**
 *  Daily Expenses Fragment which is used for rendering bar chart of daily expense for given date range
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker.ui.reporting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Bar;
import com.expensetracker.ChartDataUnit;
import com.expensetracker.DBHelper;
import com.expensetracker.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyExpensesFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button mSelectDate;
    private AnyChartView anyChartView;
    DBHelper mDBHelper;
    SharedPreferences sharedPreferences;
    List<ChartDataUnit> chartDataUnits;
    MaterialDatePicker picker;
    String fromDate,toDate;
    Bar bar;
    DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
    List<DataEntry> customData;
    public DailyExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyExpensesFragment.
     */
    public static DailyExpensesFragment newInstance(String param1, String param2) {
        DailyExpensesFragment fragment = new DailyExpensesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View root  = inflater.inflate(R.layout.fragment_daily_expenses, container, false);
        mSelectDate = root.findViewById(R.id.button_select_date);
        final Cartesian cartesian = AnyChart.bar();
        mDBHelper = new DBHelper(getContext());
        sharedPreferences=getActivity().getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
        try {
            chartDataUnits=mDBHelper.fetchDailyExpenseReport(
                    dateFormat.format(new Date(0))
                    ,dateFormat.format(Calendar.getInstance().getTime())
                    ,sharedPreferences.getString("username",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final List<DataEntry> data = new ArrayList<>();
        for(ChartDataUnit chartDataUnit:chartDataUnits) {
            data.add(new ValueDataEntry(chartDataUnit.getDate(), chartDataUnit.getExpenseAmount()));
        }
        bar = cartesian.bar(data);
        anyChartView.setChart(cartesian);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        picker = builder.build();
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());
            }
        });

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                customData = new ArrayList<>();
                Pair<Long, Long> t = (Pair<Long, Long>) picker.getSelection();
                fromDate= dateFormat.format(new Date(t.first));
                toDate= dateFormat.format(new Date(t.second));
                try {
                    chartDataUnits=mDBHelper.fetchDailyExpenseReport(fromDate,toDate,sharedPreferences.getString("username",""));
                    for(ChartDataUnit chartDataUnit:chartDataUnits) {
                        customData.add(new ValueDataEntry(chartDataUnit.getDate(), chartDataUnit.getExpenseAmount()));
                    }
                    APIlib.getInstance().setActiveAnyChartView(anyChartView);
                    if(customData.size()==0)
                        anyChartView.setVisibility(View.GONE);
                    else
                        anyChartView.setVisibility(View.VISIBLE);
                    bar.data(customData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

}