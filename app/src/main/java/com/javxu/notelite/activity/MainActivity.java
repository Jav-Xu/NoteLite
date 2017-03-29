package com.javxu.notelite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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

import com.bumptech.glide.Glide;
import com.javxu.notelite.R;
import com.javxu.notelite.adapter.HomeFragmentApater;
import com.javxu.notelite.bean.MyUser;
import com.javxu.notelite.bean.Note;

import java.util.Arrays;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton fab;

    private CircleImageView mNavCircleImageView;
    private TextView mEmailTextView;
    private TextView mNameTextView;
    private HomeFragmentApater mHomeFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        initViewPager();
        initData();
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
                    case R.id.nav_user:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                        break;
                    case R.id.nav_share:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        mDrawerLayout.closeDrawers();
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

    private void initViewPager() {
        String[] titles = {"笔记列表", "今日天气", "微信精选", "美图欣赏"};
        mHomeFragmentAdapter = new HomeFragmentApater(getSupportFragmentManager(), Arrays.asList(titles));
        mViewPager.setAdapter(mHomeFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager); // 将TabLayout和ViewPager关联起来。
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
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
                Note note = new Note();
                note.save();
                Intent intent = NoteDetailActivity.getIntent(MainActivity.this, note.getId());
                startActivity(intent);
        }
    }
}


