package com.xy.lifemanage.logic;


		import java.io.File;
		import java.io.FileNotFoundException;
		import java.util.ArrayList;
		import java.util.HashMap;
		import java.util.List;


		import android.annotation.SuppressLint;
		import android.app.Activity;
		import android.content.Intent;
		import android.graphics.Bitmap;
		import android.net.Uri;
		import android.os.Bundle;
		import android.util.Log;
		import android.view.MotionEvent;
		import android.view.View;
		import android.view.View.OnClickListener;
		import android.view.View.OnTouchListener;
		import android.widget.AdapterView;
		import android.widget.AdapterView.OnItemClickListener;
		import android.widget.AdapterView.OnItemSelectedListener;
		import android.widget.Button;
		import android.widget.CheckBox;
		import android.widget.GridView;
		import android.widget.ImageView;
		import android.widget.LinearLayout;
		import android.widget.LinearLayout.LayoutParams;
		import android.widget.RelativeLayout;

		import com.xy.lifemanage.R;
		import com.xy.lifemanage.photo.utils.Bimp;
		import com.xy.lifemanage.photo.utils.ImageItem;
		import com.xy.lifemanage.view.MainView;
		import com.xy.lifemanage.view.proview.CreateProView;
		import com.xy.lifemanage.view.proview.CreateTaskView;
		import com.xy.lifemanage.view.proview.OrgDetailView;
		import com.xy.lifemanage.view.ring.RingShareMeView;
		import com.xy.lifemanage.view.ring.RingShareOrgView;

public class ImgsActivity extends Activity {

	Bundle bundle;
	FileTraversal fileTraversal;
	GridView imgGridView;
	ImgsAdapter imgsAdapter;
	LinearLayout select_layout;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
	Button choise_button;
	ArrayList<String> filelist;
	private String tag="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);

		imgGridView=(GridView) findViewById(R.id.gridView1);
		bundle= getIntent().getExtras();
		tag = bundle.getString("tag");
		fileTraversal=bundle.getParcelable("data");
		imgsAdapter=new ImgsAdapter(this, fileTraversal.filecontent,onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		select_layout=(LinearLayout) findViewById(R.id.selected_image_layout);
		relativeLayout2=(RelativeLayout) findViewById(R.id.relativeLayout2);
		choise_button=(Button) findViewById(R.id.button3);
		hashImage=new HashMap<Integer, ImageView>();
		filelist=new ArrayList<String>();
//		imgGridView.setOnItemClickListener(this);
		util=new Util(getApplicationContext());
	}

	class BottomImgIcon implements OnItemClickListener{

		int index;
		public BottomImgIcon(int index) {
			this.index=index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {

		}
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath,int index,CheckBox checkBox) throws FileNotFoundException{
		LinearLayout.LayoutParams params=new LayoutParams(relativeLayout2.getMeasuredHeight()-10, relativeLayout2.getMeasuredHeight()-10);
		ImageView imageView=new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.imgbg);
		float alpha=1.0f;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath,checkBox));
		return imageView;
	}

	ImgCallBack imgCallBack=new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
			ImageItem imageItem = new ImageItem();
			imageItem.setBitmap(bitmap);
			Bimp.tempSelectBitmap.add(imageItem);
		}
	};

	class ImgOnclick implements OnClickListener{
		String filepath;
		CheckBox checkBox;
		public ImgOnclick(String filepath,CheckBox checkBox) {
			this.filepath=filepath;
			this.checkBox=checkBox;
		}
		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			select_layout.removeView(arg0);
			choise_button.setText("已选择("+select_layout.getChildCount()+")张");
			filelist.remove(filepath);
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass=new ImgsAdapter.OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath=fileTraversal.filecontent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				select_layout.removeView(hashImage.get(Position));
				filelist.remove(filapath);
				choise_button.setText("已选择("+select_layout.getChildCount()+")张");
			}else {
				try {
					checkBox.setChecked(true);
					Log.i("img", "img choise position->"+Position);
					ImageView imageView=iconImage(filapath, Position,checkBox);
					if (imageView !=null) {
						hashImage.put(Position, imageView);
						filelist.add(filapath);
						select_layout.addView(imageView);
						choise_button.setText("已选择("+select_layout.getChildCount()+")张");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void tobreak(View view){
		finish();
	}

	/**
	 * FIXME
	 * 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
	 * @param view
	 */
	public void sendfiles(View view){
		Intent intent = new Intent();
		Bundle bundle=new Bundle();
		System.out.println("tag--"+tag);
		switch (tag){
			case "shareme":
				intent.setClass(this, RingShareMeView.class);
				bundle.putStringArrayList("files", filelist);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case "shareorg":
				intent.setClass(this, RingShareOrgView.class);
				bundle.putStringArrayList("files", filelist);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case "createtask":
				intent.setClass(this, CreateTaskView.class);
				bundle.putStringArrayList("files", filelist);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case "createpro":
				intent.setClass(this, CreateProView.class);
				bundle.putStringArrayList("files", filelist);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case "my":
				intent.setClass(this, MainView.class);
				MainView.flag=true;
				bundle.putString("id","1");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case "ordetail":
				intent.setClass(this, OrgDetailView.class);
				bundle.putStringArrayList("files", filelist);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
		}
	}
}
