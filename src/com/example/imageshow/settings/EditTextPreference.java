package com.example.imageshow.settings;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Override EditTextPreferenc to show value as summary
 * @author user
 *
 */
public class EditTextPreference extends android.preference.EditTextPreference {

    public EditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        return this.getText();
    }
}
