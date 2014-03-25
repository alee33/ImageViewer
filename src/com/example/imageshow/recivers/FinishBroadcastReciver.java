package com.example.imageshow.recivers;

import java.util.Calendar;

import com.example.imageshow.settings.SettingsFragment;
import com.example.imageshow.settings.TimePreference;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Reciver to stop application by timer
 * @author user
 *
 */
public class FinishBroadcastReciver extends BroadcastReceiver {

    private Activity activity;

    public FinishBroadcastReciver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.finish();

    }

    public static void setAlarm(Context ctxt) {
        AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();

        String time = SettingsFragment.getStopTime(ctxt);

        cal.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
        cal.set(Calendar.MINUTE, TimePreference.getMinute(time));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        mgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), // no repeating
                getPendingIntent(ctxt));
    }

    private static PendingIntent getPendingIntent(Context ctxt) {
        Intent i = new Intent(ctxt, FinishBroadcastReciver.class);
        return (PendingIntent.getBroadcast(ctxt, 0, i, 0));
    }

    public static void cancelAlarm(Context ctxt) {
        AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(getPendingIntent(ctxt));
    }
}
