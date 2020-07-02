/**
 * A Splash Activity which displays the splash screen for expense tracker application
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private DBHelper mydb ;
    private static int SPLASH_SCREEN_TIME_OUT = 800;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        sharedPreferences=this.getSharedPreferences("expensetracker",MODE_PRIVATE);
        if(sharedPreferences.contains("username")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this,
                            HomeScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_SCREEN_TIME_OUT);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_SCREEN_TIME_OUT);
        }

    }
}