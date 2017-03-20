package com.javxu.notelite.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.javxu.notelite.R;
import com.javxu.notelite.activity.NoteDetailActivity;
import com.javxu.notelite.bean.Note;
import com.javxu.notelite.utils.Utils;

import java.util.List;

/**
 * Created by Jav-Xu on 2016/12/18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private Context mContext;
    private List<Note> mNotes;
    private LayoutInflater mInflater;

    public void setNotes(List<Note> notes) {
        mNotes = notes;
    }

    public List<Note> getNotes() {
        return mNotes;
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private TextView mItemNoteTitleTextView;
        private ImageView mItemNotePicImageView;
        private TextView mItemNoteDateTextView;
        private CheckBox mItemNoteSolvedCheckBox;

        public NoteHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mItemNoteTitleTextView = (TextView) itemView.findViewById(R.id.item_note_title_text_view);
            mItemNotePicImageView = (ImageView) itemView.findViewById(R.id.item_note_pic_image_view);
            mItemNoteDateTextView = (TextView) itemView.findViewById(R.id.item_note_date_text_view);
            mItemNoteSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.item_note_solved_check_box);
        }

        public void bindView(final Note note) {
            if (note != null) {
                mItemNoteTitleTextView.setText(note.getNoteTitle());
                Utils.loadImage(note.getNoteImagePath(), mItemNotePicImageView);
                mItemNoteDateTextView.setText(note.getNoteDate().toString());
                mItemNoteSolvedCheckBox.setChecked(note.isNoteSolved());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = NoteDetailActivity.getIntent(mContext, note.getId());
                        mContext.startActivity(intent);
                    }
                });
            }

        }
    }

    public NoteAdapter(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_note_list, parent, false);
        NoteHolder noteHolder = new NoteHolder(itemView);
        return noteHolder;
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.bindView(note);
    }

    @Override
    public int getItemCount() {
        return mNotes == null ? 0 : mNotes.size();
    }
}
