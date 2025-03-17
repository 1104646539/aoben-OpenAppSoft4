package com.open.soft.openappsoft.jinbiao.activity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.PowerManager;

public class MyApplication extends Application {

	private static PowerManager.WakeLock wakeLock;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		keepScreenOn(getApplicationContext(), true);
	}

	public static void keepScreenOn(Context context, boolean on) {
		if (on) {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
			wakeLock.acquire();
		} else {
			if (wakeLock != null) {
				wakeLock.release();
				wakeLock = null;
			}
		}
	}
	@Override
	public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        keepScreenOn(getApplicationContext(), false);
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
        keepScreenOn(getApplicationContext(), false);
    }
    @SuppressLint("NewApi")
	@Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);
        keepScreenOn(getApplicationContext(), false);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
