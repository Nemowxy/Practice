package com.xy.lifemanage.view.ring;

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
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.xy.lifemanage.R;
import com.xy.lifemanage.app.App;
import com.xy.lifemanage.bean.RingBean;
import com.xy.lifemanage.logic.ImgFileListActivity;
import com.xy.lifemanage.model.DialogListener;
import com.xy.lifemanage.photo.utils.Bimp;
import com.xy.lifemanage.photo.utils.FileUtils;
import com.xy.lifemanage.photo.utils.ImageItem;
import com.xy.lifemanage.photo.utils.Res;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;

import java.util.ArrayList;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jianfang on 2016/5/12.
 */
public class RingShareMeView extends Activity implements View.OnClickListener {

    private EditText ring_shareme_et_content;
    private TopBar topBar;
    private GridView ring_shareme_noScrollgridview;
    private GridAdapter adapter;
    public Bitmap bimap;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private App app;
    private Presenter presenter;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                Toast.makeText(RingShareMeView.this, "上传成功", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.ring_shareme_main);
        app = (App) getApplication();
        presenter = new Presenter(getApplicationContext());
        // Res.init(getApplicationContext());
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.ring_shareme_main, null);
        setContentView(parentView);
        findID();
        setListener();
        init();
    }


    public void init() {

        pop = new PopupWindow(RingShareMeView.this);
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
                intent.setClass(RingShareMeView.this, ImgFileListActivity.class);
                intent.putExtra("tag", "shareme");
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

        ring_shareme_noScrollgridview = (GridView) findViewById(R.id.ring_shareme_noScrollgridview);
        ring_shareme_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        ring_shareme_noScrollgridview.setAdapter(adapter);
        ring_shareme_noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(RingShareMeView.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(RingShareMeView.this, ImgFileListActivity.class);
                    intent.putExtra("tag", "shareme");
                    startActivity(intent);
                }
            }
        });
    }

    private void findID() {
        ring_shareme_et_content = (EditText) findViewById(R.id.ring_shareme_et_content);
        topBar = (TopBar) findViewById(R.id.topBar_ring_shareme);
        ring_shareme_noScrollgridview = (GridView) findViewById(R.id.ring_shareme_noScrollgridview);
        ring_shareme_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    /**
     * imgPathList表示选中的几张图片的路径
     * onRestart用来接收Bimp.tempSelectBitmap(图片路径);更新数据
     */
    ArrayList<String> imgPathList;


    public void setListener() {
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                ToastUtils.dialog(RingShareMeView.this, "提示", "确定发布？", new DialogListener() {
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

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getStringArrayList("files") != null) {
                imgPathList = bundle.getStringArrayList("files");
            }
        }
        init();
        adapter.update();
        super.onNewIntent(intent);
    }

    public void uploadData() {
        String content = ring_shareme_et_content.getText().toString();
        if (content.equals("")) {
            ToastUtils.toast(getApplicationContext(), "内容为空！");
        } else {
            RingBean ringBean = new RingBean();
            ringBean.setRingName(app.getUserBean().getUsername());
            ringBean.setRingDetail(content);
            ringBean.setRingLables(app.getUserBean().getLables());
            ringBean.setRingImg(app.getUserBean().getImg());
            ringBean.setOrg(Boolean.FALSE);
            final ProgressDialog progressDialog = new ProgressDialog(RingShareMeView.this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("发布中...");
            progressDialog.show();
            presenter.dataModel.addRingUser(getApplicationContext(), ringBean, imgPathList, new SaveListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "发布成功!");
                    RingShareMeView.this.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "发布失败!" + s);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
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

        public void loading() {
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

    @Override
    public void finish() {
        Bimp.tempSelectBitmap.clear();
        super.finish();
    }
}
