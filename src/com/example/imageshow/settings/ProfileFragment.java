package com.example.imageshow.settings;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.imageshow.R;
import com.example.imageshow.db.orm.DatabaseManager;
import com.example.imageshow.db.orm.Settings;
import com.j256.ormlite.dao.Dao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Fragment with profile data
 * 
 * @author user
 * 
 */
public class ProfileFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private static final String SAVE = "save";
    private static final String SETTINGS = "settings";
    private static final String SETTINGS_DEL = "profile_del";
    private static final String NAME = "name";

    private SharedPreferences preferences;
    private Dao<Settings, Long> settingsDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_profiles);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        preferences.registerOnSharedPreferenceChangeListener(this); // add listener

        findPreference(SAVE).setOnPreferenceClickListener(this);
        try {
            settingsDao = DatabaseManager.getInstance().getHelper().getViolationSettingsDao();
            initSettingsLists((ListPreference) findPreference(SETTINGS), (ListPreference) findPreference(SETTINGS_DEL));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ((EditTextPreference) findPreference(NAME)).setText("");
    }

    private void initSettingsLists(ListPreference... settingsList) {
        try {
            List<Settings> settings = settingsDao.queryForAll();
            List<String> entries = new ArrayList<String>();
            List<String> entryValues = new ArrayList<String>();
            for (int i = 0; i < settings.size(); i++) {
                Settings s = settings.get(i);
                entries.add(s.getName());
                entryValues.add(String.valueOf(s.getId()));
            }
            for (int i = 0; i < settingsList.length; i++) {
                settingsList[i].setEntries((CharSequence[]) entries.toArray(new CharSequence[entries.size()]));
                settingsList[i].setEntryValues((CharSequence[]) entryValues.toArray(new CharSequence[entryValues.size()]));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(SETTINGS)) {
            long id = Long.parseLong(sharedPreferences.getString(SETTINGS, "-1"));
            restoreSettings(id);
        } else if (key.equals(SETTINGS_DEL)) {
            long id = Long.parseLong(sharedPreferences.getString(SETTINGS_DEL, "-1"));
            delete(id);
            initSettingsLists((ListPreference) findPreference(SETTINGS), (ListPreference) findPreference(SETTINGS_DEL));

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Restore profile items
     * 
     * @param id - profile id
     */
    private void restoreSettings(long id) {
        try {
            Settings s = (Settings) settingsDao.queryForId(id);// settingsDataSource.getSettings(id);
            preferences.edit().putString(SettingsFragment.START_TIME, s.getStartupDelay().toString()).commit();
            preferences.edit().putString(SettingsFragment.END_TIME, s.getShutdownDelay().toString()).commit();
            preferences.edit().putBoolean(SettingsFragment.AVTO_CHARGE, s.getAvtoChargeStart()).commit();
            preferences.edit().putBoolean(SettingsFragment.AVTO_REBOOT, s.getAvtoResetStart()).commit();
            preferences.edit().putString(SettingsFragment.DELAY, String.valueOf(s.getDelay())).commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Save current profile as new
     */
    private void saveAsNew() {
        Settings s = new Settings();
        s.setStartupDelay(TimePreference.getTime(preferences.getString(SettingsFragment.START_TIME, "12:00")));
        s.setShutdownDelay(TimePreference.getTime(preferences.getString(SettingsFragment.END_TIME, "12:00")));
        s.setAvtoChargeStart(preferences.getBoolean(SettingsFragment.AVTO_CHARGE, false));
        s.setAvtoResetStart(preferences.getBoolean(SettingsFragment.AVTO_REBOOT, false));
        s.setDelay(Integer.parseInt(preferences.getString(SettingsFragment.DELAY, "5")));
        s.setName((((EditTextPreference) findPreference(NAME)).getText()));
        // settingsDataSource.insertSettings(s);
        try {
            settingsDao.create(s);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Delete profile
     * 
     * @param id - profile id
     */
    private void delete(long id) {
        try {
            settingsDao.deleteById(id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(SAVE)) {
            if (!((EditTextPreference) findPreference(NAME)).getText().isEmpty()) {
                saveAsNew();
                initSettingsLists((ListPreference) findPreference(SETTINGS), (ListPreference) findPreference(SETTINGS_DEL));
                Toast.makeText(getActivity().getApplicationContext(), R.string.profile_saved, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getActivity().getApplicationContext(), R.string.name_need_error, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

}
