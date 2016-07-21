package com.xy.lifemanage.view;

import android.app.Activity;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemo on 2016/5/9 0009.
 */
public interface IUserView {

    String getEditText(int id);
    Bitmap getImageView(int id);
    void setEditText(int id,String str);
    void setImageView(int id,Bitmap bitmap);
    void setListView(int id,List items);
}
