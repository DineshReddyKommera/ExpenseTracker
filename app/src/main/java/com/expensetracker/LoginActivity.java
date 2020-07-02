/**
 * Login Activity which authenticates user
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;


public class LoginActivity extends AppCompatActivity {

    private DBHelper mDBHelper;
    private EditText mTxtUsrName, mTxtPwd;
    private Button mBtnLogin, mBtnSignUp;
    Context context;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDBHelper = new DBHelper(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mBtnLogin = findViewById(R.id.button_login);
        mBtnSignUp = findViewById(R.id.button_signup);
        mTxtUsrName = findViewById(R.id.text_username);
        mTxtPwd = findViewById(R.id.text_password);
        context = getApplicationContext();
        awesomeValidation.addValidation(this, R.id.text_username, RegexTemplate.NOT_EMPTY, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.text_password, RegexTemplate.NOT_EMPTY, R.string.pwdempty);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    String username = mTxtUsrName.getText().toString().trim();
                    String password = mTxtPwd.getText().toString().trim();
                    if (mDBHelper.validateUser(username,password)) {
                        SharedPreferences.Editor editor = getSharedPreferences("expensetracker", MODE_PRIVATE).edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        Intent home = new Intent(context, HomeScreenActivity.class);
                        startActivity(home);
                    } else {
                        mTxtPwd.setText("");
                        Toast.makeText(context, "Invalid User", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signup = new Intent(context, SignUpActivity.class);
                mTxtPwd.setText("");
                mTxtUsrName.setText("");
                startActivity(signup);
            }
        });
    }

}