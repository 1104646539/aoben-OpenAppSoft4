package com.open.soft.openappsoft.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";  
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if(intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
			
			Log.i(TAG, "检测到关机广播");
		}
		Intent activity = new Intent(context, LoginActivity.class);
		
		activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(activity);
	}

}
