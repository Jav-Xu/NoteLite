package com.javxu.notelite.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.javxu.notelite.R;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.activity
 * File Name:     SettingsFragment
 * Creator:       Jav-Xu
 * Create Time:   2017/4/5 16:35
 * Description:   设置界面Fragment
 */

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    private Context mContext;
    private Preference mUserPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mContext = getActivity();
        addPreferencesFromResource(R.xml.preference);
        // 将默认自建的shared文件也换成自己的Config
        // 但开始还是会创建 packageName+"_preferrence" 文件，并录入Swith的一个值，好在后面会同步到 config 中
        getPreferenceManager().setSharedPreferencesName("config");
        initPreference();
    }

    private void initPreference() {
        mUserPreference = findPreference("setting_user");
        mUserPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "setting_user":
                startActivity(new Intent(mContext, UserActivity.class));
                break;
        }
        return false;
    }
}
