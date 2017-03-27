package com.javxu.notelite.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BackActivity implements View.OnClickListener{

    private EditText et_regis_name;
    private EditText et_age;
    private EditText et_desc;
    private RadioGroup mRadioGroup;
    private EditText et_regis_password;
    private EditText et_confirm_password;
    private EditText et_email;
    private Button btn_confirm_register;

    private boolean isBoy = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        et_regis_name = (EditText) findViewById(R.id.et_regis_name);
        et_age = (EditText) findViewById(R.id.et_profile_age);
        et_desc = (EditText) findViewById(R.id.et_profile_desc);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        et_regis_password = (EditText) findViewById(R.id.et_regis_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_email = (EditText) findViewById(R.id.et_register_email);
        btn_confirm_register = (Button) findViewById(R.id.btn_confirm_register);
        btn_confirm_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm_register:
                infoConfim();
                break;
        }
    }

    private void infoConfim() {

        String username = et_regis_name.getText().toString().trim();
        String age = et_age.getText().toString().trim();
        String desc = et_desc.getText().toString().trim();
        String password = et_regis_password.getText().toString().trim();
        String passwordCopy = et_confirm_password.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if ((TextUtils.isEmpty(username) != true) &&
                (TextUtils.isEmpty(age) != true) &&
                (TextUtils.isEmpty(password) != true) &&
                (TextUtils.isEmpty(passwordCopy) != true) &&
                (TextUtils.isEmpty(email) != true)) {

            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    if (checkedId != R.id.rb_boy) {
                        isBoy = false;
                    } else {
                        isBoy = true;
                    }
                }
            });

            if (password.equals(passwordCopy)) {
                if (TextUtils.isEmpty(desc) == true) {
                    desc = "Too lazy to edit...";
                }

                MyUser user = new MyUser();
                user.setUsername(username);
                user.setAge(Integer.parseInt(age));
                user.setDesc(desc);
                user.setPassword(password);
                user.setSex(isBoy);
                user.setEmail(email);
                user.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败 " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.text_no_empty), Toast.LENGTH_SHORT).show();
        }
    }
}

