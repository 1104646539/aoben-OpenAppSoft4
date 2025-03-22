package com.open.soft.openappsoft.jinbiao.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.LoginActivity;
import com.open.soft.openappsoft.jinbiao.activity.CheckSelectProjectActivity;
import com.open.soft.openappsoft.jinbiao.activity.SelectActivity;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.friendlyarm.AndroidSDK.HardwareControler;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.open.soft.openappsoft.util.InterfaceURL;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToolUtils {

    public static String PERSION_AND_COMPANY_DB_NAME = "people_db_name.db";
    public static String PORT_KEY = "port_key";
    public static String BOPING_KEY = "boping_key";
    public static String port = "80";
    public static String boping = "9600";

    public static KProgressHUD kp_hud = null;

    public static void showHUD(Context context, String message) {
        KProgressHUD hud = KProgressHUD.create(context);
        hud.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.setLabel(message);
        hud.setCancellable(true);
        hud.show();
        kp_hud = hud;
    }

    public static void hiddenHUD() {
        if (kp_hud != null)
            kp_hud.dismiss();
    }

    public static Bitmap getPicUrlWithBitmap(Context context, String name) {

        Bitmap mp = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            options.inJustDecodeBounds = false;
            String path = context.getFilesDir().getPath() + "/" + name;
            mp = BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mp;
    }

    /**
     * 保存bmp图像
     *
     * @param map
     * @param context
     * @return 图像名称
     */
    public static String savePrivateBmp(Bitmap map, Context context) {
        try {
            String name = System.currentTimeMillis() + ".png";
            FileOutputStream out = context.openFileOutput(name, Context.MODE_PRIVATE);
            byte[] bytes = getBitmapByte(map);
            out.write(bytes);
            out.close();
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getBitmapByte(Bitmap map) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 20, baos);
        return baos.toByteArray();
    }

    public static void deleteAllFile(Context context, String name) {
        File fi = new File(context.getFilesDir().getPath() + "/" + name);
        if (fi.exists()) {
            fi.delete();
        }
    }

    private static String devName = "/dev/ttyAMA3";
    private static int speed = 9600;
    private static int dataBits = 8;
    private static int stopBits = 1;
    public static int devfd = -1;
    public static int devfdprint = -1;
    private static Handler handler;

    // TODO Auto-generated method stub
    public static void OpenSerialPort(Context context, String message,
                                      int fromActiviy) {

        // TODO Auto-generated method stub
        if (fromActiviy == 5) {
            devName = "/dev/ttyAMA4";
            speed = 9600;
            if (devfdprint == -1) {
                devfdprint = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
            }
        } else {
            devName = "/dev/ttyAMA3";
            speed = 115200;
            if (devfd == -1) {
                devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
            }
        }
        if (devfd >= 0) {
            if (message.length() > 0) {
                message = message.toString();
                int ret = HardwareControler.write(devfd, message.getBytes());

                if (ret > 0) {

                    if (fromActiviy == 1) {
                        //定量检测
                        Message messageHandler = new Message();
                        messageHandler.what = 101;
                        handler = CheckSelectProjectActivity.mStartActivityHander;
                        handler.sendMessage(messageHandler);
                    } else if (fromActiviy == 2) {
                        //定性检测
                        Message messageHandler = new Message();
                        messageHandler.what = 102;
                        handler = CheckSelectProjectActivity.mStartActivityHander;
                        handler.sendMessage(messageHandler);
                    } else if (fromActiviy == 4) {
                        //样本图像
                        Message messageHandler = new Message();
                        messageHandler.what = 104;
                        handler = SelectActivity.mStartActivityHander;
                        handler.sendMessage(messageHandler);
                    }

                } else {
                    Toast.makeText(context, "Fail to send!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            devfd = -1;
        }
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    } // string类型转换为date类型

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    public static int[] byte2Int(byte[] data) {

        int[] result = new int[data.length / 2];

        int value;
        int extra = 0;
        if (data.length % 2 != 0) {
            extra = 2;
        }
        for (int i = 0; i < data.length - extra; i += 2) {
            int int1 = (data[i] & 0xff) << 8;
            value = int1 + (data[i + 1] & 0xff);
            result[i / 2] = value;
        }

        return result;
    }

    /**
     * 组装打印数据
     *
     * @param model
     * @param context
     * @return
     */
    public static String GetPrintInfo(ResultModel model, Context context) {

        // 朝内打印
//		return GetPrintInfo(model, context, "1");

        // 朝外打印
        return GetPrintInfo2(model, context, "1");
    }

    /**
     * @param model
     * @param context
     * @param testType 检测类型 1为定量，2为定性
     * @return
     */
    public static String GetPrintInfo(ResultModel model, Context context, String testType) {
        StringBuffer sb = new StringBuffer("\n\n\n\n\n" + context.getResources().getString(R.string.app_name) + "\n\n");

        if (model.id != 0) {
            sb.append("检测流水号：");
            sb.append(model.id + "\n");
        }
        sb.append("检测单位：");
        sb.append(model.company_name + "\n");
        sb.append("检 验 员：");
        sb.append(model.persion + "\n");
//		sb.append("试剂厂商：");
//		sb.append(model. + "\n");
        sb.append("样品名称：");
        sb.append(model.sample_name + "\n");
        sb.append("检测项目：");
        sb.append(model.project_name + "\n");

        sb.append("样品类型：");
        sb.append(model.sample_type + "\n");
        sb.append("商品来源：");
        sb.append(model.sample_unit + "\n");

        sb.append("样品编号：");
        sb.append(model.sample_number + "\n");
        sb.append("检 测 限：");
//		sb.append(model.xian + model.concentrateUnit + "\n");
        sb.append(model.xian + "\n");
//		sb.append("临 界 值：");
//		sb.append(model.lin	+"\n");
        sb.append("检 测 值：");
        sb.append(model.check_value + "\n");

        if (!"2".equals(testType)) {
            sb.append("样品浓度：");
            sb.append(model.style_long + "\n");
        }
        sb.append("检测结果：");
        sb.append(model.check_result + "\n");
        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        model.time, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");
        sb.append("\n\n\n\n\n");

        return sb.toString();
    }


    public static String GetPrintInfo1(ResultModel model, Context context, String testType) {
        String title = LoginActivity.sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            title = InterfaceURL.oneModule;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("\n\n\n");
        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        model.time, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("检测结果：");
        sb.append(model.check_result + "\n");

        if (!"2".equals(testType)) {
            sb.append("样品浓度：");
            sb.append(model.style_long + "\n");
        }

        //增加检测值，20250117
        //sb.append("检 测 值：");
        //sb.append(model.xian +  "\n");

        sb.append("检 测 限：");
        sb.append(model.xian + "\n");

        sb.append("样品编号：");
        sb.append(model.sample_number + "\n");

        // 分割商品来源字符串
//		List list1 = getStrList(model.sample_unit,11);
        List list1 = getStrList1(model.sample_unit);
        Log.d("list1", list1.toString());
        Log.d("list1.size() ", "" + list1.size() + "");

        if (0 < model.sample_unit.length() && model.sample_unit.length() <= 11) {
            sb.append("样品来源：");
            sb.append(list1.get(0) + "\n");
        } else if (11 < model.sample_unit.length() && model.sample_unit.length() <= 27) {
            sb.append(list1.get(1) + "\n");
            sb.append("样品来源：");
            sb.append(list1.get(0) + "\n");
        } else if (27 < model.sample_unit.length() && model.sample_unit.length() <= 43) {
            sb.append(list1.get(0) + "\n");
            sb.append("样品来源：");
            sb.append(list1.get(1) + "\n");
            sb.append(list1.get(2) + "\n");
        }

        sb.append("检测项目：");
        sb.append(model.project_name + "\n");


        sb.append("样品名称：");
        sb.append(model.sample_name + "\n");


        sb.append("样品类型：");
        sb.append(model.sample_type + "\n");


//		sb.append("检 验 员：");
//		sb.append( model.persion +"\n");
//
//
//		sb.append("检测单位：");
//		sb.append( model.company_name +"\n");

        if (model.persion.length() > 11) {
            sb.append(model.persion.substring(11) + "\n");
            sb.append("检 验 员：");
            sb.append(model.persion.substring(0, 11) + "\n");
        } else {
            sb.append("检 验 员：");
            sb.append(model.persion + "\n");
        }
        //20250107 添加被检单位(商品来源字段)
        if (model.sample_unit.length() > 11) {
            sb.append(model.sample_unit.substring(11) + "\n");
            sb.append("被检单位：");
            sb.append(model.sample_unit.substring(0, 11) + "\n");
        } else {
            sb.append("被检单位：");
            sb.append(model.sample_unit + "\n");
        }

//判断打印检测单位名称是否换行
        if (model.company_name.length() > 11) {
            sb.append(model.company_name.substring(11) + "\n");
            sb.append("检测单位：");
            sb.append(model.company_name.substring(0, 11) + "\n");
        } else {
            sb.append("检测单位：");
            sb.append(model.company_name + "\n");
        }


        sb.append(title + "\n\n\n");

        sb.append("\n\n\n");

//		sb.append("检 测 值：");
//		sb.append(model.check_value + "\n");

        //if(model.id != 0) {
        //	sb.append("检测流水号：");
        //	sb.append(model.id + "\n");
        //}


//		sb.append(context.getResources().getString(R.string.app_name) + "\n\n\n\n\n");


        return sb.toString();
    }

    public static String GetPrintInfo2(ResultModel model, Context context, String testType) {

        String title = LoginActivity.sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            title = InterfaceURL.oneModule;
        }

        StringBuffer sb = new StringBuffer();

        sb.append("\n\n\n");

        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        model.time, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("检测结果：");
        sb.append(model.check_result + "\n");

        sb.append("检 测 值：");
        sb.append(model.check_value + "\n");

        if (!"2".equals(testType)) {
            sb.append("样品浓度：");
            sb.append(model.style_long + "\n");
        }

        sb.append("检 测 限：");
        sb.append(model.xian + "\n");

//        sb.append("样品编号：");
//        sb.append(model.sample_number + "\n");
//
//
//        sb.append("样品来源：");
//        sb.append(model.sample_unit + "\n");

        sb.append("检测项目：");
        sb.append(model.project_name + "\n");

        sb.append("样品名称：");
        sb.append(model.sample_name + "\n");


        sb.append("样品类型：");
        sb.append(model.sample_type + "\n");

//		sb.append("检 测 值：");
//		sb.append(model.check_value + "\n");

        sb.append("检 验 员：");
        sb.append(model.persion + "\n");
        //if(model.id != 0) {
        //	sb.append("检测流水号：");
        //	sb.append(model.id + "\n");
        //}

        //20250107 添加被检单位(商品来源字段)
//        if (model.sample_unit.length() > 11) {
//            sb.append(model.sample_unit.substring(11) + "\n");
//            sb.append("被检单位：");
//            sb.append(model.sample_unit.substring(0, 11) + "\n");
//        } else {
        sb.append("被检单位：");
        sb.append(model.sample_unit + "\n");
//        }

//		sb.append("检测单位：");
//		sb.append(model.company_name +"\n");
        if (model.company_name.length() > 11) {
            sb.append(model.company_name.substring(11) + "\n");
            sb.append("检测单位：");
            sb.append(model.company_name.substring(0, 11) + "\n");
        } else {
            sb.append("检测单位：");
            sb.append(model.company_name + "\n");
        }


        sb.append(title + "\n\n\n");

        sb.append("\n\n\n");

        return sb.toString();
    }

    public static String GetPrintInfo3(ResultModel model) {

        String title = LoginActivity.sp_ServiceUrl.query("TitleSet").toString();
        if (title.isEmpty() || title.equals("0")) {
            title = InterfaceURL.oneModule;
        }

        StringBuffer sb = new StringBuffer();

        sb.append("\n\n\n");

        sb.append("检测时间：");
        sb.append(ToolUtils.dateToString(ToolUtils.longToDate(
                        model.time, "yyyy-MM-dd HH:mm:ss"),
                "yyyy-MM-dd HH:mm:ss") + "\n");

        sb.append("检测结果：");
        sb.append(model.check_result + "\n");

        sb.append("检 测 值：");
        sb.append(model.check_value + "\n");

        sb.append("检 测 限：");
        sb.append(model.xian + "\n");

//        sb.append("样品编号：");
//        sb.append(model.sample_number + "\n");
//
//
//        sb.append("样品来源：");
//        sb.append(model.sample_unit + "\n");

        sb.append("检测项目：");
        sb.append(model.project_name + "\n");

        sb.append("样品名称：");
        sb.append(model.sample_name + "\n");


        sb.append("样品类型：");
        sb.append(model.sample_type + "\n");

//		sb.append("检 测 值：");
//		sb.append(model.check_value + "\n");

        sb.append("检 验 员：");
        sb.append(model.persion + "\n");
        //if(model.id != 0) {
        //	sb.append("检测流水号：");
        //	sb.append(model.id + "\n");
        //}

        //20250107 添加被检单位(商品来源字段)
//        if (model.sample_unit.length() > 11) {
//            sb.append(model.sample_unit.substring(11) + "\n");
//            sb.append("被检单位：");
//            sb.append(model.sample_unit.substring(0, 11) + "\n");
//        } else {
        sb.append("被检单位：");
        sb.append(model.sample_unit + "\n");
//        }

//		sb.append("检测单位：");
//		sb.append(model.company_name +"\n");
        sb.append("检测单位：");
        sb.append(model.company_name + "\n");


        sb.append(title + "\n\n\n");

        sb.append("\n\n\n");

        return sb.toString();
    }


    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }


    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static List<String> getStrList1(String inputString) {
        List<String> list = new ArrayList<String>();

        if (0 < inputString.length() && inputString.length() <= 11) {
            list.add(inputString.substring(0, inputString.length()));
        } else if (11 < inputString.length() && inputString.length() <= 27) {
            list.add(inputString.substring(0, 11));
            list.add(inputString.substring(11, inputString.length()));
        } else if (27 < inputString.length() && inputString.length() <= 43) {
            list.add(inputString.substring(0, 11));
            list.add(inputString.substring(11, 27));
            list.add(inputString.substring(27, inputString.length()));
        }

        return list;
    }


}
