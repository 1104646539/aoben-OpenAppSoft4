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
import com.open.soft.openappsoft.sql.bean.DetectionResultBean;

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
    private List<DetectionResultBean> list;

    CheckPresenter checkPresenter;
    int count = 0;
    int success_count = 0;
    int failed_count = 0;

    public UploadThread2(Context context, List<DetectionResultBean> list, onUploadListener listener) {

        this.list = list;
        this.context = context;
        this.listener = listener;
        checkPresenter = new CheckPresenter();
        count = list.size();
        success_count = 0;
        failed_count = 0;
    }


    @Override
    public void run() {
        Log.d(TAG, "获取到的list=" + list.size());
        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {
//            if (!list.get(i).isSelected) continue;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

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
                    success_count++;
                    listener.onUploadSuccess(requestCode,msg);
                    uploadFinish();
                }

                @Override
                public void SendResultFailed(String msg, int requestCode) {
                    failed_count++;
                    listener.onUploadFail(requestCode,msg);
                    uploadFinish();
                }
            });
        }
    }

    private void uploadFinish() {
        if (success_count + failed_count >= count) {
            listener.onUploadFinish(count,success_count,failed_count);
        }
    }

    public static UploadBean transToUploadModel(DetectionResultBean checkResult) {
        if (Global.BASE_URL == null || Global.BASE_URL.isEmpty() || Global.Token == null || Global.Token.isEmpty()) {
            return null;
        }
        int result = 0;
        if (checkResult.getDetectionResult().contains("不合格") || checkResult.getDetectionResult().contains("阳性")) {
            result = 1;
        }
        UploadBean uploadBean = new UploadBean();
        uploadBean.setCheckUser(ToolUtil.nullToString(checkResult.getInspector(), ""));
        uploadBean.setSampleName(ToolUtil.nullToString(checkResult.getSampleName(), ""));
        uploadBean.setCheckItemName(ToolUtil.nullToString(checkResult.getTestItem(), ""));
        uploadBean.setCheckMothedName(ToolUtil.nullToString("123", ""));
        uploadBean.setDeviceSn(ToolUtil.nullToString(InterfaceURL.companyName, ""));
        uploadBean.setSampleType(ToolUtil.nullToString(checkResult.getSpecimenType(), ""));
        uploadBean.setSampleTypeId(ToolUtil.nullToString(checkResult.getSpecimenTypeCode(), ""));
        uploadBean.setSampleSubType(ToolUtil.nullToString(checkResult.getSpecimenTypeChild(), ""));
        uploadBean.setSampleSubTypeId(ToolUtil.nullToString(checkResult.getSpecimenTypeChildCode(), ""));
        uploadBean.setCompanyName(ToolUtil.nullToString(checkResult.getUnitsUnderInspection(), ""));
        uploadBean.setCompanyCode(ToolUtil.nullToString(checkResult.getUnitsUnderInspectionCode(), ""));
        uploadBean.setSamplingTime(ToolUtil.nullToString(checkResult.getSamplingDate(), ""));
        uploadBean.setCheckLimit(ToolUtil.nullToString(checkResult.getLimitStandard(), ""));
        uploadBean.setResult(result);
        uploadBean.setResultValue(ToolUtil.nullToString(checkResult.getDetectionValue(), ""));
        uploadBean.setCheckOrg(ToolUtil.nullToString(checkResult.getDetectionCompany(), ""));
        uploadBean.setCheckTime(ToolUtil.date2String(new Date(checkResult.getDetectionTime()), ToolUtil.DateTime1));
        return uploadBean;
    }
//    public static UploadBean transToUploadModel(CheckResult checkResult) {
//        if (Global.BASE_URL == null || Global.BASE_URL.isEmpty() || Global.Token == null || Global.Token.isEmpty()) {
//            return null;
//        }
//        int result = 0;
//        if (checkResult.resultJudge.contains("不合格") || checkResult.resultJudge.contains("阳性")) {
//            result = 1;
//        }
//        UploadBean uploadBean = new UploadBean();
//        uploadBean.setCheckUser(ToolUtil.nullToString(checkResult.checker, ""));
//        uploadBean.setSampleName(ToolUtil.nullToString(checkResult.sampleName, ""));
//        uploadBean.setCheckItemName(ToolUtil.nullToString(checkResult.projectName, ""));
//        uploadBean.setCheckMothedName(ToolUtil.nullToString("123", ""));
//        uploadBean.setDeviceSn(ToolUtil.nullToString(InterfaceURL.companyName, ""));
//        uploadBean.setSampleType(ToolUtil.nullToString(checkResult.sampleType, ""));
//        uploadBean.setSampleTypeId(ToolUtil.nullToString(checkResult.sampleTypeCode, ""));
//        uploadBean.setSampleSubType(ToolUtil.nullToString(checkResult.sampleTypeChild, ""));
//        uploadBean.setSampleSubTypeId(ToolUtil.nullToString(checkResult.sampleTypeChildCode, ""));
//        uploadBean.setCompanyName(ToolUtil.nullToString(checkResult.bcheckedOrganization, ""));
//        uploadBean.setCompanyCode(ToolUtil.nullToString(checkResult.bcheckedOrganizationCode, ""));
//        uploadBean.setSamplingTime(ToolUtil.nullToString(checkResult.SamplingTime, ""));
//        uploadBean.setCheckLimit(ToolUtil.nullToString(checkResult.xlz, ""));
//        uploadBean.setResult(result);
//        uploadBean.setResultValue(ToolUtil.nullToString(checkResult.testValue, ""));
//        uploadBean.setCheckOrg(ToolUtil.nullToString(checkResult.checkedOrganization, ""));
//        uploadBean.setCheckTime(ToolUtil.date2String(new Date(checkResult.testTime), ToolUtil.DateTime1));
//        return uploadBean;
//    }

    public interface onUploadListener {

        void onUploadSuccess(int position,String msg);

        void onUploadFail(int position,String failInfo);

        void onUploadFinish(int count, int successCount, int failedCount);
    }
}
