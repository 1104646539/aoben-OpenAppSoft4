package com.example.utils.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

//import com.gsls.gt.GT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import com.open.soft.openappsoft.multifuction.dialog.EditURLDialog;



public class OrderPresenter {
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
    public OrderPresenter() {
        checkService = RetrofitServiceManager.getInstance().getCheckService();
    }

    public void GetAreaList(final OrderInterface checkInterface) {
//        GT.logs("GetAreaList:" + Global.BASE_URL);
        checkService.GetAreaList(Global.URL_GetAreaList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Result<List<AreaResultBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                if (checkInterface != null) {
//                    checkInterface.getAreaListFailed(e.getMessage());
//                }
            }

            @Override
            public void onNext(Result<List<AreaResultBean>> listResult) {
                Log.d("GetAreaList", "onNext result=" + listResult);
                if (listResult.ErrCode.equals("0")) {
                    if (checkInterface != null) {
                        checkInterface.getAreaListSuccess(listResult);
                    }
                } else if (listResult.ErrCode.equals("1")) {
                    if (checkInterface != null) {
                        checkInterface.getAreaListFailed(listResult.ErrMsg);
                    }
                } else {
                    if (checkInterface != null) {
                        checkInterface.getAreaListFailed(listResult.ErrMsg);
                    }
                }
            }
        });
    }

    String TAG = "GT_i";

    public void login(final LoginBean loginBean, final OrderInterface checkInterface, final Context context, final String pass, final ProgressDialog progressDialog) {
        checkService.LogIn(Global.URL_LOGIN, loginBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Result<LoginResultBean>>() {
            @Override
            public void onCompleted() {

                //保存登录成功的账号密码
                SharedPreferences sp = context.getSharedPreferences("userPass",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();   //获取编辑器
                editor.putString("user",loginBean.getId()); //存入String型数据
                editor.putString("pass",pass); //存入String型数据
                editor.apply();                //提交修改，否则不生效

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, "请检查网络，网速太慢了...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
//                if (checkInterface != null) {
//                    checkInterface.logInFailed(e.getMessage());
//                }
            }

            @Override
            public void onNext(Result<LoginResultBean> result) {
                if (result.ErrCode.equals("0")) {
                    if (checkInterface != null) {
                        checkInterface.logInSuccess(result.getData());
                    }
                } else if (result.ErrCode.equals("1")) {
                    if (checkInterface != null) {
                        checkInterface.logInFailed(result.ErrMsg);
                    }
                } else {
                    if (checkInterface != null) {
                        checkInterface.logInFailed(result.ErrMsg);
                    }
                }
            }
        });
    }

    public void GetKnowledge(Map<String,String> map, final OrderInterface checkInterface) {
//        checkService.LogIn(AreaId,Id,Pwd).subscribeOn(Schedulers.io())
        // 从某个地址上获取数据
        checkService.GetKnowledge(Global.URL_GetKnowledge,map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Result<List<KnowledgeResultBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//                if (checkInterface != null) {
//                    checkInterface.logInFailed(e.getMessage());
//                }
            }

            @Override
            public void onNext(Result<List<KnowledgeResultBean>> result) {
                Log.d("login", "onNext result=" + result);
                Log.i("nameSSS","result:" + result);
                if (result.ErrCode.equals("0")) {
                    if (checkInterface != null) {
                        checkInterface.getKnowledgeSuccess(result);
                    }
                } else if (result.ErrCode.equals("1")) {
                    if (checkInterface != null) {
                        checkInterface.getKnowledgeFailed(result.ErrMsg);
                    }
                } else {
                    if (checkInterface != null) {
                        checkInterface.getKnowledgeFailed(result.ErrMsg);
                    }
                }
            }
        });
    }

    public interface OrderInterface {
        void getAreaListSuccess(Result<List<AreaResultBean>> result);

        void getAreaListFailed(String msg);

        void logInSuccess(LoginResultBean resultBean);

        void logInFailed(String msg);

        void getKnowledgeSuccess(Result<List<KnowledgeResultBean>> result);

        void getKnowledgeFailed(String msg);
    }
}
