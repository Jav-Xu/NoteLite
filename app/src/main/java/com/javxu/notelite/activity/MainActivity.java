package com.javxu.notelite.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.javxu.notelite.R;
import com.javxu.notelite.adapter.FragmentApater;
import com.javxu.notelite.bean.MyUser;
import com.javxu.notelite.utils.StaticUtil;

import java.util.Arrays;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_PERMISSION = 0;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton fab;

    private CircleImageView mNavCircleImageView;
    private TextView mEmailTextView;
    private TextView mNameTextView;
    private FragmentApater mHomeFragmentAdapter;

    private BroadcastReceiver mLogoutReceiver; // 退出登录后，UserActivity 发送广播，MainActivity就不能作为栈底，也要销毁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        initViewPager();
        initData();
        initBroadcastReceiver();
        initRequestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.list_note_add_fab);
        fab.setOnClickListener(this);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setCheckedItem(R.id.nav_list); //默认
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_list:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_tools:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_trash:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_share:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    default:
                }
                return true;
            }
        });
        View headerView = mNavigationView.getHeaderView(0);
        mNavCircleImageView = (CircleImageView) headerView.findViewById(R.id.icon_image);
        mNavCircleImageView.setOnClickListener(this);
        mEmailTextView = (TextView) headerView.findViewById(R.id.mail);
        mNameTextView = (TextView) headerView.findViewById(R.id.username);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("NoteLite");
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    private void initData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        String username = user.getUsername();
        String email = user.getEmail();
        String avatarStr = user.getAvatarStr();
        byte[] imageByteArray = avatarStr == null ? null : Base64.decode(avatarStr, Base64.DEFAULT);

        mNameTextView.setText(username);
        mEmailTextView.setText(email);
        Glide.with(this).load(imageByteArray).error(R.mipmap.ic_launcher).into(mNavCircleImageView);
    }

    private void initViewPager() {
        String[] titles = {"笔记列表", "今日天气", "微信精选", "美图欣赏"};
        mHomeFragmentAdapter = new FragmentApater(getSupportFragmentManager(), Arrays.asList(titles));
        mViewPager.setAdapter(mHomeFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager); // 将TabLayout和ViewPager关联起来。
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
    }

    private void initBroadcastReceiver() {

        mLogoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MainActivity.this.finish();
            }
        };
        IntentFilter filter = new IntentFilter(StaticUtil.LOGOUT_ACTION_NAME);

        registerReceiver(mLogoutReceiver, filter);
    }

    private void initRequestPermissions() {
        // 应用已启动加载就开始申请权限，包括文件读写（要为照片准备文件）和相机拍照权限，这里由检查文件读写这一项权限触发，来申请两项权限
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_image:
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                break;
            case R.id.list_note_add_fab:
                //Note note = new Note();
                //note.save();
                Intent intent = NoteDetailActivity.getIntent(MainActivity.this, -1);
                startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "你拒绝了一些权限，之后将不能进行相片操作", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLogoutReceiver);
    }
}


