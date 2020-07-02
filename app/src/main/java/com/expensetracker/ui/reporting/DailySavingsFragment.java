/**
 *  Daily Savings Fragment which is used for rendering bar chart of daily savings
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
import android.widget.Toast;

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
 * Use the {@link DailySavingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailySavingsFragment extends Fragment {

    private AnyChartView anyChartView;
    DBHelper mDBHelper;
    SharedPreferences sharedPreferences;
    List<ChartDataUnit> chartDataUnits;
    String fromDate,toDate;
    DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DailySavingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailySavingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailySavingsFragment newInstance(String param1, String param2) {
        DailySavingsFragment fragment = new DailySavingsFragment();
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
        View root= inflater.inflate(R.layout.fragment_daily_savings, container, false);
        final Cartesian cartesian = AnyChart.bar();
        mDBHelper = new DBHelper(getContext());
        sharedPreferences=getActivity().getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        try {
            chartDataUnits=mDBHelper.fetchDailySavingsReport(
                    dateFormat.format(new Date(0))
                    ,dateFormat.format(Calendar.getInstance().getTime())
                    ,sharedPreferences.getString("username",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<DataEntry> data = new ArrayList<>();
        for(ChartDataUnit chartDataUnit:chartDataUnits) {
            data.add(new ValueDataEntry(chartDataUnit.getDate(), chartDataUnit.getExpenseAmount()));
        }
        Bar bar = cartesian.bar(data);
        anyChartView = (AnyChartView) root.findViewById(R.id.daily_saving_chart);
        anyChartView.setChart(cartesian);
        return root;
    }
}