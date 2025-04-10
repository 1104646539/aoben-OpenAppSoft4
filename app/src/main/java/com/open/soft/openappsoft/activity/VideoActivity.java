package com.open.soft.openappsoft.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.util.FileUtils;
import com.open.soft.openappsoft.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoActivity extends Activity {
    private TextView tv_title;
    private String title;
    private VideoView videoView;
    private MediaController media = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        tv_title = findViewById(R.id.tv_title);
        videoView = findViewById(R.id.videoView);

        String file_path = Environment.getDownloadCacheDirectory().toString() + "/video";
        String filename = "test.mp4";
//        Deposit(file_path, filename);
        title = getIntent().getStringExtra("title");
        if (title != null) {
            tv_title.setText(title);
        }
        media = new MediaController(this);
        videoView.setMediaController(media);

//        Uri uri = Uri.parse("file:///android_asset/" + filename);
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + );
        try {
            File file = fileFromAsset(this, filename);
            videoView.setVideoPath(file.getAbsolutePath());
            videoView.requestFocus();
            videoView.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        videoView.setVideoURI(uri);
//        videoView.setVideoPath(new File(file_path, filename).getAbsolutePath());
//        videoView.requestFocus();
//        videoView.start();
    }
    public static File fileFromAsset(Context context, String assetName) throws IOException {
        File outFile = new File(context.getCacheDir(), assetName + "-test.mp4");
        if (assetName.contains("/")) {
            outFile.getParentFile().mkdirs();
        }
        copy(context.getAssets().open(assetName), outFile);
        return outFile;
    }

    public static void copy(InputStream inputStream, File output) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(output);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }
    public Boolean Deposit(String path, String fileName) {
        InputStream inputStream;
        try {

            //判断文件是否存在
            File file1 = new File(path + "/" + fileName);

            if (!file1.exists()) {

                inputStream = getResources().getAssets().open(fileName);
                File file = new File(path);
                //当目录不存在时创建目录
                if (!file.exists()) {
                    file.mkdirs();
                }

                FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileName);// 保存到本地的文件夹下的文件
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

            } else {
                Toast.makeText(this, "已存在", Toast.LENGTH_LONG).show();

            }

            return true;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(videoView!=null){
            videoView.stopPlayback();
        }
    }
}
