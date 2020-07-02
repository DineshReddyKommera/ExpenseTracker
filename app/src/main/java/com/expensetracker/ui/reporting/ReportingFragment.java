/**
 * Reporting fragment is used to provide different types of reports
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */

package com.expensetracker.ui.reporting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.expensetracker.R;
import com.google.android.material.tabs.TabLayout;

public class ReportingFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reporting, container, false);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Daily Expenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Daily Savings"));
        tabLayout.addTab(tabLayout.newTab().setText("Item Expenses"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        final ViewPager viewPager =(ViewPager)root.findViewById(R.id.view_pager);
        FragmentCollectionPagerAdapter tabsAdapter = new FragmentCollectionPagerAdapter(this.getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }
}