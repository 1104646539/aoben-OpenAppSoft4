package com.example.utils.http;

public class Global {

    public static String BASE_URL = "http://api.shionda.com/qingdao/";
    /**
     * 登录ID
     */
    public static String ID = "";
    /**
     * 用户名
     */
    public static String NAME = "";
    /**
     * 秘钥
     */
    public static String SALT = "";
    /**
     * 设备码
     */
    public static String SN = "W123";
    /**
     * 仪器型号
     */
    public static String KEY = "688961777307881472";
    /**
     * 应用名
     */
    public static String APP_NAME = "qingdao001";
    /**
     * 加密 key
     */
    public static String ENCODE_KEY = "e2fd36a50c8c4759a5aaf1f094543a17";
    /**
     * Token
     */
    public static String Token = "";
    /**
     * Token 刷新用
     */
    public static String RefreshToken = "";

    //是否自动录入信息
    public static boolean isVoluntarily = true;//默认是自动的录入信息

    // 混合录入信息标志位
    public static boolean ismixedentry = true;// 默认不是混合录入信息
    public static String NEEDCompanyCode = "0";// 默认不是混合录入信息

    /**
     * 用户单位
     */
    public static String Dept = "";

    /**
     * 是否是管理员模式，管理员可以修改部分设置
     */
    public static boolean isAdimin = true;
    public static String admin_user = "admin";
    public static String admin_psw = "123456";
    public static String admin_name = "";
    public static String admin_pt = "";
    public static String URL_LOGIN = "api/system/auth/device-login";
    public static String URL_UPDATE = "api/system/app/version/check";
    //    public static String URL_GetAreaList = "System/GetAreaList";
//    public static String URL_GetSamplingInfo = "QR/GetSamplingInfo";
//    public static String URL_GetCardQRInfo = "QR/GetQRInfo";
    public static String URL_SendResult = "api/device/sample/check/upload";
//    public static String URL_GetKnowledge = "Other/GetKnowledge";

    /**
     * 公司名称
     */
    public static String company_name = "";

    /**
     * 输入方式判定标志
     */
    public static String SamplingMode = "";

    /**
     * 操作说明的路径
     */
    public static String URI_MULT = "";
    public static String URI_JINBIAO = "";

    public final static String SP_SN = "SN";
    public final static String SP_KEY = "KEY";
    public final static String SP_ADMIN_USER = "ADMIN_USER";
    public final static String SP_ADMIN_PSW = "ADMIN_PSW";
    public final static String SP_ADMIN_PT = "PT";
    public final static String SP_URL_LOGIN = "URL_LOGIN";
    public final static String SP_URL_GetAreaList = "URL_GetAreaList";
    public final static String SP_URL_GetSamplingInfo = "URL_GetSamplingInfo";
    public final static String SP_URL_GetCardQRInfo = "URL_GetCardQRInfo";
    public final static String SP_URL_SendResult = "URL_SendResult";
    public final static String SP_URL_GetKnowledge = "URL_GetKnowledge";

    /**
     * 操作手册参数
     */
    public static String companyCode_value;
    public static String deviceType_value;
    public static String samplingMode_value;
}
