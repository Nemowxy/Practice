package com.nemo.joke.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nemo.joke.R;
import com.nemo.joke.utils.Utils;


public class TopBar extends RelativeLayout {
	private Button leftButton;
	public Button rightButton;
	private TextView title;

	private String leftText;
	private float leftTextSize;
	private int leftTextColor;
	private Drawable leftBackground;

	private String rightText;
	private float rightTextSize;
	private int rightTextColor;
	private Drawable rightBackground;

	private String titleText;
	private float titleTextSize;
	private int titleTextColor;

	private LayoutParams leftParams, rightParams, titleParams;
	
	public interface topBarOnclickListener{
		public void leftClick();
		public void rightClick();
	}

	private topBarOnclickListener listener;

	public void setTitle(String string){
		title.setText(string);
	}
	
	public void setTopBarOnclickListener(topBarOnclickListener listener){
		this.listener = listener;
	}
	
	@SuppressLint("NewApi")
	public TopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.MyTopBar);
		leftTextColor = ta.getColor(R.styleable.MyTopBar_leftTextColor, 0);
		leftBackground = ta.getDrawable(R.styleable.MyTopBar_leftBackground);
		leftText = ta.getString(R.styleable.MyTopBar_leftText);
		leftTextSize = ta.getDimension(R.styleable.MyTopBar_leftTextSize, 0);

		rightTextColor = ta.getColor(R.styleable.MyTopBar_rightTextColor, 0);
		rightBackground = ta.getDrawable(R.styleable.MyTopBar_rightBackground);
		rightText = ta.getString(R.styleable.MyTopBar_rightText);
		rightTextSize = ta.getDimension(R.styleable.MyTopBar_rightTextSize, 0);

		titleText = ta.getString(R.styleable.MyTopBar_titleText);
		titleTextColor = ta.getColor(R.styleable.MyTopBar_mtitleTextColor, 0);
		titleTextSize = ta.getDimension(R.styleable.MyTopBar_titleTextSize, 0);

		ta.recycle();
		
		leftButton = new Button(context);
		rightButton = new Button(context);
		title = new TextView(context);
		leftButton.setText(leftText);
		leftButton.setTextColor(leftTextColor);
		leftButton.setTextSize(leftTextSize);
		leftButton.setBackground(leftBackground);
		if(leftTextSize!=0)
			leftButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);

		rightButton.setText(rightText);
		rightButton.setTextColor(rightTextColor);
		rightButton.setTextSize(rightTextSize);
		if(rightTextSize!=0)
			rightButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
		rightButton.setBackground(rightBackground);

		title.setText(titleText);
		title.setTextColor(titleTextColor);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
		title.setGravity(Gravity.CENTER);
		if(leftText==null){
			leftParams = new LayoutParams(Utils.dip2px(context,32),
					Utils.dip2px(context,28));
			leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
			leftParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
			leftParams.leftMargin=Utils.dip2px(context,10);
			addView(leftButton, leftParams);
		}else{
			leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
			leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
			addView(leftButton, leftParams);
		}

		if(rightText==null) {
			rightParams = new LayoutParams(Utils.dip2px(context, 25),
					Utils.dip2px(context, 25));
			rightParams.rightMargin = Utils.dip2px(context, 10);
			rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
			rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
			addView(rightButton, rightParams);
		}else {
			rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
			rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
			addView(rightButton, rightParams);
		}

		titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

		addView(title, titleParams);
		if(leftButton!=null)
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				listener.leftClick();
			}
		});
		if(rightButton!=null)
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.rightClick();
			}
		});
	}
}
