package com.javxu.notelite.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.Note;
import com.javxu.notelite.utils.FileUtil;
import com.javxu.notelite.utils.ImageUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.javxu.notelite.R.id.collapsing_toolbal;


public class NoteDetailFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_PHOTO = 1;
    public static final int REQUEST_GALLERY = 2;
    public static final int REQUEST_CROP = 3;

    private Note mNote;
    private Uri imageUri;

    private Toolbar toolbar;
    private ImageView mDetailNotePicImageView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFloatingActionButton;
    private EditText mDetailNoteTitleEditText;
    private Button mDetailNoteDateButton;
    private CheckBox mDetailNoteSolvedCheckBox;


    public static NoteDetailFragment newInstance(int noteId) {
        Bundle args = new Bundle();
        args.putInt("NOTEID", noteId);

        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        View view = inflater.inflate(R.layout.fragment_notedetail, container, false);

        int noteId = getArguments().getInt("NOTEID");
        List<Note> notes = DataSupport.where("id = ?", String.valueOf(noteId)).find(Note.class);
        mNote = notes.get(0);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_detail);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(collapsing_toolbal);
        mDetailNotePicImageView = (ImageView) view.findViewById(R.id.detail_note_pic_image_view);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.detail_note_shoot_fab);
        mDetailNoteTitleEditText = (EditText) view.findViewById(R.id.detail_note_title_edit_text);
        mDetailNoteDateButton = (Button) view.findViewById(R.id.detail_note_date_button);
        mDetailNoteSolvedCheckBox = (CheckBox) view.findViewById(R.id.detail_note_solved_check_box);

        mCollapsingToolbarLayout.setTitle(mNote.getNoteTitle());
        ImageUtil.loadImage(mNote.getNoteImagePath(), mDetailNotePicImageView);
        mDetailNoteTitleEditText.setText(mNote.getNoteTitle());
        mDetailNoteTitleEditText.setSelection(mNote.getNoteTitle().length());
        mDetailNoteDateButton.setText(mNote.getNoteDate().toString());
        mDetailNoteSolvedCheckBox.setChecked(mNote.isNoteSolved());

        PackageManager packageManager = getActivity().getPackageManager();
        Intent shootIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canShoot = (mNote.getNoteImagePath() != null) && (shootIntent.resolveActivity(packageManager) != null);
        if (canShoot) {
            mFloatingActionButton.setClickable(true);
            mFloatingActionButton.setOnClickListener(this);
        }

        mDetailNoteTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setNoteTitle(s.toString());
                mCollapsingToolbarLayout.setTitle(mNote.getNoteTitle());
                mNote.update(mNote.getId());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDetailNoteDateButton.setOnClickListener(this);

        mDetailNoteSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mNote.setToDefault("noteSolved");
                } else {
                    mNote.setNoteSolved(true);
                }
                mNote.update(mNote.getId());
            }
        });

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_notedetail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                getActivity().finish();
//                break;
            case R.id.delete:
                new AlertDialog.Builder(getActivity())
                        .setTitle("注意：")
                        .setMessage("是否确定删除该笔记？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DataSupport.delete(Note.class, mNote.getId());
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.send:
                Intent sendIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(mNote.getNoteTitle())
                        .setChooserTitle(getString(R.string.send_report))
                        .createChooserIntent();
                startActivity(sendIntent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_note_shoot_fab:
                try {
                    File tempFile = new File(FileUtil.getExternalPicturesFileDir(),
                            "NoteLite_IMG_" + String.valueOf(mNote.getId()) + "_" + System.currentTimeMillis() + ".jpg");
                    tempFile.createNewFile();
                    if (tempFile.exists()) {
                        imageUri = FileUtil.getUriFromFile(getActivity(), tempFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "预备照片文件路径出错", Toast.LENGTH_SHORT).show();
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("照片获取")
                        .setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        toGallery();
                                        break;
                                    case 1:
                                        toCamera();
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.detail_note_date_button:
                FragmentManager manager = getFragmentManager();
                DatePickerFragment fragment = DatePickerFragment.newInstance(mNote.getNoteDate());
                fragment.setTargetFragment(NoteDetailFragment.this, REQUEST_DATE);
                fragment.show(manager, "DIALOGDATE");
                break;
            default:
        }
    }

    private void toCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_PHOTO);
    }

    private void toGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mDetailNoteDateButton.setText(date.toString());
                mNote.setNoteDate(date);
                mNote.update(mNote.getId());
                break;
            case REQUEST_PHOTO:
                mNote.setNoteImagePath(String.valueOf(imageUri));
                mNote.update(mNote.getId());
                ImageUtil.loadImage(mNote.getNoteImagePath(), mDetailNotePicImageView);
                break;
            case REQUEST_GALLERY:
                imageUri = data.getData();
                mNote.setNoteImagePath(String.valueOf(imageUri));
                mNote.update(mNote.getId());
                ImageUtil.loadImage(mNote.getNoteImagePath(), mDetailNotePicImageView);
                break;
            case REQUEST_CROP:

                break;
            default:
        }
    }
}
