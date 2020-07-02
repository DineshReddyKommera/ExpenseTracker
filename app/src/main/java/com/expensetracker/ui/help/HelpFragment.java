/**
 * Help Fragment which helps the user to contact team
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */
package com.expensetracker.ui.help;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.expensetracker.R;

public class HelpFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_help, container, false);
        final TextView textView = root.findViewById(R.id.text_help);
        textView.setText(R.string.help);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return root;
    }
}