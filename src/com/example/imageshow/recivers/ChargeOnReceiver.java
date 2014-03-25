package com.example.imageshow.recivers;

import com.example.imageshow.ScreenSlidePagerActivity;
import com.example.imageshow.settings.SettingsFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Reciver to start application after charge on
 * @author user
 *
 */
public class ChargeOnReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("charge on reciver");
        if (SettingsFragment.isAvtoCharge(context)) {
            Intent activity = new Intent(context, ScreenSlidePagerActivity.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activity);
        }
        ;

    }
}
