package com.open.soft.openappsoft.activity.api;

import android.content.Context;

import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.BaseResponseListener;
import com.kongzue.baseokhttp.util.JsonMap;

public class HttpDataRequest implements ServiceApi {
    private static HttpDataRequest httpDataRequest;
    private static Context me;

    public static HttpDataRequest getInstance() {
        if (httpDataRequest == null) {
            httpDataRequest = new HttpDataRequest();
        }
        return httpDataRequest;
    }

    public HttpDataRequest getContext(Context me) {
        this.me = me;
        return this;
    }
    //产品名称
    public void GetSampleCategoryName(String name,  BaseResponseListener beanResponseListener) {
        JsonMap jsonMap = new JsonMap();
        jsonMap.put("AreaId", com.example.utils.http.Global.admin_pt);
        jsonMap.put("CompanyName", name);
        HttpRequest.JSONPOST(me, GETSampleCategoryName, jsonMap.toString(), beanResponseListener);
    }

}
