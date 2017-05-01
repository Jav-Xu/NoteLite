package com.javxu.notelite.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.javxu.notelite.subfragment.GalleryFragment;
import com.javxu.notelite.subfragment.NoteListFragment;
import com.javxu.notelite.subfragment.ToolsFragment;
import com.javxu.notelite.subfragment.WeChatFragment;

import java.util.List;

/**
 * Created by Jav-Xu on 2016/12/30.
 */

public class FragmentApater extends FragmentStatePagerAdapter {

    private List<String> mTitles;

    public FragmentApater(FragmentManager fm, List<String> mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new NoteListFragment();
                break;
            case 1:
                fragment = new WeChatFragment();
                break;
            case 2:
                fragment = new GalleryFragment();
                break;
            case 3:
                fragment = new ToolsFragment();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
