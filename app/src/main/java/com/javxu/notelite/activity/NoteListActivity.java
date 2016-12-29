package com.javxu.notelite.activity;

import android.support.v4.app.Fragment;

import com.javxu.notelite.fragment.NoteListFragment;

public class NoteListActivity extends SingFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new NoteListFragment();
    }
}
