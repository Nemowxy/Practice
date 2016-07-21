package com.xy.lifemanage.view.ring;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.TextView;

import com.xy.lifemanage.R;
import com.xy.lifemanage.bean.RingBean;
import com.xy.lifemanage.logic.ImgFileListActivity;
import com.xy.lifemanage.model.DialogListener;
import com.xy.lifemanage.photo.utils.Bimp;
import com.xy.lifemanage.photo.utils.FileUtils;
import com.xy.lifemanage.photo.utils.ImageItem;
import com.xy.lifemanage.presenter.Presenter;
import com.xy.lifemanage.utils.ToastUtils;
import com.xy.lifemanage.view.TopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jianfang on 2016/5/13.
 */
public class RingShareOrgView extends Activity {

    public final static int MY_REQUEST = 0x000002;
    public final static int MY_REQUEST_LABLE = 0x000003;
    private TopBar topBar;
    private EditText ring_shareorg_et_content;
    private RelativeLayout ring_shareorg_layout_chooseOrg;
    private RelativeLayout ring_shareorg_layout_chooseRecruit;
    private TextView ring_shareorg_orgname;
    private TextView ring_shareorg_lable;
    private GridView ring_shareorg_noScrollgridview;
    private GridAdapter adapter;
    private ProgressDialog progressDialog;
    public static Bitmap bimap;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static String path;
    private Map<String,Integer> lables = new HashMap<>();
    private Presenter presenter;
    private String objectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_shareorg_main);
        presenter = new Presenter(getApplicationContext());
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.ring_shareorg_main, null);
        setContentView(parentView);

        findID();
        setListener();
        init();
    }

    public void uploadData() {
        String name = ring_shareorg_orgname.getText().toString();
        String content = ring_shareorg_et_content.getText().toString();
        if(name.equals("选择小组")||content.equals("")){
            ToastUtils.toast(getApplication(),"信息有误！");
        }else {
            RingBean ringBean = new RingBean();
            ringBean.setRingName(name);
            ringBean.setRingDetail(content);
            ringBean.setRingLables(lables);
            ringBean.setOrg(Boolean.TRUE);
            final ProgressDialog progressDialog = new ProgressDialog(RingShareOrgView.this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("发布中...");
            progressDialog.show();
            presenter.dataModel.addRing(getApplicationContext(), ringBean, imgPathList, objectId, new SaveListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "发布成功!");
                    finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "发布失败!" + s);
                }
            });
        }
    }

    public void init() {

        pop = new PopupWindow(RingShareOrgView.this);
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
                intent.setClass(RingShareOrgView.this,ImgFileListActivity.class);
                intent.putExtra("tag","shareorg");
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

        ring_shareorg_noScrollgridview = (GridView) findViewById(R.id.ring_shareorg_noScrollgridview);
        ring_shareorg_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        ring_shareorg_noScrollgridview.setAdapter(adapter);
        ring_shareorg_noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(RingShareOrgView.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(RingShareOrgView.this,ImgFileListActivity.class);
                    intent.putExtra("tag","shareorg");
                    startActivity(intent);
                }
            }
        });

        ring_shareorg_layout_chooseOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingShareOrgView.this, RingChooseView.class);
                startActivityForResult(intent, MY_REQUEST);
            }
        });
        ring_shareorg_layout_chooseRecruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingShareOrgView.this, RingShareLable.class);
                startActivityForResult(intent, MY_REQUEST_LABLE);
            }
        });
    }

    private void findID() {
        ring_shareorg_lable = (TextView) findViewById(R.id.ring_shareorg_orglable);
        ring_shareorg_et_content = (EditText) findViewById(R.id.ring_shareorg_et_content);
        ring_shareorg_layout_chooseOrg = (RelativeLayout) findViewById(R.id.ring_shareorg_layout_chooseOrg);
        ring_shareorg_layout_chooseRecruit = (RelativeLayout) findViewById(R.id.ring_shareorg_layout_chooselable);
        ring_shareorg_orgname = (TextView) findViewById(R.id.ring_shareorg_orgname);
        topBar = (TopBar) findViewById(R.id.topBar_ring_shareorg);
        ring_shareorg_noScrollgridview = (GridView) findViewById(R.id.ring_shareorg_noScrollgridview);
        ring_shareorg_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }


    /**
     * imgPathList表示选中的几张图片的路径
     * onRestart用来接收Bimp.tempSelectBitmap(图片路径);更新数据
     */
    ArrayList<String> imgPathList;
    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle= intent.getExtras();
        if (bundle!=null) {
            if (bundle.getStringArrayList("files")!=null) {
                imgPathList= bundle.getStringArrayList("files");
                System.out.println("imp---------"+imgPathList);
            }
        }
        adapter.update();
        super.onNewIntent(intent);
    }

    public void setListener() {
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                ToastUtils.dialog(RingShareOrgView.this, "提示", "确定发布？", new DialogListener() {
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

    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public void update() {
            adapter.notifyDataSetChanged();
        }
        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
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
            case MY_REQUEST_LABLE:
                List<String> lable = data.getStringArrayListExtra("lable");
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < lable.size(); i++) {
                    sb.append(lable.get(i)+" ");
                    lables.put(lable.get(i),0);
                }
                ring_shareorg_lable.setText(sb.toString());
                break;
            case MY_REQUEST:
                String str = data.getStringExtra("name");
                String[] s = str.split(",");
                ring_shareorg_orgname.setText(s[0]);
                objectId = s[1];
                break;
        }
    }
    @Override
    public void finish() {
        super.finish();
        Bimp.tempSelectBitmap.clear();
    }
}
