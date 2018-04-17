package com.deepsyntax.tictactoegame;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

public class SettingsActivity extends PreferenceActivity {
    public static final String SETTINGS_TO_SHOW = "SETTINGS_TO_SHOW";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        if (extra.getInt(SETTINGS_TO_SHOW, 0) == 1) {
            addPreferencesFromResource(R.xml.pref_gameplay);
        } else {
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
