package com.open.soft.openappsoft.jinbiao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.open.soft.openappsoft.jinbiao.activity.MainActivity;

/**
 * Created by Administrator on 2017-12-11.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent data = new Intent(context, MainActivity.class);
        data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(data);
    }
}
