package com.example.imageshow.settings;

import java.util.List;

import com.example.imageshow.R;

import android.preference.PreferenceActivity;

/**
 * Settings activity
 * @author user
 *
 */
public class SettingsActivity extends PreferenceActivity {

    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

}
