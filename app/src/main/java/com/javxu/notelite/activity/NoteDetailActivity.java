package com.javxu.notelite.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.javxu.notelite.fragment.NoteDetailFragment;

public class NoteDetailActivity extends SingFragmentActivity {

    public static Intent getIntent(Context context, int noteId) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra("NOTEID", noteId);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        int noteId = getIntent().getIntExtra("NOTEID", 0);
        return NoteDetailFragment.newInstance(noteId);
    }

}
