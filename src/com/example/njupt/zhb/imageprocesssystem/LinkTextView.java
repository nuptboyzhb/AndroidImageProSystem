package com.example.njupt.zhb.imageprocesssystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.AttributeSet;
/*
 *@author: ZhengHaibo  
 *web:     blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2012-8-31  Nanjing njupt
 */
public class LinkTextView extends View implements OnClickListener{
    String UrlText;
    Activity ParentActivity;
    Paint paint=new Paint();
    int color=Color.RED;
	public LinkTextView(Context context) {
		super(context);
		setClickable(true);
		this.setOnClickListener(this);
		// TODO Auto-generated constructor stub
	}
	public LinkTextView(Context context,AttributeSet attrs){
		super(context,attrs);
		setClickable(true);
		this.setOnClickListener(this);
	}
	//@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		//canvas.drawLine(left, bottom, left+100, bottom, paint);
		int width=getWidth();
		int height=getHeight();
		int textwidth=getTextWidth();
		int x_pos=(int)((width-textwidth)/2);
		height=(int)(height*0.67);
		canvas.drawText(UrlText, x_pos, height, paint);
		paint.setStrokeWidth(1);
		paint.setColor(Color.BLUE);
		canvas.drawLine(x_pos, height, textwidth+x_pos, height, paint);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Uri uri = Uri.parse(UrlText);
		Intent it = new Intent(Intent.ACTION_VIEW,uri);
		ParentActivity.startActivity(it);
		color=Color.BLACK;
		this.invalidate();
	}
	public void setUrl(Activity ParentActivity,String text){
		UrlText=text;
		this.ParentActivity=ParentActivity;
	}
	private int getTextWidth(){
		int len=UrlText.length();
		return len*6;
	}
	

}
