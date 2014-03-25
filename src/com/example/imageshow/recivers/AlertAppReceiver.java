package com.example.imageshow.recivers;

import java.util.Calendar;

import com.example.imageshow.ScreenSlidePagerActivity;
import com.example.imageshow.settings.SettingsFragment;
import com.example.imageshow.settings.TimePreference;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Reciver to startup applicatiom by timer
 * @author user
 *
 */
public class AlertAppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ScreenSlidePagerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        // FinishBroadcastReciver.setAlarm(context); //alarm to stop activity
    }

    public static void setAlarm(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
        String time = SettingsFragment.getStartTime(context);

        cal.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
        cal.set(Calendar.MINUTE, TimePreference.getMinute(time));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context ctxt) {
        Intent i = new Intent(ctxt, AlertAppReceiver.class);

        return (PendingIntent.getBroadcast(ctxt, 0, i, 0));
    }

    public static void cancelAlarm(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        mgr.cancel(getPendingIntent(context));
    }
}
