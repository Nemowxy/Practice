package com.xy.lifemanage.view.proview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.ProjectBean;
import com.xy.lifemanage.logic.ImgFileListActivity;
import com.xy.lifemanage.model.DialogListener;
import com.xy.lifemanage.photo.utils.Bimp;
import com.xy.lifemanage.photo.utils.FileUtils;
import com.xy.lifemanage.photo.utils.ImageItem;
import com.xy.lifemanage.photo.utils.PublicWay;
import com.xy.lifemanage.photo.utils.Res;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.IUserView;
import com.xy.lifemanage.view.TopBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by nemo on 2016/5/12.
 */
public class CreateProView extends Activity implements View.OnClickListener, IUserView {

    private EditText ring_shareme_et_content;
    private TextView ring_shareme_tv_choose;
    private TopBar topBar;
    private GridView ring_sharepro_noScrollgridview;
    private GridAdapter adapter;
    private ProgressDialog progressDialog;
    public static Bitmap bimap;
    private View parentView;
    private PopupMenu pm_bt_xuanze = null;
    public String popMenu_selected;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static String path;
    private EditText or_create_pro_detail;
    private EditText or_create_pro_theme;
    private TextView or_create_pro_name;
    private App app;
    private Presenter presenter;
    private String objectId;
    private String proname;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                progressDialog.dismiss();
                Toast.makeText(CreateProView.this, "上传成功", Toast.LENGTH_SHORT).show();
                Bimp.tempSelectBitmap.clear();
                finish();
            }
            if (msg.what == 1) {
                adapter.notifyDataSetChanged();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_pro_main);
        app = (App) getApplication();
        presenter = new Presenter(getApplicationContext(), this);
        Intent intent = getIntent();
        objectId = intent.getStringExtra("id");
        proname = intent.getStringExtra("name");
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.create_pro_main, null);
        setContentView(parentView);

        findID();
        setListener();
        init();
    }

    public void init() {

        pop = new PopupWindow(CreateProView.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CreateProView.this, ImgFileListActivity.class);
                intent.putExtra("tag", "createpro");
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        ring_sharepro_noScrollgridview = (GridView) findViewById(R.id.ring_sharepro_noScrollgridview);
        ring_sharepro_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        ring_sharepro_noScrollgridview.setAdapter(adapter);
        ring_sharepro_noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(CreateProView.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(CreateProView.this, ImgFileListActivity.class);
                    intent.putExtra("tag", "createpro");
                    startActivity(intent);
                }
            }
        });
        Button button = (Button) findViewById(R.id.or_create_pro_addfujian);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ring_sharepro_noScrollgridview.setVisibility(View.VISIBLE);
            }
        });
    }

    private void findID() {
        ring_shareme_et_content = (EditText) findViewById(R.id.ring_shareme_et_content);
        topBar = (TopBar) findViewById(R.id.topBarORCreatePro);
        ring_sharepro_noScrollgridview = (GridView) findViewById(R.id.ring_sharepro_noScrollgridview);
        ring_sharepro_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        or_create_pro_name = (TextView) findViewById(R.id.or_create_pro_name);
        or_create_pro_detail = (EditText) findViewById(R.id.or_create_pro_detail);
        or_create_pro_theme = (EditText) findViewById(R.id.or_create_pro_theme);
    }


    /**
     * imgPathList表示选中的几张图片的路径
     * onRestart用来接收Bimp.tempSelectBitmap(图片路径);更新数据
     */
    ArrayList<String> imgPathList;

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getStringArrayList("files") != null) {
                imgPathList = bundle.getStringArrayList("files");
            }
        }
        adapter.update();
        super.onNewIntent(intent);
    }

    public ArrayList<String> getImgPathList() {
        return imgPathList;
    }

    public void setImgPathList(ArrayList<String> imgPathList) {
        this.imgPathList = imgPathList;
    }

    public void setListener() {
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                ToastUtils.dialog(CreateProView.this, "提示", "确定创建？", new DialogListener() {
                    @Override
                    public void clickOK() {
                        uploadData();
                    }

                    @Override
                    public void clickCancle() {

                    }
                });
            }
        });
    }

    public void uploadData() {
        String detail = or_create_pro_detail.getText().toString();
        String theme = or_create_pro_theme.getText().toString();
        or_create_pro_name.setText(proname);
        if(detail.equals("")||theme.equals("")){
            ToastUtils.toast(getApplicationContext(),"信息有误！");
        }else {
            ProjectBean projectBean = new ProjectBean();
            projectBean.setTitle(theme);
            projectBean.setDetail(detail);
            projectBean.setAuthor(app.getUserBean().getUsername());
            if (imgPathList == null || imgPathList.size() == 0)
                projectBean.setHasFujian(Boolean.FALSE);
            else
                projectBean.setHasFujian(Boolean.TRUE);
            final ProgressDialog progressDialog = new ProgressDialog(CreateProView.this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("创建中...");
            progressDialog.show();
            presenter.dataModel.addProjectDataToOrg(getApplicationContext(), projectBean, imgPathList, objectId, new SaveListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "创建成功!");
                    finish();
                }
                @Override
                public void onFailure(int i, String s) {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "创建失败!" + s);
                }
            });
        }
    }

    public void setPath(String p) {
        path = p;
    }

    public static String getPath() {
        return path;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void finish() {
        super.finish();
        Bimp.tempSelectBitmap.clear();
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


    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }
}
