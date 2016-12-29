package com.javxu.notelite.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.javxu.notelite.R;
import com.javxu.notelite.activity.NoteDetailActivity;
import com.javxu.notelite.adapter.NoteAdapter;
import com.javxu.notelite.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NoteListFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "NoteListFragment";
    private List<Note> mNotes;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private int mLayoutManagerNum = 1;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) view.findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_list); //默认
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_list:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_gallery:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_trash:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_encourage:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_author:
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                }
                return true;
            }
        });

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_list);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        fab = (FloatingActionButton) view.findViewById(R.id.list_note_add_fab);
        fab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mNoteAdapter = new NoteAdapter(getActivity(), mNotes);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mNoteAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.note_list_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.style:
                if (++mLayoutManagerNum % 2 == 0) {
                    RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

                    mRecyclerView.setLayoutManager(manager);
                } else {
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(manager);
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_note_add_fab:
                Note note = new Note();
                note.save();
                Intent intent = NoteDetailActivity.getIntent(getActivity(), note.getId());
                startActivity(intent);
                break;
            default:
        }
    }

    public void updateList() {

        mNotes = DataSupport.findAll(Note.class);

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(getActivity(), mNotes);
            mRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.setNotes(mNotes);
            mNoteAdapter.notifyDataSetChanged();//not smart
        }
    }

}
