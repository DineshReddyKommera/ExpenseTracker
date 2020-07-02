/**
 * About Fragment provides about the insights of expenses tracker
 * @Author Abhinay Kacham, Dinesh Reddy Kommera
 */


package com.expensetracker.ui.about;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.expensetracker.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about, container, false);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.splash_screen_min)
                .setDescription("We help individuals to keep track of their expenses")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("expensetracker@gmail.com")
                .addWebsite("http://expensetracker.github.io/")
                .addFacebook("http://facebook.com/")
                .addTwitter("http://twitter.com/")
                .addYoutube("http://youtube.com/")
                .addPlayStore("com.expensetracker")
                .addInstagram("http://instragram.com/")
                .addGitHub("https://github.com/abhinaykacham/ExpenseTracker")
                .addItem(getCopyRightsElement())
                .create();
        return aboutPage;
    }

    /* Used the About Us API and tried to set copy right and other resources */

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}