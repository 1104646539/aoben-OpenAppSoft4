package com.open.soft.openappsoft.multifuction.thread;

import android.content.Context;
import android.text.TextUtils;

import com.open.soft.openappsoft.multifuction.db.DbHelper;
import com.open.soft.openappsoft.multifuction.model.CheckResult;
import com.open.soft.openappsoft.multifuction.util.Global;
import com.open.soft.openappsoft.multifuction.util.ToolUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadThread extends Thread {

    private static final String TAG = "UploadThread";
    private Context context;
    private onUploadListener listener;
    private List<CheckResult> list;

    public UploadThread(Context context, List<CheckResult> list, onUploadListener listener) {

        this.list = list;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public void run() {
        try {
            for (int i = 0; i < list.size(); i++) {
                Thread.sleep(300);
                String content = ToolUtils.assemblyUploadData(list.get(i));
                if (TextUtils.isEmpty(content) && listener != null) {
                    listener.onFail("设备ID或上传地址为空，请输入后重新上传");
                    return;
                }


                //组装正确的后台请求参数
                int spmcIndex = content.indexOf("spmc");
                String name = content.substring(spmcIndex + 6, content.indexOf(",", spmcIndex) - 1);

                //从数据库中获取到对应的 样品号
                String sampleNumber = DbHelper.getSampleNumber(name);

                //如果查询的为空，那就直接返回上传失败
                if (sampleNumber == null) {
                    if (listener != null) {
                        listener.onFail("上传失败");
                    }
                    return;
                }
                // 获取第一段数据
                String spdm = content.substring(0, content.indexOf("spdm") + 6);

                // 获取第二段数据
                String spmc = content.substring(content.indexOf("spmc") - 2, content.length());

                //组合正确的网络请求参数
                content = spdm + sampleNumber + spmc;

                OkHttpClient okHttpClient = new OkHttpClient();

                MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
                RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE, "result=" + content);
                requestBody.contentType().charset(Charset.forName("gb2312"));
                Request request = new Request.Builder().url(Global.BASE_URL)
                        .post(requestBody).build();
                Call call = okHttpClient.newCall(request);
                int finalI = i;
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (listener != null) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        // 新增
                        if (listener != null) {
                            if (result.contains("上传成功")) {
                                listener.onSuccess(list, 1, finalI, result);
                            } else {
                                listener.onFail(result);
                            }
                        }
                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFail("上传失败2");
            }
        }

    }

    public interface onUploadListener {

        void onSuccess(List<CheckResult> list, int returnId, int position, String result);

        void onFail(String failInfo);
    }

}
