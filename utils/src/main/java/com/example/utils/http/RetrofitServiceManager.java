package com.example.utils.http;


//import com.gsls.gt.GT;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 3;//超时时间 5s    
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;
    private static CheckService checkService;
    static RetrofitServiceManager retrofitServiceManager;

    public RetrofitServiceManager() {
        init();
//        // 创建 OKHttpClient      
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间        builder.writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//写操作 超时时间        
//        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间  
//        // 添加公共参数拦截器        
////            HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
////                    .addHeaderParams("paltform","android")
////                    .addHeaderParams("userToken","1234343434dfdfd3434")
////                    .addHeaderParams("userId","123445")
////                    .build();
////            builder.addInterceptor(commonInterceptor);
//        // 创建Retrofit        
//        mRetrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(Global.BASE_URL)
//                .build();
//        checkService = mRetrofit.create(CheckService.class);
    }

    public void init() {
        Interceptor logInterceptor;
        //处理网络请求的日志拦截输出
//        if (BuildConfig.DEBUG) {
//            //只显示基础信息
//            logInterceptor = new HttpLoggingInterceptor(new HttpLog()).setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
        logInterceptor = new HttpLoggingInterceptor(new HttpLog()).setLevel(HttpLoggingInterceptor.Level.BODY);
//        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String token = Global.Token;

                // 如果 Token 存在，添加到请求头
                if (token != null && !originalRequest.url().toString().contains(Global.URL_LOGIN)) {
                    Request authRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(authRequest);
                }
                return chain.proceed(originalRequest);
            }
        });
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间        builder.writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//写操作 超时时间        
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间  
        builder.addInterceptor(logInterceptor);
        // 添加公共参数拦截器        
//            HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
//                    .addHeaderParams("paltform","android")
//                    .addHeaderParams("userToken","1234343434dfdfd3434")
//                    .addHeaderParams("userId","123445")
//                    .build();
//            builder.addInterceptor(commonInterceptor);
        // 创建Retrofit

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Global.BASE_URL)
                .build();
//        GT.logs("Global.BASE_URL:" + Global.BASE_URL);
        checkService = mRetrofit.create(CheckService.class);
    }

    public void refreshRetrofitServiceManager() {
        init();
    }

    public CheckService getCheckService() {
        return checkService;
    }

//    private static class SingletonHolder {
//        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
//    }
//
//    /**
//     * 获取RetrofitServiceManager
//     *
//     * @return
//     */
//    public static RetrofitServiceManager getInstance() {
//        return SingletonHolder.INSTANCE;
//    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static RetrofitServiceManager getInstance() {
        if (retrofitServiceManager == null) {
            retrofitServiceManager = new RetrofitServiceManager();
        }
        return retrofitServiceManager;
    }


    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
