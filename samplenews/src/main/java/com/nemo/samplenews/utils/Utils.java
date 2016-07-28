package com.nemo.samplenews.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class Utils {

    private static int START_YEAR = 1990, END_YEAR = 2100;


    public static ProgressBar createProgressBar(Activity activity, Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    // 判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static File toFile(Bitmap bitmap){
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        int quality = 100;
        File file = new File(Environment.getExternalStorageDirectory()+"/"+"default.png");
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bitmap.compress(format, quality, stream);
        System.out.println("file:"+file.exists()+file.getAbsolutePath());
        return file;
    }
}
