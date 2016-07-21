package com.xy.lifemanage.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.xy.lifemanage.R;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.TaskBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.utils.Utils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by nemo on 2016/5/8 0008.
 */
public class UserModel implements IUserModel{

    @Override
    public void login(Context context, String username, String password, final SaveListener listener) {
        UserBean ub = new UserBean();
        ub.setUsername(username);
        ub.setPassword(password);
        ub.login(context, new SaveListener(){
            @Override
            public void onSuccess() {
                listener.onSuccess();
                //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                listener.onFailure(code,msg);
            }
        });
    }

    @Override
    public void register(final Context context, String username, String password, String email, final SaveListener listener) {
        final UserBean ub = new UserBean();
        ub.setUsername(username);
        ub.setPassword(password);
        ub.setEmail(email);
        final BmobFile bmobFile = new BmobFile(new File(Utils.toFile(BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_img)).getAbsolutePath()));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                ub.setImg(bmobFile);
                ub.signUp(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        listener.onSuccess();
                        //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        listener.onFailure(code, msg);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i,s);
            }
        });

    }

    @Override
    public void getCoding(Context context, final String email, final SaveListener saveListener) {
        UserBean.requestEmailVerify(context, email, new EmailVerifyListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                saveListener.onSuccess();
            }
            @Override
            public void onFailure(int code, String e) {
                // TODO Auto-generated method stub
                saveListener.onFailure(code,e);
            }
        });
    }

    public void updateUser(Context context, UserBean user, final SaveListener saveListener){
        UserBean newUser = user;
        UserBean bmobUser = BmobUser.getCurrentUser(context,UserBean.class);
        newUser.update(context,bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                saveListener.onSuccess();
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                saveListener.onFailure(code,msg);
            }
        });
    }

}
interface IUserModel{
    public void login(Context context,String username,String password,SaveListener saveListener);
    public void register(Context context,String username,String password,String email,SaveListener saveListener);
    public void getCoding(Context context,String email,SaveListener saveListener);
}