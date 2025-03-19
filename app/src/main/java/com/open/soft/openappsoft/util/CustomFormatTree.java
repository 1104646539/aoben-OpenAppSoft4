package com.open.soft.openappsoft.util;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class CustomFormatTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        String customMessage = String.format("[%s] %s: %s", getCurrentTimestamp(), tag, message);
        Log.i(tag,message);
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
