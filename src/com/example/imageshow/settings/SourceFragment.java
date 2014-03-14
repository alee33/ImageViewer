package com.example.imageshow.settings;

import com.example.imageshow.R;
import com.example.imageshow.ScreenSlidePagerActivity;
import com.example.imageshow.rest.RequestManager;
import com.example.imageshow.rest.RequestManager.Operations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Fragment with selection data source
 * @author user
 *
 */
public class SourceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    public static final String SOURCE_KEY = "source";
    public static final String VK_KEY = "vk_id";
    public static final String UPDATE = "update";
    private final String TAG = getClass().getSimpleName();

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(UPDATE)) {
            if (SourceFragment.getSourceId(getActivity().getApplicationContext()) == ScreenSlidePagerActivity.VK_FOTO) {
                Log.d(TAG, "call request manager - get foto vk");
                RequestManager.from(getActivity().getApplicationContext()).performRequest(Operations.VK_FOTO, "owner_id", getVKPageId(getActivity().getApplicationContext()));
            }
            return true;
        }
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).registerOnSharedPreferenceChangeListener(this); // add listener
        ListPreference source = (ListPreference) findPreference(SOURCE_KEY);
        if (source.findIndexOfValue(source.getValue()) == ScreenSlidePagerActivity.VK_FOTO) {
            findPreference(VK_KEY).setEnabled(true);
            findPreference(UPDATE).setEnabled(true);
            onPreferenceClick(findPreference(UPDATE));// update
        } else {
            findPreference(VK_KEY).setEnabled(false);
            findPreference(UPDATE).setEnabled(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Preference pref = findPreference(key);
        if (key.equals(SOURCE_KEY)) {
            ListPreference listPref = (ListPreference) pref;
            if (listPref.findIndexOfValue(listPref.getValue()) == ScreenSlidePagerActivity.VK_FOTO) {
                findPreference(VK_KEY).setEnabled(true);
                findPreference(UPDATE).setEnabled(true);

            } else {
                findPreference(VK_KEY).setEnabled(false);
                findPreference(UPDATE).setEnabled(false);
            }

        }
    }

    public static final int getSourceId(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(SOURCE_KEY, "-1"));
    }

    public static final String getVKPageId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(VK_KEY, "-1");
    }
}
