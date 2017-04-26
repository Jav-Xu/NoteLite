package com.javxu.notelite.activity;

import android.app.Activity;
import android.content.Context;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.Note;
import com.javxu.notelite.utils.DateUtil;
import com.javxu.notelite.utils.FileUtil;
import com.javxu.notelite.utils.ImageUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.javxu.notelite.R.id.collapsing_toolbal;

public class NoteDetailActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_PHOTO = 1;
    public static final int REQUEST_GALLERY = 2;
    public static final int REQUEST_CROP = 3;

    private Note mNote;
    private File mImageFile; // Note相片文件
    private Uri mImageUri; // Note相片的Uri
    private File mCropFile;  //裁剪照片File
    private Uri mCropUri;    //裁剪照片Uri

    private Toolbar toolbar;
    private ImageView mDetailNotePicImageView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFloatingActionButton;
    private MaterialEditText mDetailNoteTitleEditText;
    private MaterialEditText mDetailNoteContentEditText;
    private TextView mDetailNoteDateTextView;
    private CheckBox mDetailNoteSolvedCheckBox;

    private boolean editable = false;
    private boolean isNewNote = false;

    public static Intent getIntent(Context context, int noteId) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra("NOTEID", noteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        initStatusBar();
        initToolbar();
        initData();
        initView();
        setEditable(editable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_notedetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mNote.isSaved()) {
                    if (editable) {
                        new AlertDialog.Builder(NoteDetailActivity.this)
                                .setTitle("尚未保存，是否直接离开？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.operate:
                if (editable) {
                    editable = false;
                    item.setIcon(R.drawable.ic_edit);
                    setEditable(false);
                    if (mNote.isSaved()) {
                        mNote.update(mNote.getId());
                    } else {
                        mNote.save();
                    }
                    //mNote.saveOrUpdate("id = ?", String.valueOf(mNote.getId()));
                    // 新建Note构造方法里没ID，但DataSupport可能有
                } else {
                    editable = true;
                    item.setIcon(R.drawable.ic_save);
                    setEditable(true);
                }
                break;
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle("注意：")
                        .setMessage("是否确定删除该笔记？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mNote.isSaved()) {
                                    DataSupport.delete(Note.class, mNote.getId());
                                }
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null).show();
                break;
            case R.id.send:
                Intent sendIntent = ShareCompat.IntentBuilder.from(NoteDetailActivity.this)
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isNewNote) {
            menu.findItem(R.id.operate).setIcon(R.drawable.ic_save);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_note_shoot_fab:
                new AlertDialog.Builder(NoteDetailActivity.this)
                        .setTitle("照片获取")
                        .setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if (initImageFileAndUriSuccess()) {
                                            toGallery();
                                        }
                                        break;
                                    case 1:
                                        if (initImageFileAndUriSuccess()) {
                                            toCamera();
                                        }
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.detail_note_date_textview:
                FragmentManager manager = getSupportFragmentManager();
                Calendar calendar = Calendar.getInstance();
                Date date = mNote.getNoteDate();
                calendar.setTime(date);
                DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show(manager, "Datepickerdialog");
                break;
            default:
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
        mNote.setNoteDate(date);
        //mNote.update(mNote.getId());
        mDetailNoteDateTextView.setText(DateUtil.dateToStr(date));
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        int noteId = getIntent().getIntExtra("NOTEID", -1);
        if (noteId == -1) { // 证明是新建笔记
            isNewNote = true;
            mNote = new Note();
            editable = true; // 新建的 Note 要一开始就处于编辑状态
            invalidateOptionsMenu(); // 回调 onPrepareOptionsMenu 方法，编辑toolbar
        } else {
            List<Note> notes = DataSupport.where("id = ?", String.valueOf(noteId)).find(Note.class);
            mNote = notes.get(0);
        }
    }

    private void initView() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(collapsing_toolbal);
        mDetailNotePicImageView = (ImageView) findViewById(R.id.detail_note_pic_image_view);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.detail_note_shoot_fab);
        mDetailNoteTitleEditText = (MaterialEditText) findViewById(R.id.detail_note_title_edit_text);
        mDetailNoteContentEditText = (MaterialEditText) findViewById(R.id.content_edit_text);
        mDetailNoteDateTextView = (TextView) findViewById(R.id.detail_note_date_textview);
        mDetailNoteSolvedCheckBox = (CheckBox) findViewById(R.id.detail_note_solved_check_box);

        mCollapsingToolbarLayout.setTitle(mNote.getNoteTitle());
        ImageUtil.loadImage(mNote.getNoteImagePath(), mDetailNotePicImageView);
        mDetailNoteTitleEditText.setText(mNote.getNoteTitle());
        mDetailNoteTitleEditText.setSelection(mNote.getNoteTitle().length());
        mDetailNoteContentEditText.setText(mNote.getNoteContent());
        mDetailNoteDateTextView.setText(DateUtil.dateToStr(mNote.getNoteDate()));
        mDetailNoteSolvedCheckBox.setChecked(mNote.isNoteSolved());

        PackageManager packageManager = getPackageManager();
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
                mCollapsingToolbarLayout.setTitle(s.toString());
                //mNote.update(mNote.getId());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDetailNoteContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setNoteContent(s.toString());
                //mNote.update(mNote.getId());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDetailNoteDateTextView.setOnClickListener(this);

        mDetailNoteSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mNote.setToDefault("noteSolved");
                } else {
                    mNote.setNoteSolved(true);
                }
                //mNote.update(mNote.getId());
            }
        });
    }

    private void setEditable(boolean flag) {
        mFloatingActionButton.setClickable(flag);
        mDetailNoteTitleEditText.setEnabled(flag);
        mDetailNoteDateTextView.setClickable(flag);
        mDetailNoteDateTextView.setEnabled(flag);
        mDetailNoteSolvedCheckBox.setEnabled(flag);
        mDetailNoteContentEditText.setEnabled(flag);
    }

    private boolean initImageFileAndUriSuccess() {
        boolean flag = false;
        mImageFile = new File(FileUtil.getExternalPicturesFileDir(),
                "NoteLite_IMG_" + String.valueOf(mNote.getId()) + "_" + System.currentTimeMillis() + ".jpg");
        mCropFile = new File(FileUtil.getExternalPicturesFileDir(),
                "NoteLite_IMG_CROP_" + String.valueOf(mNote.getId()) + "_" + System.currentTimeMillis() + ".jpg");
        // 一个主意（Bug）：新建笔记 mNote.getId() 为 0，也就是初始设置图片 id 为 0，在之后改变图片文件之后，id 才会按历史递增
        //TODO 新建笔记 mNote.getId() 为 0，在保存一次之后，再改变图片文件 id 才会修正，思索修改方法
        try {
            mImageFile.createNewFile();
            mCropFile.createNewFile();
            mImageUri = FileUtil.getUriFromFile(this, mImageFile);
            mCropUri = Uri.fromFile(mCropFile);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "预备照片文件路径出错", Toast.LENGTH_SHORT).show();
        } finally {
            return flag;
        }
    }

    private void toCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra("return-data", false);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraIntent, REQUEST_PHOTO);
    }

    private void toGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    private void toCrop(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");//设置裁剪
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropUri);
        startActivityForResult(cropIntent, REQUEST_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            mImageFile.delete();
            mCropFile.delete();
            return;
        }
        switch (requestCode) {
            case REQUEST_PHOTO:
                Uri uri_camera = FileUtil.getContentUriFromFile(NoteDetailActivity.this, mImageFile);
                toCrop(uri_camera);
                break;
            case REQUEST_GALLERY:
                Uri uri_gallery = data.getData();
                toCrop(uri_gallery);
                break;
            case REQUEST_CROP:
                try {
                    mImageFile.delete();
                    mNote.setNoteImagePath(mCropUri.getPath());
                    mDetailNotePicImageView.setImageURI(mCropUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }
}

