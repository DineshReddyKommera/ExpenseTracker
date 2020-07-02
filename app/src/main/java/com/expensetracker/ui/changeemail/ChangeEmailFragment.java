/**
 * Fragment which is used to change email of the user
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */

package com.expensetracker.ui.changeemail;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.expensetracker.DBHelper;
import com.expensetracker.R;

import static android.content.Context.MODE_PRIVATE;


public class ChangeEmailFragment extends Fragment {

    private DBHelper mDBHelper;
    private EditText mChangeEmail;
    private Button mBtnUpdateEmail;
    private String changeEmail,username;
    private SharedPreferences prefs;
    private AwesomeValidation awesomeValidation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDBHelper = new DBHelper(getContext());
        View root = inflater.inflate(R.layout.fragment_change_email, container, false);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mChangeEmail = root.findViewById(R.id.text_updateemail);
        mBtnUpdateEmail = root.findViewById(R.id.button_updateemail);
        prefs = getContext().getSharedPreferences("expensetracker", MODE_PRIVATE);
        username= prefs.getString("username", "");
        mBtnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
                if(awesomeValidation.validate()) {
                    changeEmail = mChangeEmail.getText().toString();
                    if (mDBHelper.changeEmail(username, changeEmail)) {
                        Toast.makeText(getContext(), "Updated Email", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Unable to update email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return root;
    }

    private void validateEmail() {
        awesomeValidation.addValidation(getActivity(), R.id.text_updateemail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
    }
}