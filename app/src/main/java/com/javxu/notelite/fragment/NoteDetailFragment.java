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
import com.javxu.notelite.utils.DateUtil;
import com.javxu.notelite.utils.FileUtil;
import com.javxu.notelite.utils.ImageUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.javxu.notelite.R.id.collapsing_toolbal;


public class NoteDetailFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_PHOTO = 1;
    public static final int REQUEST_GALLERY = 2;
    public static final int REQUEST_CROP = 3;

    private Note mNote;
    private File mImageFile; // Note相片文件
    private Uri mImageUri; // Note相片的Uri

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

        View view = inflater.inflate(R.layout.fragment_notedetail, container, false);
        initStatusBar();
        initToolbar(view);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                //使用原生 DatePicker 的方式
                //DatePickerFragment fragment = DatePickerFragment.newInstance(mNote.getNoteDate());
                //fragment.setTargetFragment(NoteDetailFragment.this, REQUEST_DATE);
                //fragment.show(manager, "DIALOGDATE");
                Calendar calendar = Calendar.getInstance();
                Date date = mNote.getNoteDate();
                calendar.setTime(date);
                DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(manager, "Datepickerdialog");
                break;
            default:
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
        mNote.setNoteDate(date);
        mNote.update(mNote.getId());
        mDetailNoteDateButton.setText(DateUtil.dateToStr(date));
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_detail);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        int noteId = getArguments().getInt("NOTEID");
        List<Note> notes = DataSupport.where("id = ?", String.valueOf(noteId)).find(Note.class);
        mNote = notes.get(0);
    }

    private void initView(View view) {
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
        mDetailNoteDateButton.setText(DateUtil.dateToStr(mNote.getNoteDate()));
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
    }

    private boolean initImageFileAndUri() {
        boolean flag = false;
        try {
            mImageFile = new File(FileUtil.getExternalPicturesFileDir(),
                    "NoteLite_IMG_" + String.valueOf(mNote.getId()) + "_" + System.currentTimeMillis() + ".jpg");
            mImageFile.createNewFile();
            if (mImageFile.exists()) {
                mImageUri = FileUtil.getUriFromFile(getActivity(), mImageFile);
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "预备照片文件路径出错", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    private void toCamera() {
        if (initImageFileAndUri()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            cameraIntent.putExtra("return-data", false); // 设置是否在 onActivityResult 方法的 intent 值中返回 Bitmap 缩略图对象
            startActivityForResult(cameraIntent, REQUEST_PHOTO);
        }
    }

    private void toGallery() {
        if (initImageFileAndUri()) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            galleryIntent.putExtra("return-data", false); // 设置是否在 onActivityResult 方法的 intent 值中返回 Bitmap 缩略图对象
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
        }
    }

    private void toCrop(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");//设置裁剪
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri); // 设置是否将裁剪结果保存到指定文件中，这里直接在原图上裁剪覆盖操作
        cropIntent.putExtra("return-data", false); // 设置是否在 onActivityResult 方法的 intent 值中返回 Bitmap 缩略图对象
        startActivityForResult(cropIntent, REQUEST_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            /*case REQUEST_DATE:
                if (data == null) {
                    return;
                }
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mDetailNoteDateButton.setText(date.toString());
                mNote.setNoteDate(date);
                mNote.update(mNote.getId());
                break;*/
            case REQUEST_PHOTO:
                if (resultCode != Activity.RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                //Uri photoUri = data.getData(); //空指针
                Uri photoUri = mImageUri;
                toCrop(photoUri);
                break;
            case REQUEST_GALLERY:
                // 这里要注意，不同版本手机 返回data取出照片实际路径方法不一样，这里都给了 crop 处理
                if (resultCode != Activity.RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                Uri galleryUri = data.getData();
                toCrop(galleryUri);
                break;
            case REQUEST_CROP:
                if (resultCode != Activity.RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                mNote.setNoteImagePath(mImageUri.getPath()); //String.valueOf(mImageUri)) 返回会加上 file:///stroage/sdcard...
                mNote.update(mNote.getId());
                ImageUtil.loadImage(mNote.getNoteImagePath(), mDetailNotePicImageView);
                break;
            default:
        }
    }
}
