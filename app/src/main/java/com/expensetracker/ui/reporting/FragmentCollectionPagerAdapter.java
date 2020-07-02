/**
 * Fragment Collection Pager Adapter is used for rendering multiple fragments inside a fragment
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker.ui.reporting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.expensetracker.ui.about.AboutFragment;
import com.expensetracker.ui.help.HelpFragment;
import com.expensetracker.ui.home.HomeFragment;
import com.expensetracker.ui.updatepassword.UpdatePasswordFragment;

// Since this is an object collection, use a FragmentStatePagerAdapter,

public class FragmentCollectionPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public FragmentCollectionPagerAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                DailyExpensesFragment dailyExpensesFragment = new DailyExpensesFragment();
                return dailyExpensesFragment;
            case 1:
                DailySavingsFragment dailySavingsFragment = new DailySavingsFragment();
                return dailySavingsFragment;
            case 2:
                ItemExpensesFragment ItemExpensesFragment = new ItemExpensesFragment();
                return ItemExpensesFragment;
            default:
                return null;
        }
    }
}

