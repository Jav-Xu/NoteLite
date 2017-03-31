package com.javxu.notelite.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.javxu.notelite.R;

/**
 * Created by Jav-Xu on 2016/12/18.
 */

public abstract class SingFragmentActivity extends AppCompatActivity {

    protected abstract Fragment getFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = getFragment();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }
}
