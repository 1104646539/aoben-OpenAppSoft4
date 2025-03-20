package com.example.utils.http;

import com.example.utils.http.model.BaseResult;
import com.example.utils.http.model.UploadBean;
import com.example.utils.http.model.UploadBean2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface CheckService {
    /**
     * 登录
     *
     * @return
     */
//    @POST("System/LogIn")
    @POST
    Observable<BaseResult<LoginResultBean>> LogIn(@Url String url, @Body LoginBean loginBean);

    /**
     * 获取归属平台列表接口
     *
     * @return
     */
    @GET
    Observable<Result<List<AreaResultBean>>> GetAreaList(@Url String url);

    /**
     * 获取样本信息接口
     *
     * @return
     */
    @POST
    Observable<Result<GetSamplingInfoResultBean>> GetSamplingInfo(@Url String url, @Body GetSamplingInfoBean loginBean);
    /**
     * 获取更新接口
     *
     * @return
     */
    @POST
    Observable<Result<GetSamplingInfoResultBean>> GetUpdate(@Url String url, @Body GetSamplingInfoBean loginBean);

    /**
     * 验证有效性接口
     *
     * @return
     */
    @POST
    Observable<Result<GetQRInfoResultBean>> GetCardQRInfo(@Url String url, @Body GetQRInfoBean getQRInfoBean);

    /**
     * 上传检测数据
     *
     * @return
     */
    @POST
    Observable<BaseResult<String>> SendResult(@Url String url, @Body UploadBean2 uploadBean);

    /**
     * 获取知识库
     *
     * @return
     */
    @POST
    Observable<Result<List<KnowledgeResultBean>>> GetKnowledge(@Url String url,@Body Map<String,String> map);


}
