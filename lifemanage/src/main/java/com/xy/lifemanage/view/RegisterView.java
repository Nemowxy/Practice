package com.xy.lifemanage.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xy.lifemanage.R;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.DialogListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by nemo on 2016/5/9 0009.
 */
public class RegisterView extends Activity implements IUserView {

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        presenter = new Presenter(getApplicationContext(), this);
        initView();
    }

    private void initView() {
        TopBar topBar = (TopBar) findViewById(R.id.regist_topBar);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }
            @Override
            public void rightClick() {

            }
        });
        Button register_ok = (Button) findViewById(R.id.register_ok);
        register_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = presenter.getText(R.id.regist_name);
                final String email = presenter.getText(R.id.regist_email);
                final String psw = presenter.getText(R.id.regist_password_first);
                final String psw2 = presenter.getText(R.id.regist_password_second);

                if ((!psw.equals(psw2)) || psw.equals("") || psw2.equals("")) {
                    ToastUtils.toast(getApplicationContext(), "两次密码不一致，请重新输入!");
                } else if(psw.length()<6){
                    ToastUtils.toast(getApplicationContext(), "请至少输入六位密码！");
                }
                else if(username.equals("")||email.equals("")){
                    ToastUtils.toast(getApplicationContext(), "请填写完整信息！");
                }
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(RegisterView.this);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("注册中...");
                    progressDialog.show();
                    presenter.register(username, psw, email, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            ToastUtils.dialog(RegisterView.this, "提示", "注册成功，前往登录？", new DialogListener() {
                                @Override
                                public void clickOK() {
                                    Intent intent = new Intent(RegisterView.this, LoginView.class);
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void clickCancle() {

                                }
                            });
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            progressDialog.dismiss();
                            ToastUtils.toast(getApplicationContext(), "注册失败！"+s);
                        }
                    });
                }
            }
        });
    }

    @Override
    public String getEditText(int id) {
        return ((EditText)findViewById(id)).getText().toString();
    }

    @Override
    public Bitmap getImageView(int id) {
        return null;
    }

    @Override
    public void setEditText(int id, String str) {

    }

    @Override
    public void setImageView(int id, Bitmap bitmap) {

    }

    @Override
    public void setListView(int id, List items) {

    }
}
