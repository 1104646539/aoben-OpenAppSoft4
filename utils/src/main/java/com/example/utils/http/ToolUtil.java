package com.example.utils.http;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class ToolUtil {


    public final static String DateTime1 = "yyyy-MM-dd HH:mm:ss";
    public final static String Date1 = "yyyy-MM-dd";
    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

//    public static int[] byte2Int(byte[] data){
//
//        int[] result = new int[data.length/2];
//        int value;
//        int end = data.length;
//        if (data.length % 2 != 0) {
//            end--;
//        }
//        for (int i = 0; i < end; i+=2) {
//            int int1 = (data[i] & 0xff) << 8;
//            value = int1 + (data[i + 1] & 0xff);
//            result[i / 2] = value;
//        }
//
//        return result;
//    }

    public static int[] byte2Int(byte[] data) {

        int[] result = new int[data.length / 3];
        int end = data.length;
        int remainder = 0;
        if ((remainder = data.length % 3) != 0) {
            end -= remainder;
        }
        for (int i = 0; i < end; i += 3) {
            int int1 = (data[i] & 0xff) << 16;
            int int2 = (data[i + 1] & 0xff) << 8;
            int int3 = (data[i + 2] & 0xff);
            result[i / 3] = int1 + int2 + int3;
        }

        return result;
    }

    public static String date2String(Date date, String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String long2String(String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static String long2String(long time, String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return simpleDateFormat.format(new Date(time));
    }

    public static String doubleToString(int scale, double value) {
        return String.valueOf(doubleToDouble(scale, value));
    }

    public static int doubleToInt(int scale, double value) {
        return Integer.valueOf((int) doubleToDouble(scale, value));
    }

    public static double doubleToDouble(int scale, double value) {
        try {
            BigDecimal b = new BigDecimal(value);
            value = b.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return value;
    }

    public static String nullToString(String msg) {
        return nullToString(msg, "");
    }

    public static String nullToString(Integer msg) {
        return nullToString(msg, "");
    }

    public static String nullToString(Integer msg, String defaults) {
        if (msg == null) {
            return nullToString(defaults);
        }
        return nullToString(String.valueOf(msg));
    }

    public static String nullToString(Long msg) {
        return nullToString(msg, "");
    }

    public static String nullToString(Long msg, String defaults) {
        if (msg == null) {
            return nullToString(defaults);
        }
        return nullToString(String.valueOf(msg));
    }

    public static String nullToString(Double msg) {
        return nullToString(String.valueOf(msg), "");
    }

    public static String nullToString(Double msg, String defaults) {
        if (msg == null) {
            return nullToString(defaults);
        }
        return nullToString(String.valueOf(msg));
    }

    public static String nullToString(Float msg, String defaults) {
        if (msg == null) {
            return nullToString(defaults);
        }
        return nullToString(String.valueOf(msg));
    }

    public static String nullToString(String msg, String defalut) {
        if (msg == null) {
            return defalut;
        }
        return msg;
    }

    public static int nullToInt(String msg, int defalut) {
        if (msg == null) {
            return defalut;
        }
        return Integer.valueOf(msg);
    }

    public static int nullToInt(String msg) {
        return nullToInt(msg, 0);
    }

    public static double nullToDouble(String msg, double defalut) {
        if (msg == null) {
            return defalut;
        }
        return Double.valueOf(msg);
    }

    public static double nullToDouble(String msg) {
        return nullToDouble(msg, 0);
    }

    public static double nullToDouble(float value) {
        return nullToDouble(new String(value + ""), 0);
    }

    public static float nullToFloat(String msg, float defalut) {
        if (msg == null) {
            return defalut;
        }
        return Float.valueOf(msg);
    }

    public static float nullToFloat(String msg) {
        return nullToFloat(msg, 0);
    }

    public static String printStrings(String[] args) {
        if (args == null || args.length == 0) {
            return "null";
        }
        String str = "";
        for (int i = 0; i < args.length; i++) {
            str += args[i] + "";
            if (i != args.length - 1) {
                str += ",";
            }
        }
        return str;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]+([.]{1}[0-9]+){0,1}$");
        return pattern.matcher(str).matches();
    }

    public static String isNotNumericReturnDefault(String str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        return isNumeric(str) ? str : defaultValue;
    }

    /**
     * 是否存在扫码枪
     *
     * @param context
     * @return
     */
    public static boolean isQrCodeDevice(Context context) {
        UsbManager um = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> hm = um.getDeviceList();
        if (hm.isEmpty()) {
            return false;
        }
        Set<String> keys = hm.keySet();
        Iterator<String> kis = keys.iterator();
        while (kis.hasNext()) {
            UsbDevice ud = hm.get(kis.next());
            if (ud != null) {
                int pid = ud.getProductId();
                Log.d("isQrCodeDevice", "isQrCode pid=" + pid);
                if (pid == 42188) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getSampleQrCode(String code) {
        String sampleQRCode = "";
        if (code == null || code.isEmpty()) {
            return sampleQRCode;
        }
        int start = code.indexOf("SampleNumber=");
        if (start == -1) {//没有SampleNumber=
            return sampleQRCode;
        }
        start += "SampleNumber=".length();
        int end = code.indexOf("&", start);
        if (end == -1) {//没有&
            end = code.length();
        }
        sampleQRCode = code.substring(start, end);
        sampleQRCode = sampleQRCode.replace("\r\n", "");
        return sampleQRCode;
    }

}
