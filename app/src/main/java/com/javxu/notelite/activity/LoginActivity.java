package com.javxu.notelite.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.MyUser;
import com.javxu.notelite.utils.SharedUtil;
import com.javxu.notelite.utils.StaticUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_username;
    private EditText et_login_password;
    private Button bt_login;
    private Button bt_register;
    private CheckBox mKeepPassWord;
    private TextView tv_forget;

    private boolean isKeep;
    private String username;
    private String password;

    private SweetAlertDialog mMyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        et_login_username = (EditText) findViewById(R.id.et_login_name);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        mKeepPassWord = (CheckBox) findViewById(R.id.keep_password);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.btn_login);
        bt_login.setOnClickListener(this);
        bt_register = (Button) findViewById(R.id.btn_register);
        bt_register.setOnClickListener(this);
    }

    private void initData() {
        isKeep = SharedUtil.getBoolean(this, StaticUtil.SHARE_IS_KEEP, false);
        if (isKeep) {
            mKeepPassWord.setChecked(true);
            username = SharedUtil.getString(this, StaticUtil.SHARE_LAST_USERNAME, "");
            password = SharedUtil.getString(this, StaticUtil.SHARE_LAST_PASSWORD, "");
            et_login_username.setText(username);
            et_login_password.setText(password);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                infoConfim();
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
        }
    }

    private void infoConfim() {

        mMyDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mMyDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mMyDialog.setTitleText("Loading");
        mMyDialog.setCancelable(false);

        username = et_login_username.getText().toString().trim();
        password = et_login_password.getText().toString().trim();

        if ((TextUtils.isEmpty(username) != true) && (TextUtils.isEmpty(password) != true)) {

            mMyDialog.show();

            final MyUser user = new MyUser();
            user.setUsername(username);
            user.setPassword(password);

            user.login(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (e == null) {
                        if (user.getEmailVerified()) { // 再判断邮箱是否验证成功
                            mMyDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            mMyDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            mMyDialog.setTitleText("登陆成功");
                            mMyDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            mMyDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            mMyDialog.setTitleText("请前往邮箱验证再来登陆");
                        }
                    } else {
                        mMyDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        mMyDialog.setTitleText("登陆失败" + e.toString());
                    }
                }
            });
        } else {
            mMyDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            mMyDialog.setTitleText(getString(R.string.text_no_empty));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean isKeep = mKeepPassWord.isChecked();
        if (isKeep) {
            SharedUtil.putBoolean(this, StaticUtil.SHARE_IS_KEEP, true);
            SharedUtil.putString(this, StaticUtil.SHARE_LAST_USERNAME, username);
            SharedUtil.putString(this, StaticUtil.SHARE_LAST_PASSWORD, password);
        } else {
            SharedUtil.deleteItem(this, StaticUtil.SHARE_IS_KEEP);
            SharedUtil.deleteItem(this, StaticUtil.SHARE_LAST_USERNAME);
            SharedUtil.deleteItem(this, StaticUtil.SHARE_LAST_PASSWORD);
        }
    }
}
