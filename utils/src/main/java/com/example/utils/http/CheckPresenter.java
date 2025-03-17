package com.example.utils.http;

//import com.gsls.gt.GT;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CheckPresenter {
    CheckService checkService;

    /**
     * 错误描述	错误代码
     * 操作成功	0
     * 操作失败	1
     * 参数不全	2
     * 样本编码为空	20001
     * 样本编码不存在	20002
     * 二维码编号为空	30001
     * 无此二维码编号	30002
     * 已达到最大使用次数	30003
     * 该批次尚未正式发布	30004
     * 该批次正在召回	30005
     * 该批次已停止使用	30006
     * 归属平台不存在	40001
     * 用户名不存在	40002
     * 密码错误	40003
     */
    public CheckPresenter() {
        checkService = RetrofitServiceManager.getInstance().getCheckService();
    }

    public void GetSamplingInfo(GetSamplingInfoBean getSamplingInfoBean, final int requestCode, final CheckInterface checkInterface) {
//        return checkService.GetAreaList();
//        checkService.GetAreaList().
        checkService.GetSamplingInfo(Global.URL_GetSamplingInfo, getSamplingInfoBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Result<GetSamplingInfoResultBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (checkInterface != null) {
                    checkInterface.GetSamplingInfoFailed(e.getMessage(), requestCode);
                }
            }

            @Override
            public void onNext(Result<GetSamplingInfoResultBean> result) {
                if (result.ErrCode.equals("0")) {
                    if (checkInterface != null) {
                        checkInterface.GetSamplingInfoSuccess(result, requestCode);
                    }
                } else if (result.ErrCode.equals("1")) {
                    if (checkInterface != null) {
                        checkInterface.GetSamplingInfoFailed(result.ErrMsg, requestCode);
                    }
                } else {
                    if (checkInterface != null) {
                        checkInterface.GetSamplingInfoFailed(result.ErrMsg, requestCode);
                    }
                }
            }
        });
    }

    public void GetCardQRInfo(GetQRInfoBean getQRInfoBean, final int requestCode, final CheckInterface checkInterface) {
//        return checkService.GetAreaList();
//        checkService.GetAreaList().
        checkService.GetCardQRInfo(Global.URL_GetCardQRInfo, getQRInfoBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Result<GetQRInfoResultBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if (checkInterface != null) {
                    checkInterface.GetCardQRInfoFailed(e.getMessage(), requestCode);
                }
            }

            @Override
            public void onNext(Result<GetQRInfoResultBean> result) {

                if (result.ErrCode.equals("0")) {
                    if (checkInterface != null) {
                        checkInterface.GetCardQRInfoSuccess(result, requestCode);
                    }
                } else if (result.ErrCode.equals("1")) {

                    if (checkInterface != null) {
                        checkInterface.GetCardQRInfoFailed(result.ErrMsg, requestCode);
                    }
                } else {

                    if (checkInterface != null) {
                        checkInterface.GetCardQRInfoFailed(result.ErrMsg, requestCode);
                    }
                }
            }
        });
    }

    public void SendResult(SendResultBean getQRInfoBean, final int requestCode, final CheckInterface checkInterface) {
        checkService.SendResult(Global.URL_SendResult, getQRInfoBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Result<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                Timber.i("onError");
                if (checkInterface != null) {
                    checkInterface.SendResultFailed(e.getMessage(), requestCode);
                }
            }

            @Override
            public void onNext(Result<String> result) {
//                Timber.i("onNext");


                if (result.ErrCode.equals("0")) {
                    if (checkInterface != null) {
                        checkInterface.SendResultSuccess(result.getData(), requestCode);
                    }
                } else if (result.ErrCode.equals("1")) {
                    if (checkInterface != null) {
                        checkInterface.SendResultFailed(result.ErrMsg, requestCode);
                    }
                } else {
                    if (checkInterface != null) {
                        checkInterface.SendResultFailed(result.ErrMsg, requestCode);
                    }
                }
            }
        });
    }

    public interface CheckInterface {
        void GetSamplingInfoSuccess(Result<GetSamplingInfoResultBean> result, int requestCode);

        void GetSamplingInfoFailed(String msg, int requestCode);

        void GetCardQRInfoSuccess(Result<GetQRInfoResultBean> result, int requestCode);

        void GetCardQRInfoFailed(String msg, int requestCode);

        void SendResultSuccess(String msg, int requestCode);

        void SendResultFailed(String msg, int requestCode);
    }
}
