package com.open.soft.openappsoft.jinbiao.base;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.open.soft.openappsoft.jinbiao.activity.MyApplication;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
	protected void onResume() {

		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		MyApplication.keepScreenOn(getApplicationContext(), true);;
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyApplication.keepScreenOn(getApplicationContext(), false);;
		super.onDestroy();
	}
	public void back() {
		finish();
	}
	
}
