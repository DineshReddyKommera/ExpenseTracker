/**
 * Home Screen Activity which is triggered on successful login
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */

package com.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeScreenActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Toolbar mToolbar;
    FloatingActionButton mFloatingActionButton;
    DrawerLayout mDrawerLayoutDrawer;
    NavigationView mNavigationView;
    View mHeaderView;
    NavController mNavController;
    SharedPreferences preferences;
    TextView mTextUser;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        preferences = getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        mDrawerLayoutDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_addexpenses,R.id.nav_reporting, R.id.nav_help, R.id.nav_about,R.id.nav_updatepassword,R.id.nav_updateemail)
                .setDrawerLayout(mDrawerLayoutDrawer)
                .build();
        //Setting user name for Navigation Header
        mHeaderView = mNavigationView.getHeaderView(0);
        mTextUser = mHeaderView.findViewById(R.id.text_user);
        username= preferences.getString("username", "");
        mTextUser.setText(username);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        mNavigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Removing the user data when logged out of expense tracker
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent login_activity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login_activity);
                Toast.makeText(getApplicationContext(), "User Successfully Logged Out", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}