package com.xy.lifemanage.view.proview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xy.lifemanage.R;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.itemview.ProItemView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class SearchOrgView extends AppCompatActivity implements IUserView{

    private TopBar topBar;
    private EditText code;
    private Button search;
    private Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_org_main);
        presenter = new Presenter(getApplicationContext(),this);
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.topBarOrSearch);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        code = (EditText) findViewById(R.id.search_org_edit);

        search = (Button) findViewById(R.id.search_org_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = code.getText().toString();
                if(!s.equals("")) {
                    final ProgressDialog progressDialog = new ProgressDialog(SearchOrgView.this);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage("登录中...");
                    progressDialog.show();
                    presenter.dataModel.searchOrForCode(getApplicationContext(), s, new ISaveListener() {
                        @Override
                        public void onSuccess(Object objectId) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProItemView.class);
                            intent.putExtra("id", (String) objectId);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.toast(getApplicationContext(),"查找失败，没有此小组!");
                        }
                    });
                }else{
                    ToastUtils.toast(getApplicationContext(),"请输入小组码");
                }
            }
        });
    }

    @Override
    public String getEditText(int id) {
        return null;
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
