package com.example.njupt.zhb.imageprocesssystem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 作者：郑海波 2012-08-12 Email:zhb931706659@126.com
 */
public class SelectAlgActivity extends Activity implements OnItemClickListener {
	private static final String DYNAMICACTION_Broadcast = "Broadcast.njupt.zhb.selectAlg";
	private ListView listView;

	public void sendFlagToActivity(String flag) {
		Intent intent = new Intent();
		intent.setAction(DYNAMICACTION_Broadcast);
		intent.putExtra("selectFlag", flag);
		sendBroadcast(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Choose Image processing type!");
		listView = new ListView(this);
		List<String> list = getData();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, list);
		listView.setAdapter(adapter);
		setContentView(listView);
		listView.setOnItemClickListener(this);// 绑定监听接口
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("图像变亮");
		data.add("中值滤波");
		data.add("平滑滤波");
		data.add("灰度处理");
		data.add("图像二值化");
		data.add("图像取反");
		data.add("图像旋转90");
		data.add("直方图均衡");
		data.add("罗伯特梯度");
		data.add("Sobel梯度");
		data.add("3*3拉普拉斯梯度");
		data.add("灰度直方图");
		data.add("Sobel蓝色风格");
		data.add("Laplace蓝色风格");
		return data;
	}

	/* 实现OnItemClickListener接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// finish();
		String posString = Integer.toString(position);
		sendFlagToActivity(posString);
		finish();
	}
}
