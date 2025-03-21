package com.open.soft.openappsoft.util;

public class InterfaceURL {

    //模块名称
    public static String oneModule = "多参数食品安全检测仪";//M417  多参数
//    public static String oneModule = "农药残留单项精准分析仪";//NAD4074    胶体金
//    public static String  oneModule = "农药残留检测仪";//MC4011  分光光度

    //公司名称
    public static String companyName = "W123";//默认的公司名称

    //是否检测App更新

    public final static boolean isDetectionAppUpdate = true;//如果 APP更新是开启的话，那就默认将服务器改为测试服务器
    //public final static boolean isDetectionAppUpdate = false;

    //是否打开测试
    public final static boolean isOpenTest = !isDetectionAppUpdate;//仅控制admin按钮
    //TODO 修改
    //是否模拟器测试
//    public static boolean isSimulatorTest = true;//模拟机测试
    public static boolean isSimulatorTest = false;//真机测试

    //默认服务器
    public static String BASE_URL = "http://www.xindaon.cn:7005/api/";//正式服
//    public static String BASE_URL = "http://www.shionda.com:7005/api/";//正式服
    //public static String BASE_URL = "http://www.shionda.com:7011/api/";//测试服务器

}
