package com.javxu.notelite.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.base.BackActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PasswordActivity extends BackActivity implements View.OnClickListener {

    private EditText et_now_password;
    private EditText et_new_password;
    private EditText et_new_confirm_password;
    private EditText et_email_user;
    private Button btn_update_password;
    private Button btn_find_password_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initView();
    }

    private void initView() {
        et_now_password = (EditText) findViewById(R.id.et_now_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_confirm_password = (EditText) findViewById(R.id.et_new_confirm_password);
        et_email_user = (EditText) findViewById(R.id.et_email_user);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(this);
        btn_find_password_user = (Button) findViewById(R.id.btn_find_password_user);
        btn_find_password_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_find_password_user:
                final String email = et_email_user.getText().toString().trim();
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(PasswordActivity.this, "密码服务请求成功，请到 " + email + " 邮箱进行密码重置操作", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(PasswordActivity.this, "找回失败，请检验邮箱是否输入有误:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.btn_update_password:
                String now_password = et_now_password.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();
                String confirm_password = et_new_confirm_password.getText().toString().trim();
                if ((TextUtils.isEmpty(now_password) != true) &&
                        (TextUtils.isEmpty(now_password) != true) &&
                        (TextUtils.isEmpty(now_password) != true)) {
                    if (new_password.equals(confirm_password)) {
                        BmobUser.updateCurrentUserPassword(now_password, new_password, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(PasswordActivity.this, "密码重置成功，可以用新密码进行登录啦", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(PasswordActivity.this, "密码重置失败:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(PasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PasswordActivity.this, "必需信息不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
