package com.open.soft.openappsoft;

import android.app.Application;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.gsls.gt.GT;
import com.kongzue.baseokhttp.util.BaseOkHttp;
import com.open.soft.openappsoft.activity.ErrorActivity;
import com.open.soft.openappsoft.activity.MainActivity;
import com.open.soft.openappsoft.jinbiao.db.DbHelper;
import com.open.soft.openappsoft.jinbiao.location.LocationService;
import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;
import com.open.soft.openappsoft.util.CustomFormatTree;
import com.open.soft.openappsoft.util.InterfaceURL;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.io.IOException;
import java.util.HashMap;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class App extends Application {
    private static final String TAG = "App";

    public LocationService locationService;
    public Vibrator mVibrator;
    public static SharedPreferences defaultSP;

    @Override
    public void onCreate() {
        super.onCreate();
        //这个是打开日志
        BaseOkHttp.TIME_OUT_DURATION = 10;
        BaseOkHttp.DEBUGMODE = true;
        Timber.plant(new CustomFormatTree()); // 开发模式下使用 DebugTree

        defaultSP = SharedPreferencesUtil.getDefaultSharedPreferences(this);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        BaseOkHttp.serviceUrl = "http://www.xindaon.cn:7005/api/";
        SDKInitializer.initialize(getApplicationContext());

        //jinbiao
        DbHelper.InitDb(getApplicationContext());
        int time = SharedPreferencesUtil.getTime(this, "time");
        if (time == 1) {
            SharedPreferencesUtil.setTime(this, "time", 1);
        }
        //mult
        com.open.soft.openappsoft.multifuction.db.DbHelper.InitDb(this);


        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d(TAG, " onCreate initX5Environment onCoreInitFinished ");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.d(TAG, " onCreate initX5Environment onViewInitFinished b=" + b);
            }
        });
//        new GT.HttpUtil().postRequest(url, dataMap, new GT.HttpUtil.OnLoadDataListener() {
//            @Override
//            public void onSuccess(String response, Object o) {
//
//            }
//
//            @Override
//            public void onError(String response, Object o) {
//
//            }
//        });
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(InterfaceURL.BASE_URL + str_api)
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                String ret = response.body().string();
//
//            }
//        });
//        //崩溃收集
//        CaocConfig.Builder.create()
//                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//                .enabled(true) //default: true
//                .showErrorDetails(true) //default: true
//                .showRestartButton(true) //default: true
//                .logErrorOnRestart(true) //default: true
//                .trackActivities(true) //default: false
//                .minTimeBetweenCrashesMs(2000) //default: 3000
//                .errorDrawable(R.mipmap.ic_launcher) //default: bug image
//                .restartActivity(MainActivity.class) //default: null (your app's launch activity)
//                .errorActivity(ErrorActivity.class) //default: null (default error activity)
//                //default: null
//                .apply();

    }
}
