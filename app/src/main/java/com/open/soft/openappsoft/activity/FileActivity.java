package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.utils.http.Global;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.util.InterfaceURL;
import com.tencent.smtt.sdk.TbsReaderView;

public class FileActivity extends Activity implements TbsReaderView.ReaderCallback {
    private static final String TAG = "FileActivity";
    String url;
    TbsReaderView tbsReaderView;
    String title;
    String fileType;
    TextView tv_title;
    boolean isFile = false;
    InterfaceURL interfaceURL = new InterfaceURL();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        String pdf_name = Global.companyCode_value +"_"+ Global.deviceType_value + "_" + Global.samplingMode_value + ".pdf";

        String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        Global.URI_MULT = appSavePath + "/" + pdf_name;
        tv_title = findViewById(R.id.tv_title);
        isFile = getIntent().getBooleanExtra("isFile", false);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        fileType = getIntent().getStringExtra("fileType");
        if (title != null) {
            tv_title.setText(title);
        }
        tbsReaderView = new TbsReaderView(this, this);
        LinearLayout rootRl = findViewById(R.id.ll);
        rootRl.addView(tbsReaderView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Bundle bundle = new Bundle();
        bundle.putString("filePath", url);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        boolean result = tbsReaderView.preOpen(fileType, false);
        Log.d(TAG, "onCreate result=" + result);
        if (result) {
            tbsReaderView.openFile(bundle);
        }

//        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tbsReaderView.onStop();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }


}
