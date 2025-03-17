package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.multifuction.util.DownloadUtil;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

public class WebActivity extends Activity implements TbsReaderView.ReaderCallback {
    private static final String TAG = "WebActivity";
    String url;
    String title;
    TextView tv_title;
    boolean isFile = false;
    TbsReaderView tbsReaderView;
    ProgressDialog progressDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        tv_title = (TextView) findViewById(R.id.tv_title);
        isFile = getIntent().getBooleanExtra("isFile", false);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        tbsReaderView = new TbsReaderView(this, this);
        LinearLayout rootRl = (LinearLayout) findViewById(R.id.ll);
        rootRl.addView(tbsReaderView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载");
        if (url != null) {
            progressDialog.show();
            DownloadUtil.get().download(url, getCacheDir().getAbsolutePath(), "test.pdf", new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess(File file) {
                    Log.d(TAG, "onDownloadSuccess file=" + file.getAbsolutePath());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("filePath", file.getAbsolutePath());
                            bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
                            boolean result = tbsReaderView.preOpen("pdf", false);
                            Log.d(TAG, "onCreate result=" + result);
                            if (result) {
                                tbsReaderView.openFile(bundle);
                            }
                        }
                    }, 1000 * 2);

                }

                @Override
                public void onDownloading(int progress) {

                }

                @Override
                public void onDownloadFailed(Exception e) {
                    Log.d(TAG, "onDownloadFailed e=" + e.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
        if (title != null) {
            tv_title.setText(title);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
