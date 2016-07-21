package com.xy.lifemanage.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xy.lifemanage.MainActivity;
import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.myview.CircleImageView;
import com.xy.lifemanage.view.proview.CreateTaskView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by nemo on 2016/5/9 0009.
 */
public class LoginView extends Activity implements IUserView{

    private Presenter presenter;
    private Button button;
    private CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        presenter = new Presenter(getApplicationContext(),this);
        initView();
        UserBean userBean = BmobUser.getCurrentUser(LoginView.this,UserBean.class);
        App app = (App) getApplication();
        app.setUserBean(userBean);
        if(userBean!=null){
            setEditText(R.id.username,userBean.getUsername());
            Picasso.with(getApplicationContext()).load(userBean.getImg().getUrl()).into(imageView);
        }else{

        }
    }

    private void initView() {
        button = (Button) findViewById(R.id.login);
        imageView = (CircleImageView) findViewById(R.id.userHead);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = presenter.getText(R.id.username);
                String password = presenter.getText(R.id.password);
                if(username!=null&&password!=null) {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginView.this);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("登录中...");
                    progressDialog.show();
                    presenter.login(username, password, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            ToastUtils.toast(LoginView.this, "登录成功!");
                            Intent intent = new Intent(LoginView.this, MainView.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtils.toast(LoginView.this, s);
                        }
                    });
                }else{
                    ToastUtils.toast(getApplicationContext(),"请输入用户名和密码");
                }
            }
        });
        TopBar topBar = (TopBar) findViewById(R.id.topBarLogin);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
    }

    @Override
    public String getEditText(int id) {
        return ((TextView)findViewById(id)).getText().toString();
    }

    @Override
    public Bitmap getImageView(int id) {
        return ((ImageView)findViewById(id)).getDrawingCache();
    }

    @Override
    public void setEditText(int id, String str) {
        ((TextView)findViewById(id)).setText(str);
    }

    @Override
    public void setImageView(int id, Bitmap bitmap) {
        ((ImageView)findViewById(id)).setImageBitmap(bitmap);
    }

    @Override
    public void setListView(int id, List items) {

    }
}
