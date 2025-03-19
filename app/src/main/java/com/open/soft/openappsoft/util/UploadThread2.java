package com.open.soft.openappsoft.util;

import android.content.Context;
import android.util.Log;

import com.example.utils.http.CheckPresenter;
import com.example.utils.http.GetQRInfoResultBean;
import com.example.utils.http.GetSamplingInfoResultBean;
import com.example.utils.http.Global;
import com.example.utils.http.Result;
import com.example.utils.http.ToolUtil;
import com.example.utils.http.model.UploadBean;
import com.google.gson.Gson;
import com.open.soft.openappsoft.multifuction.model.CheckResult;

import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * 云农贸
 */
public class UploadThread2 extends Thread {

    private static final String TAG = "UploadThread2";
    private Context context;
    private onUploadListener listener;
    private List<CheckResult> list;

    CheckPresenter checkPresenter;

    public UploadThread2(Context context, List<CheckResult> list, onUploadListener listener) {

        this.list = list;
        this.context = context;
        this.listener = listener;
        checkPresenter = new CheckPresenter();
    }


    @Override
    public void run() {
        Log.d(TAG, "获取到的list=" + list.size());
        try {
            Gson gson = new Gson();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).isSelected) continue;
                Thread.sleep(300);

                UploadBean uploadBean = transToUploadModel(list.get(i));
                String tempStr = gson.toJson(uploadBean);
                String encodeStr = AESUtil.encrypt(tempStr, Global.ENCODE_KEY, Global.SALT);
                String decodeStr = AESUtil.decrypt(encodeStr, Global.ENCODE_KEY, Global.SALT);
                Timber.i("tempStr=" + tempStr);
                Timber.i("encodeStr=" + encodeStr);
                Timber.i("Global.ENCODE_KEY=" + Global.ENCODE_KEY + " Global.SALT=" + Global.SALT);
                Timber.i("decodeStr=" + decodeStr);
                checkPresenter.SendResult(encodeStr, i, new CheckPresenter.CheckInterface() {
                    @Override
                    public void GetSamplingInfoSuccess(Result<GetSamplingInfoResultBean> result, int requestCode) {

                    }

                    @Override
                    public void GetSamplingInfoFailed(String msg, int requestCode) {

                    }

                    @Override
                    public void GetCardQRInfoSuccess(Result<GetQRInfoResultBean> result, int requestCode) {

                    }

                    @Override
                    public void GetCardQRInfoFailed(String msg, int requestCode) {

                    }

                    @Override
                    public void SendResultSuccess(String msg, int requestCode) {
                        listener.onSuccess(list, 1, requestCode, "");
                    }

                    @Override
                    public void SendResultFailed(String msg, int requestCode) {
                        listener.onFail(msg);
                    }
                });
//                String content = transToUploadModel(list.get(i));
//                Log.d("UploadThread2", "" + TextUtils.isEmpty(content));
//                if (TextUtils.isEmpty(content) && listener != null) {
//                    listener.onFail("设备ID或上传地址为空，请输入后重新上传");
//                    return;
//                }
//                Log.d("", "String content:" + content);
////                APPUtils.showToast((Activity) context, content);
//                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//                MediaType FORM_CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
//                RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE, content);
//                requestBody.contentType().charset(Charset.forName("gb2312"));
//                Request request = new Request.Builder().url(Global.YNM_BaseUrl)
//                        .post(requestBody).build();
//                Call call = okHttpClient.newCall(request);
//                int finalI = i;
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        if (listener != null) {
//                            listener.onFail(e.getMessage());
//                        }
//                        Log.d("失败", e.toString());
//                        APPUtils.showToast((Activity) context, e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String result = response.body().string();
//                        Log.d("成功", result);
////                        try {
////                            YNMUploadResultModel um = new Gson().fromJson(result, YNMUploadResultModel.class);
////                            if (listener != null) {
////                                if (um.getStatus() == 1) {
////                                    listener.onSuccess(list, 1, finalI, result);
////                                } else {
////                                    listener.onFail("上传失败:" + um.getMessage());
////                                }
////                            }
////                        } catch (Exception e) {
////                            if (listener != null) {
////                                listener.onFail("失败：" + result);
////                            }
////                        }
//
//                    }
//                });
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFail("上传失败2");
            }
        }
    }

    public static UploadBean transToUploadModel(CheckResult checkResult) {
        if (Global.BASE_URL == null || Global.BASE_URL.isEmpty() || Global.Token == null || Global.Token.isEmpty()) {
            return null;
        }
        int result = 0;
        if (checkResult.resultJudge.contains("不合格") || checkResult.resultJudge.contains("阳性")) {
            result = 1;
        }
        UploadBean uploadBean = new UploadBean();
        uploadBean.setCheckUser(ToolUtil.nullToString(checkResult.checker, ""));
        uploadBean.setSampleName(ToolUtil.nullToString(checkResult.sampleName, ""));
        uploadBean.setCheckItemName(ToolUtil.nullToString(checkResult.projectName, ""));
        uploadBean.setCheckMothedName(ToolUtil.nullToString("123", ""));
        uploadBean.setDeviceSn(ToolUtil.nullToString(InterfaceURL.companyName, ""));
        uploadBean.setSampleType(ToolUtil.nullToString(checkResult.sampleType, ""));
        uploadBean.setSampleTypeId(ToolUtil.nullToString(checkResult.sampleTypeCode, ""));
        uploadBean.setSampleSubType(ToolUtil.nullToString(checkResult.sampleTypeChild, ""));
        uploadBean.setSampleSubTypeId(ToolUtil.nullToString(checkResult.sampleTypeChildCode, ""));
        uploadBean.setCompanyName(ToolUtil.nullToString(checkResult.bcheckedOrganization, ""));
        uploadBean.setCompanyCode(ToolUtil.nullToString(checkResult.bcheckedOrganizationCode, ""));
        uploadBean.setSamplingTime(ToolUtil.nullToString(checkResult.SamplingTime, ""));
        uploadBean.setCheckLimit(ToolUtil.nullToString(checkResult.xlz, ""));
        uploadBean.setResult(result);
        uploadBean.setResultValue(ToolUtil.nullToString(checkResult.testValue, ""));
        uploadBean.setCheckOrg(ToolUtil.nullToString(checkResult.checkedOrganization, ""));
        uploadBean.setCheckTime(ToolUtil.date2String(new Date(checkResult.testTime),ToolUtil.DateTime1));
        return uploadBean;
    }

    public interface onUploadListener {

        void onSuccess(List<CheckResult> list, int returnId, int position, String result);

        void onFail(String failInfo);
    }
}
