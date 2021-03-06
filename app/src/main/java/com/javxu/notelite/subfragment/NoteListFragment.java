package com.javxu.notelite.subfragment;

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

public class NoteListFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_notelist, container, false);
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

    public void updateList() {

        mNotes = DataSupport.where("abandoned = ?", "0").find(Note.class); // LitePal Boolean 存储形式居然是 0/1

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(getActivity(), mNotes);
            mRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.setNotes(mNotes);
            mNoteAdapter.notifyDataSetChanged();//not smart
        }
    }

}
