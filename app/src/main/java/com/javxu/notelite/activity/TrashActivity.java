package com.javxu.notelite.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.javxu.notelite.R;
import com.javxu.notelite.adapter.NoteAdapter;
import com.javxu.notelite.base.BaseActivity;
import com.javxu.notelite.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.activity
 * File Name:     TrashActivity
 * Creator:       Jav-Xu
 * Create Time:   2017/9/5 11:38
 * Description:   回收箱界面
 */

public class TrashActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<Note> mTrashNotes;
    private NoteAdapter mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        initToolbar();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_trash);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_trash);
        mNoteAdapter = new NoteAdapter(this, mTrashNotes);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    private void initData() {
        mTrashNotes = DataSupport.where("abandoned = ?", "1").find(Note.class);
        mNoteAdapter.setNotes(mTrashNotes);
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
