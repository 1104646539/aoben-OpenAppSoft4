package com.open.soft.openappsoft.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.utils.http.CheckService;
import com.example.utils.http.Global;
import com.example.utils.http.Result;
import com.example.utils.http.RetrofitServiceManager;
import com.example.utils.http.model.BaseResult;
import com.example.utils.http.model.UpdateBean;
import com.google.gson.Gson;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.App;
import com.open.soft.openappsoft.bean.AppInfoBean;
import com.open.soft.openappsoft.bean.AppUpdateBean;
import com.open.soft.openappsoft.bean.JsonRootBean;
import com.open.soft.openappsoft.multifuction.util.DownloadUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class APPUtils {
    public static boolean isNull(String value) {
        if (value == null || "".equals(value)) return true;
        return false;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;

        //拿到管理类之类这样的代码
        /*
         * SMSManager.getDefaul()
         * windowManager 可以拿到和屏幕相关的信息
         * TelephoneManager 电话相关的
         * NotificationManager 拿到和通知相关的
         * PackageManager 和包相关的
         */

        PackageManager packageManager = context.getPackageManager();
        // 拿到包的一些信息
        try {
            // 标记写一个0代表所有信息都要，javabean对象
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            // 拿到版本号和版本名称
            versionCode = packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVersionName(Context context) {
        String versionName = null;

        PackageManager packageManager = context.getPackageManager();
        // 拿到包的一些信息
        try {
            // 标记写一个0代表所有信息都要
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);


            // 拿到版本号和版本名称
            versionName = packageInfo.versionName;

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isSDCardEnable() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void showToast(final Activity activity, final String content) {
        activity.runOnUiThread(new Runnable() {

            @SuppressLint("WrongConstant")
            @Override
            public void run() {
                Toast.makeText(activity, content, 0).show();
            }
        });
    }

    /**
     * 判断指定名称的服务是否运行
     *
     * @param act
     * @param serviceName
     * @return
     */
    public static boolean isServiceRunning(Activity act, String serviceName) {

        /**
         * 手机中的任务管理器，管理手机所有的正在运行的信息，如：activity,service,内存使用状态，等，都由ActivityManager 来管理
         */
        ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);

        // 获得正在运行的服务的信息
        List<RunningServiceInfo> runningServices = am.getRunningServices(100); // 如果手机运行20个服务，返回的集合 size = 20 ，如果手机运行 200个服务，返回的集合 size = 100
        for (RunningServiceInfo runningServiceInfo : runningServices) {
            // 正在运行的服务的类名
            String className = runningServiceInfo.service.getClassName();
//			System.out.println("className::"+className);
            if (className.equals(serviceName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获得手机安装的所有的应用程序信息
     *
     * @param ctx
     * @return
     */
    public static List<AppInfoBean> getAllAppInfo(Context ctx) {

        List<AppInfoBean> allApplist = new ArrayList<AppInfoBean>();

        // 获得包管理器，管理手机上所有的APK安装包
        PackageManager pm = ctx.getPackageManager();

        // 获得安装的所有的包的信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {

            AppInfoBean appInfo = new AppInfoBean();

            allApplist.add(appInfo);

            // 设置包名
            appInfo.packageName = packageInfo.packageName;

            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            // 获得图标
            Drawable appIcon = applicationInfo.loadIcon(pm);
            appInfo.appIcon = appIcon;
            // 获得名称
            CharSequence appName = applicationInfo.loadLabel(pm);
            appInfo.appName = appName.toString();

            // APK文件的路径
            String apkPath = applicationInfo.sourceDir;
//			System.out.println(appName+" : "+apkPath);
            appInfo.apkPath = apkPath;

            // 获得apk文件大小
            appInfo.appSize = new File(apkPath).length();

            // 判断是否是系统应用
            if (apkPath.startsWith("/data")) { // 用户应用
                appInfo.isSys = false;
            } else {
                appInfo.isSys = true;
            }

            // applicationInfo.flags 与特定的FLAG值按位相与 ，如果不等于0，说明批配成功，
            // 当前应用拥有该FLAG表示的属性
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//				System.out.println(appName +"用flag 判断是系统应用");
            } else {
//				System.out.println(appName +"用flag 判断是用户应用");
            }

            if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                // 是安装在SD卡中
                appInfo.isInSd = true;
            } else {
                // 在内部存储中
                appInfo.isInSd = false;
            }
        }

        SystemClock.sleep(1000); // 休眠一会儿，让看见效果

        return allApplist;
    }

    /**
     * okHttp 网络请求框架
     */
    public static class OkHttp {

        private OkHttpClient mOkHttpClient = null;
        private Call call = null;
        private String url = null;
        private Map<String, String> map = null;

        /**
         * 初始化 参数 post
         *
         * @param url
         * @param map
         */
        public OkHttp(String url, Map<String, String> map) {
            mOkHttpClient = new OkHttpClient();
            this.url = url;
            this.map = map;
        }

        /**
         * 初始化 参数 get
         *
         * @param url
         */
        public OkHttp(String url) {
            mOkHttpClient = new OkHttpClient();
            this.url = url;
        }

        /**
         * 请求数据
         *
         * @param callback new 一个 Callback 类的内部类
         */
        public void loadData(Callback callback) {
            if (url != null) {
                Request request = null;
                if (map != null && map.size() > 0) {
                    FormBody.Builder builder = new FormBody.Builder();
                    for (String key : map.keySet()) {
                        builder.add(key, map.get(key));
                    }
                    RequestBody formBody = builder.build();
                    request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .build();
                }
                call = mOkHttpClient.newCall(request);
                call.enqueue(callback);

            }
        }

    }

    /**
     * @param apkPath
     * @安装APK
     * @安装新版本
     */
    public static void installNewApk(Activity activity, String apkPath) {
        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + apkPath;
        Uri uri;
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //支持7.0
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(url));
        } else {
            uri = Uri.fromFile(new File(url));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型
        activity.getApplication().startActivity(intent);
    }

    //更新APP中
    public static void updateApp(Context context, ProgressDialog progressDialog) {
        CheckService checkService = RetrofitServiceManager.getInstance().getCheckService();

//        Map<String, String> map = new HashMap<>();
//        if ("多参数食品安全检测仪".equals(InterfaceURL.oneModule)) {//多参数
//            map.put("requestAppVersionsName", "OpenAppSoft3");
//        } else if ("农药残留检测仪".equals(InterfaceURL.oneModule)) {//单农残
//            map.put("requestAppVersionsName", "OpenAppSoft2");
//        } else if ("农药残留单项精准分析仪".equals(InterfaceURL.oneModule)) {//单金标
//            map.put("requestAppVersionsName", "OpenAppSoft1");
//        }
//        map.put("app","qingdao001");
        checkService.GetUpdate(Global.URL_UPDATE, Global.APP_NAME).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseResult<UpdateBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ((Activity) context).runOnUiThread(() -> {
                            progressDialog.dismiss();
                            APPUtils.showToast(((Activity) context),"获取错误："+e.getMessage());

                        });
                    }

                    @Override
                    public void onNext(BaseResult<UpdateBean> updateBeanResult) {
                        if (updateBeanResult.code == 200) {
                            UpdateBean appUpdateBean = updateBeanResult.data;
                            if (appUpdateBean == null) {
                                return;
                            }
                            int versionCode = Integer.valueOf(appUpdateBean.getVersion());
                            if (versionCode <= GT.ApplicationUtils.getVersionCode(context)) {
                                Timber.i("onResponse: 当前App为最新版，无需更新。");
                                return;
                            }

                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!((Activity) context).isFinishing())
                                        progressDialog.show();
                                }
                            });

                            /**
                             * @param url          下载连接
                             * @param destFileDir  下载的文件储存目录
                             * @param destFileName 下载文件名称，后面记得拼接后缀，否则手机没法识别文件类型
                             * @param listener     下载监听
                             */
                            String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "最新APP/";
                            String saveFileName = appUpdateBean.getName() + "" + appUpdateBean.getVersion() + ".apk";
                            new DownloadUtil().download(appUpdateBean.getUrl(), appSavePath, saveFileName, new DownloadUtil.OnDownloadListener() {
                                @Override
                                public void onDownloadSuccess(File file) {
//                            Log.i(TAG, "onDownloadSuccess: 下载完成");

                                    ((Activity) context).runOnUiThread(() -> progressDialog.dismiss());
                                    //自动安装下载好的 APP
                                    installNewApk((Activity) context, "最新APP/" + saveFileName);
                                }

                                @Override
                                public void onDownloading(int progress) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.setMessage("正在下载最新版本，请稍等...已完成 " + progress + "%");
                                        }
                                    });
//                            Log.i(TAG, "onDownloading: 下载中...:" + progress);
                                }

                                @Override
                                public void onDownloadFailed(Exception e) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isValidContext(context) && progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                            APPUtils.showToast(((Activity) context),"下载失败："+e.getMessage());
                                        }
                                    });

                                }
                            });
                        } else {
                            ((Activity) context).runOnUiThread(() -> {
                                progressDialog.dismiss();
                                APPUtils.showToast(((Activity) context),"获取错误："+updateBeanResult.code +" "+updateBeanResult.message);
                            });
//                            progressDialog.dismiss();
                        }
                    }
                });
//        new OkHttp(Global.BASE_URL + Global.URL_UPDATE, map).loadData(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.code() == 200) {
//                    String string = response.body().string();
//                    BaseResult<UpdateBean> jsonRootBean = new Gson().fromJson(string, BaseResult<UpdateBean>.class);
//                    AppUpdateBean appUpdateBean = jsonRootBean.data;
//                    if (appUpdateBean == null) {
//                        return;
//                    }
//
//                    if (appUpdateBean.getVersions().equals(GT.ApplicationUtils.getVerName(context))) {
//                        Timber.i("onResponse: 当前App为最新版，无需更新。");
//                        return;
//                    }
//
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!((Activity) context).isFinishing())
//                                progressDialog.show();
//                        }
//                    });
//
//                    /**
//                     * @param url          下载连接
//                     * @param destFileDir  下载的文件储存目录
//                     * @param destFileName 下载文件名称，后面记得拼接后缀，否则手机没法识别文件类型
//                     * @param listener     下载监听
//                     */
//                    String appSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "最新APP/";
//                    new DownloadUtil().download(appUpdateBean.getFilePath(), appSavePath, appUpdateBean.getProjectName() + ".apk", new DownloadUtil.OnDownloadListener() {
//                        @Override
//                        public void onDownloadSuccess(File file) {
////                            Log.i(TAG, "onDownloadSuccess: 下载完成");
//
//                            ((Activity) context).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                }
//                            });
//                            //自动安装下载好的 APP
//                            installNewApk((Activity) context, "最新APP/" + appUpdateBean.getProjectName() + ".apk");
//                        }
//
//                        @Override
//                        public void onDownloading(int progress) {
//                            ((Activity) context).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.setMessage("正在下载最新版本，请稍等...已完成 " + progress + "%");
//                                }
//                            });
////                            Log.i(TAG, "onDownloading: 下载中...:" + progress);
//                        }
//
//                        @Override
//                        public void onDownloadFailed(Exception e) {
//                            ((Activity) context).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (isValidContext(context) && progressDialog.isShowing()) {
//                                        progressDialog.dismiss();
//                                    }
//                                }
//                            });
//
//                        }
//                    });
//
//                }
//            }
//        });


    }

    private static boolean isValidContext(Context c) {

        Activity a = (Activity) c;

        if (a.isDestroyed() || a.isFinishing()) {
            Log.i("YXH", "Activity is invalid." + " isDestoryed-->" + a.isDestroyed() + " isFinishing-->" + a.isFinishing());
            return false;
        } else {
            return true;
        }
    }


}
