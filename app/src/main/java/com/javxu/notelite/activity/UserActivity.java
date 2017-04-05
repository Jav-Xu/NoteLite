package com.javxu.notelite.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.javxu.notelite.R;
import com.javxu.notelite.bean.MyUser;
import com.javxu.notelite.utils.FileUtil;
import com.javxu.notelite.utils.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_PHOTO = 11;
    public static final int REQUEST_GALLERY = 22;
    public static final int REQUEST_CROP = 33;

    private Toolbar tb_user;
    private CircleImageView civ_profile_image;

    private EditText et_profile_name;
    private EditText et_profile_age;
    private EditText et_profile_sex;
    private EditText et_profile_desc;

    private Button btn_profile_exit;
    private Button btn_password_serve;

    private File mImageFile; //拍摄的照片临时文件
    private Uri mImageUri; //拍摄的照片临时文件

    private Bitmap mAvatarBitmap; //裁剪后的头像文件

    private SweetAlertDialog mMyDialog;

    private static final int DELAY = 404;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELAY:
                    mMyDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initToolbar();
        initView();
        initData();
    }

    private void initToolbar() {
        tb_user = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(tb_user);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        civ_profile_image = (CircleImageView) findViewById(R.id.civ_profile_image);
        civ_profile_image.setOnClickListener(this);

        et_profile_name = (EditText) findViewById(R.id.et_profile_username);
        et_profile_age = (EditText) findViewById(R.id.et_profile_age);
        et_profile_sex = (EditText) findViewById(R.id.et_profile_sex);
        et_profile_desc = (EditText) findViewById(R.id.et_profile_desc);

        btn_profile_exit = (Button) findViewById(R.id.btn_profile_exit);
        btn_profile_exit.setOnClickListener(this);
        btn_password_serve = (Button) findViewById(R.id.btn_password_serve);
        btn_password_serve.setOnClickListener(this);

        setEditEnable(false);
    }

    private void initData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        String name = user.getUsername();
        int age = user.getAge();
        boolean isBoy = user.isSex();
        String desc = user.getDesc();
        String avatarStr = user.getAvatarStr();
        byte[] imageByteArray = avatarStr == null ? null : Base64.decode(avatarStr, Base64.DEFAULT);

        et_profile_name.setText(name);
        et_profile_age.setText(String.valueOf(age));
        et_profile_sex.setText(isBoy ? "男" : "女");
        et_profile_desc.setText(desc);
        Glide.with(this).load(imageByteArray).error(R.mipmap.ic_launcher).into(civ_profile_image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_profile_image:
                new AlertDialog.Builder(this)
                        .setTitle("头像获取")
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
            case R.id.btn_password_serve:
                startActivity(new Intent(UserActivity.this, PasswordActivity.class));
                break;
            case R.id.btn_profile_exit:
                userExit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PHOTO:
                if (resultCode != Activity.RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                Uri uri_camera = mImageUri;
                //Uri uri_camera = data.getData(); //这里为什么 NullPointerException
                toCrop(uri_camera);
                break;
            case REQUEST_GALLERY:
                if (resultCode != Activity.RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                Uri uri_gallery = data.getData();
                toCrop(uri_gallery);
                break;
            case REQUEST_CROP:
                if (resultCode != Activity.RESULT_OK) {
                    mImageFile.delete();
                    return;
                }
                try {
                    mAvatarBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                civ_profile_image.setImageBitmap(mAvatarBitmap);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                setEditEnable(true);
                break;
            case R.id.save:
                profileUpdate();
                break;
        }
        return true;
    }

    /**
     * 设置四个 EditText 的可编辑性
     *
     * @param b 四个 EditText 的可编辑与否
     */
    private void setEditEnable(boolean b) {
        civ_profile_image.setClickable(b);
        et_profile_name.setEnabled(b);
        et_profile_age.setEnabled(b);
        et_profile_sex.setEnabled(b);
        et_profile_desc.setEnabled(b);
    }

    private void initImageFileAndUri() {
        mImageFile = new File(FileUtil.getExternalPicturesFileDir(), "NoteLite_UserAvaTemp_" + System.currentTimeMillis() + ".jpg");
        try {
            mImageFile.createNewFile();
            mImageUri = FileUtil.getUriFromFile(this, mImageFile);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "为照片预备文件出错", Toast.LENGTH_SHORT).show();
        }
    }

    private void toCamera() {
        initImageFileAndUri();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraIntent, REQUEST_PHOTO);
    }

    private void toGallery() {
        initImageFileAndUri();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    private void toCrop(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");

        cropIntent.putExtra("crop", "true");//设置裁剪

        cropIntent.putExtra("aspectX", 1);//裁剪宽高比例
        cropIntent.putExtra("aspectY", 1);

        cropIntent.putExtra("outputX", 320);//裁剪图片的质量
        cropIntent.putExtra("outputY", 320);

        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri); // 设置是否将裁剪结果保存到指定文件中，这里直接在原图上裁剪覆盖操作
        cropIntent.putExtra("return-data", false); // 设置是否在 onActivityResult 方法的 intent 值中返回 Bitmap 缩略图对象
        startActivityForResult(cropIntent, REQUEST_CROP);
    }

    private void userExit() {
        BmobUser.logOut();   //清除缓存用户对象
        BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
        startActivity(new Intent(UserActivity.this, LoginActivity.class));
        finish();
    }

    private void profileUpdate() {

        mMyDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mMyDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mMyDialog.setTitleText("用户信息更新中");
        mMyDialog.setCancelable(false);

        String name_new = et_profile_name.getText().toString().trim();
        String age_new = et_profile_age.getText().toString().trim();
        String sex_new = et_profile_sex.getText().toString().trim();
        String desc_new = et_profile_desc.getText().toString().trim();
        String avaStr_new = ImageUtil.bitmapToBase64(mAvatarBitmap);

        if ((TextUtils.isEmpty(name_new) == false) &&
                (TextUtils.isEmpty(age_new) == false) &&
                (TextUtils.isEmpty(sex_new) == false)) {

            if (TextUtils.isEmpty(desc_new)) {
                desc_new = "Too lazy to edit...";
            }

            mMyDialog.show();

            MyUser newUser = new MyUser();
            newUser.setUsername(name_new);
            newUser.setAge(Integer.parseInt(age_new));
            newUser.setSex((sex_new.equals("男")) ? true : false);
            newUser.setDesc(desc_new);
            newUser.setAvatarStr(avaStr_new);

            BmobUser bmobUser = BmobUser.getCurrentUser();
            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        setEditEnable(false);
                        mMyDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        mMyDialog.setTitleText("用户信息更新成功");
                        mHandler.sendEmptyMessageDelayed(DELAY, 1000);
                    } else {
                        mMyDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        mMyDialog.setTitleText("用户信息更新失败：" + e.toString());
                    }
                }
            });
        } else {
            mMyDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            mMyDialog.setTitleText(getString(R.string.text_no_empty));
        }
    }
}

