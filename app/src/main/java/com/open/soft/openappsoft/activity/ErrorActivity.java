package com.open.soft.openappsoft.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.open.soft.openappsoft.R;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        String stackTraceFromIntent = CustomActivityOnCrash.getStackTraceFromIntent(getIntent());
        findViewById(R.id.button).setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(getBaseContext().CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", stackTraceFromIntent);
            clipboardManager.setPrimaryClip(clip);
        });
        findViewById(R.id.button2).setOnClickListener(view -> {
            CaocConfig configFromIntent = CustomActivityOnCrash.getConfigFromIntent(getIntent());
            CustomActivityOnCrash.restartApplication(this, configFromIntent);
        });


        TextView textView = findViewById(R.id.textView);
        textView.setText(stackTraceFromIntent);
    }
}