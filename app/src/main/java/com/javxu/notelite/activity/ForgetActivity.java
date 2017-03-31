package com.javxu.notelite.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.base.BackActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends BackActivity implements View.OnClickListener {

    private EditText et_email;
    private Button btn_find_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        et_email = (EditText) findViewById(R.id.et_email);
        btn_find_password = (Button) findViewById(R.id.btn_find_password);
        btn_find_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_find_password:
                final String email = et_email.getText().toString().trim();
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(ForgetActivity.this, "密码服务请求成功，请到 " + email + " 邮箱进行密码重置操作", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetActivity.this, "找回失败，请检验邮箱是否输入有误:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }
    }
}

