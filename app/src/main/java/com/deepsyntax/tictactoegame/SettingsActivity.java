package com.deepsyntax.tictactoegame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
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
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference pref = prefScreen.getPreference(i);
            if (!(pref instanceof SwitchPreference)) {
                String value = sharedPreferences.getString(pref.getKey(), "");
                setPreferenceSummary(pref, value);
            }
        }
    }

    private void setPreferenceSummary(Preference pref, String value) {
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            int prefIndex = listPref.findIndexOfValue(value);
            if (prefIndex >= 0) {
                listPref.setSummary(listPref.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (null != pref) {
            if (!(pref instanceof SwitchPreference)) {
                String value = sharedPreferences.getString(pref.getKey(), "");
                setPreferenceSummary(pref, value);
            }
        }
    }
}
