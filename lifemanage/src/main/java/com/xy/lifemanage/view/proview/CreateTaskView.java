package com.xy.lifemanage.view.proview;


import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.TaskBean;
import com.xy.lifemanage.bean.UserBean;
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
import com.xy.lifemanage.view.ring.RingChooseView;
import com.xy.lifemanage.view.wheelview.OnWheelScrollListener;
import com.xy.lifemanage.view.wheelview.WheelView;
import com.xy.lifemanage.view.wheelview.adapter.ArrayWheelAdapter;
import com.xy.lifemanage.view.wheelview.adapter.NumericWheelAdapter;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

public class CreateTaskView extends Activity implements View.OnClickListener, IUserView {

    public static final int REQUEST_CHOOSE = 0x00005;

    private LayoutInflater inflater = null;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView time;
    private WheelView min;
    private WheelView sec;

    private int mYear = 2016;

    LinearLayout ll;

    View view = null;

    private TextView or_create_task_finishtime;
    private Button or_create_task_timer;
    private EditText or_create_task_detail;
    private TextView title;
    private TextView receive;
    private RelativeLayout receiver;
    private TopBar topBar;
    private GridView create_task_noScrollgridview;
    private GridAdapter adapter;
    public static Bitmap bimap;
    private View parentView;
    private PopupMenu pm_bt_xuanze = null;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static String path;
    public String receiverName = "";
    private Presenter presenter;
    private UserBean userBean;
    private App app;
    private String objectId;
    private ORBean thisOrbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_main);
        Intent intent = getIntent();
        objectId = intent.getStringExtra("id");
        app = (App) getApplication();
        userBean = app.getUserBean();
        presenter = new Presenter(getApplicationContext(), this);
        bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.create_task_main, null);
        setContentView(parentView);
        BmobQuery<ORBean> query = new BmobQuery<>();
        query.getObject(getApplicationContext(), objectId, new GetListener<ORBean>() {
            @Override
            public void onSuccess(ORBean orBean) {
                thisOrbean = orBean;


                findID();
                setListener();
                init();
                initView();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

    private void initView() {
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        ll = (LinearLayout) findViewById(R.id.ll);
        or_create_task_finishtime = (TextView) findViewById(R.id.or_create_task_finishtime);
        or_create_task_timer = (Button) findViewById(R.id.or_create_task_timer);
        or_create_task_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll.getChildCount() != 0) {
                    ll.removeAllViews();
                } else {
                    ll.addView(getDataPick());
                }
            }
        });
    }

    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        int curYear = mYear;

        view = inflater.inflate(R.layout.wheel_date_picker, null);

        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this, 1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        day.addScrollingListener(scrollListener);
        day.setCyclic(true);

        time = (WheelView) view.findViewById(R.id.time);
        String[] times = {"上午", "下午"};
        ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(CreateTaskView.this, times);
        time.setViewAdapter(arrayWheelAdapter);
        time.setCyclic(true);
        time.addScrollingListener(scrollListener);

        min = (WheelView) view.findViewById(R.id.min);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(this, 1, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        min.setViewAdapter(numericWheelAdapter3);
        min.setCyclic(true);
        min.addScrollingListener(scrollListener);

        sec = (WheelView) view.findViewById(R.id.sec);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(this, 1, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        sec.setViewAdapter(numericWheelAdapter4);
        sec.setCyclic(true);
        sec.addScrollingListener(scrollListener);


        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);
        time.setVisibleItems(7);
        min.setVisibleItems(7);
        sec.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate);
        time.setCurrentItem(curDate);
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
//            int n_year = year.getCurrentItem() + 1950;//年
//            int n_month = month.getCurrentItem() + 1;//月
//            initDay(n_year,n_month);
            or_create_task_finishtime.setText((day.getCurrentItem() + 1) + "天" + (min.getCurrentItem() + 1) + "时");
        }
    };

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }


    public void init() {

        pop = new PopupWindow(CreateTaskView.this);
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
                intent.setClass(CreateTaskView.this, ImgFileListActivity.class);
                intent.putExtra("tag", "createtask");
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

        create_task_noScrollgridview = (GridView) findViewById(R.id.create_task_noScrollgridview);
        create_task_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        create_task_noScrollgridview.setAdapter(adapter);
        create_task_noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(CreateTaskView.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(CreateTaskView.this, ImgFileListActivity.class);
                    intent.putExtra("tag", "createtask");
                    startActivity(intent);
                }
            }
        });
        Button button = (Button) findViewById(R.id.create_task_addfujian);
        button.setOnClickListener(this);
    }

    private void findID() {
        topBar = (TopBar) findViewById(R.id.topBarORCreateTask);
        create_task_noScrollgridview = (GridView) findViewById(R.id.create_task_noScrollgridview);
        create_task_noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        receiver = (RelativeLayout) findViewById(R.id.create_task_addreceiver);
        receiver.setOnClickListener(this);
        receive = (TextView) findViewById(R.id.or_create_task_receiver);
        title = (TextView) findViewById(R.id.or_create_task_title);
        title.setText(thisOrbean.getOr_name());
        or_create_task_detail = (EditText) findViewById(R.id.or_create_task_detail);
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

    public void setListener() {
        topBar.setTopBarOnclickListener(new TopBar.topBarOnclickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                ToastUtils.dialog(CreateTaskView.this, "提示", "确定创建？", new DialogListener() {
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
        String detail = or_create_task_detail.getText().toString();
        String finishTime = or_create_task_finishtime.getText().toString();
        if (receiverName.equals("")) {
            ToastUtils.toast(getApplicationContext(), "未选择接收人");
        }else if(detail.equals("")||finishTime.equals("完成时间")){
            ToastUtils.toast(getApplicationContext(),"信息有误！");
        }
        else {
            final TaskBean taskBean = new TaskBean();
            taskBean.setTitle(thisOrbean.getOr_name());
            taskBean.setDetail(detail);
            taskBean.setDate(finishTime);
            taskBean.setHasFinish(false);
            taskBean.setHasFujian(false);
            taskBean.setSendName(userBean.getUsername());
            taskBean.setReceiverName(receiverName);
            final ProgressDialog progressDialog = new ProgressDialog(CreateTaskView.this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("创建中...");
            progressDialog.show();
            presenter.dataModel.addTaskDataToOrg(getApplicationContext(), taskBean, imgPathList, objectId, new SaveListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                    ToastUtils.toast(getApplicationContext(), "创建成功!");
                    Bimp.tempSelectBitmap.clear();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_task_addfujian:
                create_task_noScrollgridview.setVisibility(View.VISIBLE);
                break;
            case R.id.create_task_addreceiver:
                Intent intent = new Intent(getApplicationContext(), ChooseReceiverView.class);
                intent.putExtra("id", objectId);
                startActivityForResult(intent, REQUEST_CHOOSE);
                break;
        }
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
            case REQUEST_CHOOSE:
                String s = data.getStringExtra("username");
                receiverName = s;
                receive.setText(s);
                break;
        }
    }
}
