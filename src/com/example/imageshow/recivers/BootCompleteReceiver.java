package com.example.imageshow.recivers;

import com.example.imageshow.ScreenSlidePagerActivity;
import com.example.imageshow.settings.ProfileItemFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Reciver to start application after reboot
 * @author user
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ProfileItemFragment.isAvtoReboot(context)) {
            Intent activity = new Intent(context, ScreenSlidePagerActivity.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activity);
        }

        AlertAppReceiver.setAlarm(context);
    }
}
