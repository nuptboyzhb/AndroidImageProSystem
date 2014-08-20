package com.example.njupt.zhb.imageprocesssystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
public class ActivityAbout extends Activity {
	/** Called when the activity is first created. */
	ImageView imageview1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);;
		setContentView(R.layout.about_activity);
		imageview1=(ImageView)findViewById(R.id.photo1);
		setTitle("This apk is made by ZhengHaibo");
	}
}
