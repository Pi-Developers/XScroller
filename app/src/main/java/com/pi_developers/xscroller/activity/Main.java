package com.pi_developers.xscroller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.pi_developers.xscroller.R;

import com.pi_developers.xscroller.Xposed;
import com.pi_developers.xscroller.widget.SeekBarPreference;

import al.androidfire.loltint.Color;
import al.androidfire.loltint.LolTint;

public class Main extends PreferenceActivity {
    private SeekBarPreference mFactorPreference;
    private SharedPreferences sharedPreferences;

    private ListPreference ScrollStyle;
    private Preference mAbout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21)
        setTheme(android.R.style.Theme_Material_Light_DarkActionBar);
        super.onCreate(savedInstanceState);

        assert getActionBar() != null;
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.COLOR_TEAL));


        addPreferencesFromResource(R.xml.settings);

        sharedPreferences = getSharedPreferences(Xposed.class.getPackage().getName(), Context.MODE_WORLD_READABLE);

        mFactorPreference = (SeekBarPreference)findPreference("factor");

        mAbout = (Preference) findPreference("about_matter");
        mAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),About.class);
                startActivity(intent);
                return true;
            }
        });

        ListPreference scrollPolicy = (ListPreference) findPreference("scr_policy");
        ScrollStyle  = (ListPreference)findPreference("scr_style");
        mFactorPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int value = mFactorPreference.getProgress();
                sharedPreferences.edit().putInt("scroll_factor", value).apply();

                return true;
            }
        });
        ScrollStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String result = null;
                if (newValue instanceof String) {
                    result = (String) newValue;

                }

                if (result.equalsIgnoreCase("Smooth")) {
                    sharedPreferences.edit().putBoolean("smooth_scroll",true).apply();
                }
                else {
                    sharedPreferences.edit().putBoolean("smooth_scroll",false).apply();
                }
                return true;
            }
        });
        scrollPolicy.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String result = null;
                if (newValue instanceof String) {
                    result = (String) newValue;

                }

                if (result.equalsIgnoreCase("Vertical")) {
                    sharedPreferences.edit().putInt("scrolling_policy", Xposed.VERTICAL_SCROLL).apply();
                } else {
                    sharedPreferences.edit().putInt("scrolling_policy", Xposed.HORIZONTAL_SCROLL).apply();
                }

                return true;
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LolTint.on(true,true,true,this);
    }
}
