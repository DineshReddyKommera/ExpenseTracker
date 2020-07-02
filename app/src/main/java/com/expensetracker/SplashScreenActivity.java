/**
 * A Splash Activity which displays the splash screen for expense tracker application
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private DBHelper mydb ;
    private static int SPLASH_SCREEN_TIME_OUT = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent i = new Intent(SplashScreenActivity.this,
//                        LoginActivity.class);
                Intent i = new Intent(SplashScreenActivity.this,
                        HomeScreenActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}