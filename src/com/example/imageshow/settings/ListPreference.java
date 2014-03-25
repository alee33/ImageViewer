package com.example.imageshow.settings;


import android.content.Context;
import android.util.AttributeSet;
/**
 * Override ListPreference to show value as summary
 * @author user
 *
 */
public class ListPreference extends android.preference.ListPreference {

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
       
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        return this.getEntry();
    }
}
