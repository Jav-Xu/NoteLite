package com.javxu.notelite.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.javxu.notelite.R;

import java.io.File;
import java.util.UUID;

/**
 * Created by Jav-Xu on 2016/12/20.
 */

public class Utils {

    private Context mContext;

    public Utils(Context context) {
        mContext = context;
    }

    public static File getExternalFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public void loadImage(String s, ImageView view) {
        Glide.with(mContext).load(s).signature(new StringSignature(UUID.randomUUID().toString()))
                .placeholder(R.mipmap.ic_launcher).into(view);
    }
}
