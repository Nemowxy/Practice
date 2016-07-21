package com.xy.lifemanage.view.proview;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.model.ISaveListener;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.utils.Utils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;
import com.xy.lifemanage.view.myview.MyViewGroup;
import com.xy.lifemanage.view.widget.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by nemo on 2016/5/16 0016.
 */
public class CreateOrgView extends AppCompatActivity implements IUserView{

    private TopBar topBar;
    private EditText title;
    private MyViewGroup powerViewGroup;
    private MyViewGroup classifyViewGroup;
    private TextView addpower;
    private TextView addclassify;
    private Map<String,Integer> lables;
    private String power;
    private ORBean orBean;
    private String imgPath;

    private Presenter presenter;
    private App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_org_main);
        presenter = new Presenter(getApplicationContext(),this);
        app = (App) getApplication();
        initData();
        initView();
    }

    private void initData() {
        lables = new HashMap<>();
        orBean = new ORBean();
    }


    private void initView() {

        title = (EditText) findViewById(R.id.or_create_org_name);

        topBar = (TopBar) findViewById(R.id.topBarORCreateOr);
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }
            @Override
            public void rightClick() {
                save();
            }
        });

        powerViewGroup = (MyViewGroup) findViewById(R.id.create_org_group_power);
        classifyViewGroup = (MyViewGroup) findViewById(R.id.create_org_group_classify);
        addpower = (TextView) findViewById(R.id.create_org_addlable_power);
        addpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(CreateOrgView.this).builder();
                dialog.setEdit("添加标签")
                        .setPositiveButtonForText("确认", new AlertDialog.OnAlertListener() {
                            @Override
                            public void onClick(String str,View v) {
                                addView(powerViewGroup,0,str);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        addclassify = (TextView) findViewById(R.id.create_org_addlable_classify);
        addclassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog(CreateOrgView.this).builder();
                dialog.setEdit("添加标签")
                        .setPositiveButtonForText("确认", new AlertDialog.OnAlertListener() {
                            @Override
                            public void onClick(String str,View v) {
                                addView(classifyViewGroup,1,str);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private void save() {
        if(presenter.getText(R.id.or_create_org_name).equals("")){
            ToastUtils.toast(getApplicationContext(),"信息有误！");
        }else {
            orBean.setOr_name(presenter.getText(R.id.or_create_org_name));
            orBean.setLables(lables);
            List<String> members = new ArrayList<String>();
            members.add(app.getUserBean().getUsername());
            orBean.setOr_member(members);
            BmobFile bmobFile = new BmobFile(new File(Utils.toFile(BitmapFactory.decodeResource(getResources(),R.mipmap.default_img)).getAbsolutePath()));
            orBean.setOr_image(bmobFile);
            final ProgressDialog progressDialog = new ProgressDialog(CreateOrgView.this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("发布中...");
            progressDialog.show();
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    presenter.dataModel.addOrganizeData(getApplicationContext(), orBean, new ISaveListener() {
                        @Override
                        public void onSuccess(Object objectId) {
                            UserBean userBean = app.getUserBean();
                            List<String> ors;
                            if (null == userBean.getOrganizeId()) {
                                ors = new ArrayList<String>();
                            } else {
                                ors = userBean.getOrganizeId();
                            }
                            ors.add((String) objectId);
                            userBean.setOrganizeId(ors);
                            presenter.dataModel.updateUser(getApplicationContext(), userBean, new ISaveListener() {
                                @Override
                                public void onSuccess(Object objectId) {
                                    progressDialog.dismiss();
                                    ToastUtils.toast(getApplicationContext(), "创建成功!");
                                }

                                @Override
                                public void onError(String msg) {
                                    ToastUtils.toast(getApplicationContext(), "创建失败!" + msg);
                                }
                            });
                            finish();
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.toast(getApplicationContext(), "创建失败!" + msg);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.toast(getApplicationContext(), "创建失败!" + s);
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                }
            });

        }
    }




    public void setPath(Intent data) {
        ContentResolver resolver = getContentResolver();
        Uri uri = data.getData();
        Bitmap bm;
        try {
            bm = MediaStore.Images.Media.getBitmap(resolver, uri);
            //这里开始的第二部分，获取图片的路径：
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = managedQuery(uri, proj, null, null, null);
            //按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径
            String path = cursor.getString(column_index);
            setImgPath(path);
            Bitmap bitmap = Bitmap.createScaledBitmap(bm, 200, 200, true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public String getCode(){
        Random random = new Random();
        String result="";

        for(int i=0;i<6;i++){
            result+=random.nextInt(10);
        }
        return result;
    }


    private void delete(MyViewGroup group,View v,String str) {
        group.removeView(v);
        lables.remove(str);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addView(final MyViewGroup viewGroup, int temp, String str) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = Utils.dip2px(CreateOrgView.this,10);
        TextView view = new TextView(this);
        view.setText(str);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        view.setTextColor(Color.WHITE);
        view.setBackground(getDrawable(R.drawable.button_shape_blue));
        view.setPadding(Utils.dip2px(CreateOrgView.this,8),Utils.dip2px(CreateOrgView.this,4),Utils.dip2px(CreateOrgView.this,8),Utils.dip2px(CreateOrgView.this,4));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog dialog = new AlertDialog(CreateOrgView.this).builder();
                dialog.setTitle("提示")
                        .setMsg("删除后别人的认可会丢失，确定要删除白色区域的标签吗？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete(viewGroup,view,((TextView)view).getText().toString());
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        if(temp==0){
            viewGroup.removeView(addpower);
            viewGroup.addView(view,lp);
            viewGroup.addView(addpower);
            power = str;
        }else{
            viewGroup.removeView(addclassify);
            viewGroup.addView(view,lp);
            viewGroup.addView(addclassify);
            lables.put(str,0);
        }
        ToastUtils.toast(getApplicationContext(),"添加成功！");
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
