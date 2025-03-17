package com.open.soft.openappsoft.jinbiao.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-11-28.
 */

public class APPUtils {


    public static void showToast(final Activity activity, final String content){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
