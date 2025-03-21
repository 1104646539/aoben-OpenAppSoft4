package com.open.soft.openappsoft.util;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class CustomFormatTree extends Timber.Tree {
    File path;
    FileWriter writer;

    public CustomFormatTree(File path) {
        this.path = path;
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
//            String str = bufferedReader.readLine();
//            int kl = 3;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        try {
            writer = new FileWriter(path);
        } catch (Exception e) {
            Log.i("CustomFormatTree", "CustomFormatTree" + path + " 无权限");
        }
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        String customMessage = String.format("[%s] %s: %s\n", getCurrentTimestamp(), tag, message);
        if (writer != null) {
            try {
                writer.append(customMessage);
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Log.i(tag, message);
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
