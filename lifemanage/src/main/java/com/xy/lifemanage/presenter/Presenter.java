package com.xy.lifemanage.presenter;

import android.app.Activity;
import android.content.Context;

import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.DataModel;
import com.xy.lifemanage.model.IGetDataListener;
import com.xy.lifemanage.model.UserModel;
import com.xy.lifemanage.view.IUserView;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by nemo on 2016/5/8 0008.
 */
public class Presenter{
    public UserModel userModel;
    public DataModel dataModel;
    public IUserView iUserView;
    private Context context;
    private Activity activity;
    public static Presenter presenter;
    public Presenter(Context context,IUserView activity){
        this.context = context;
        userModel = new UserModel();
        dataModel = new DataModel();
        iUserView = activity;
        presenter = this;
    }

    public Presenter(Context context){
        this.context = context;
        userModel = new UserModel();
        dataModel = new DataModel();
        presenter = this;
    }

    public String getText(int id){
        return iUserView.getEditText(id);
    }

    public void getORData(UserBean userBean,IGetDataListener listener){
        dataModel.getUserOrganizeData(context,userBean,listener);
    }

    public void getRingData(IGetDataListener listener){
        dataModel.getRingData(context,listener);
    }

    public void login(String username, String password, SaveListener saveListener){
        userModel.login(context,username,password,saveListener);
    }
    public void register(String username, String password, String email,SaveListener saveListener){
        userModel.register(context,username,password,email,saveListener);
    }

    public void getCoding(String email,SaveListener listener){
        userModel.getCoding(context,email,listener);
    }
}
