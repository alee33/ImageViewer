package com.example.imageshow.settings;

import java.util.ArrayList;
import java.util.List;

import com.example.imageshow.R;
import com.example.imageshow.db.Settings;
import com.example.imageshow.db.SettingsDataSource;
import com.example.imageshow.recivers.AlertAppReceiver;
import com.example.imageshow.recivers.FinishBroadcastReciver;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Fragment with profile data
 * @author user
 *
 */
public class ProfileItemFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String SAVE = "save";
    private static final String NAME = "name";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String DELAY = "delay";
    private static final String AVTO_CHARGE = "avto_charge";
    private static final String AVTO_REBOOT = "avto_reboot";
    private static final String SETTINGS = "settings";
   // private static final String ID = "id";
    private static final String ALARM = "alarm";

    private static final String TRANSFORMATION = "transformation";

    private SettingsDataSource settingsDataSource;

    public static final boolean isAvtoReboot(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AVTO_REBOOT, false);
    }

    public static final boolean isAvtoCharge(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AVTO_CHARGE, false);
    }

    public static final String getStartTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(START_TIME, "12:00");
    }

    public static final String getStopTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(END_TIME, "12:00");
    }

    public static final int getDelay(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(DELAY, "5000")) * 1000;
    }

    public static final int getTransformation(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(TRANSFORMATION, "0"));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_settings);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).registerOnSharedPreferenceChangeListener(this); // add listener

        settingsDataSource = new SettingsDataSource(getActivity().getApplicationContext());
        settingsDataSource.open();
        List<Settings> settings = settingsDataSource.getSettingsList();

        ListPreference settingsList = (ListPreference) findPreference(SETTINGS);

        List<String> entries = new ArrayList<String>();
        List<String> entryValues = new ArrayList<String>();
        for (int i = 0; i < settings.size(); i++) {
            Settings s = settings.get(i);
            entries.add(s.getName());
            entryValues.add(String.valueOf(s.getId()));
        }
        settingsList.setEntries((CharSequence[]) entries.toArray(new CharSequence[entries.size()]));
        settingsList.setEntryValues((CharSequence[]) entryValues.toArray(new CharSequence[entryValues.size()]));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(SETTINGS)) {
            long id = Long.parseLong(sharedPreferences.getString(SETTINGS, "-1"));
            restoreSettings(id);
        }

        if (ALARM.equals(key)) {
            boolean enabled = sharedPreferences.getBoolean(key, false);

            int flag = (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
            ComponentName component = new ComponentName(getActivity().getApplicationContext(), AlertAppReceiver.class);

            getActivity().getPackageManager().setComponentEnabledSetting(component, flag, PackageManager.DONT_KILL_APP);

            if (enabled) {
                AlertAppReceiver.setAlarm(getActivity().getApplicationContext());
                FinishBroadcastReciver.setAlarm(getActivity().getApplicationContext());
            } else {
                AlertAppReceiver.cancelAlarm(getActivity().getApplicationContext());
                FinishBroadcastReciver.cancelAlarm(getActivity().getApplicationContext());
            }
        } else if (START_TIME.equals(key)) {
            AlertAppReceiver.cancelAlarm(getActivity().getApplicationContext());
            AlertAppReceiver.setAlarm(getActivity().getApplicationContext());
        } else if (END_TIME.equals(key)) {
            FinishBroadcastReciver.cancelAlarm(getActivity().getApplicationContext());
            FinishBroadcastReciver.setAlarm(getActivity().getApplicationContext());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settingsDataSource.close(); // TODO ???
    }

    @Override
    public void onStop() {
        if (((CheckBoxPreference) findPreference(SAVE)).isChecked()) {
            saveAsNew();
            ((CheckBoxPreference) findPreference(SAVE)).setChecked(false);
        }
        super.onStop();
    }

    private void restoreSettings(long id) {
        Settings s = settingsDataSource.getSettings(id);
        ((TimePreference) findPreference(START_TIME)).setDefaultValue(s.getStartupDelay().toString());
        ((TimePreference) findPreference(END_TIME)).setDefaultValue(s.getShutdownDelay().toString());
        ((CheckBoxPreference) findPreference(AVTO_CHARGE)).setChecked(s.getAvtoChargeStart());
        ((CheckBoxPreference) findPreference(AVTO_REBOOT)).setChecked(s.getAvtoResetStart());
        ((EditTextPreference) findPreference(DELAY)).setText(String.valueOf(s.getDelay()));
        ((EditTextPreference) findPreference(NAME)).setText(s.getName());
        ((CheckBoxPreference) findPreference(SAVE)).setChecked(false);
        //((EditTextPreference) findPreference(ID)).setText(String.valueOf(s.getId()));
    }

    private void saveAsNew() {
        Settings s = new Settings();
        s.setStartupDelay(((TimePreference) findPreference(START_TIME)).getTime());
        s.setShutdownDelay(((TimePreference) findPreference(END_TIME)).getTime());
        s.setAvtoChargeStart(((CheckBoxPreference) findPreference(AVTO_CHARGE)).isChecked());
        s.setAvtoResetStart(((CheckBoxPreference) findPreference(AVTO_REBOOT)).isChecked());
        s.setDelay(Integer.parseInt((((EditTextPreference) findPreference(DELAY)).getText())));
        s.setName((((EditTextPreference) findPreference(NAME)).getText()));
        settingsDataSource.insertSettings(s);
    }
}
