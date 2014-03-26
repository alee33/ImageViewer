package com.example.imageshow.settings;


import com.example.imageshow.R;
import com.example.imageshow.recivers.AlertAppReceiver;
import com.example.imageshow.recivers.FinishBroadcastReciver;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Fragment with app settings data
 * @author user
 *
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    static final String START_TIME = "start_time";
    static final String END_TIME = "end_time";
    static final String DELAY = "delay";
    static final String AVTO_CHARGE = "avto_charge";
    static final String AVTO_REBOOT = "avto_reboot";
    static final String ALARM = "alarm";

    static final String TRANSFORMATION = "transformation";

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
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(DELAY, "5")) * 1000;
    }

    public static final int getTransformation(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(TRANSFORMATION, "0"));
    }

    private Context context;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_settings);
        context= getActivity().getApplicationContext();
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this); // add listener
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (ALARM.equals(key)) {
            boolean enabled = sharedPreferences.getBoolean(key, false);

            int flag = (enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
            ComponentName component = new ComponentName(context, AlertAppReceiver.class);

            context.getPackageManager().setComponentEnabledSetting(component, flag, PackageManager.DONT_KILL_APP);

            if (enabled) {
                AlertAppReceiver.setAlarm(context);
                FinishBroadcastReciver.setAlarm(context);
            } else {
                AlertAppReceiver.cancelAlarm(context);
                FinishBroadcastReciver.cancelAlarm(context);
            }
        } else if (START_TIME.equals(key)) {
            AlertAppReceiver.cancelAlarm(context);
            AlertAppReceiver.setAlarm(context);
        } else if (END_TIME.equals(key)) {
            FinishBroadcastReciver.cancelAlarm(context);
            FinishBroadcastReciver.setAlarm(context);
      
        }

    }
}
