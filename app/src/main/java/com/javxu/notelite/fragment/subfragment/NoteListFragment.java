package com.javxu.notelite.fragment.subfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javxu.notelite.R;
import com.javxu.notelite.adapter.NoteAdapter;
import com.javxu.notelite.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NoteListFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "NoteListFragment";
    private List<Note> mNotes;
    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mNoteAdapter = new NoteAdapter(getActivity(), mNotes);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mNoteAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
