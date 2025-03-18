package com.open.soft.openappsoft.util;

/**
 * Created by Administrator on 2017-12-18.
 */

public class Global {

    /**
     * 数据库版本号
     */
    public static final int DATABASE_VERSION = 3;

    /**
     *T2主板通信串口名称
     */
    public static final String COM3_T2 = "/dev/ttyAMA3";
    /**
     *T3主板通信串口名称
     */
    public static final String COM3_T3 = "/dev/ttySAC3";
    /**
     *T2主板打印串口名称
     */
    public static final String COM4_T2 = "/dev/ttyAMA4";
    /**
     *T3主板打印串口名称
     */
    public static final String COM4_T3 = "/dev/ttySAC4";
    /**
     *已经被选中的通信串口的设备标号
     */
    public static int DEV_COM3 = -1;
    /**
     *已经被选中的打印串口的设备标号
     */
    public static int DEV_COM4 = -1;
    /**
     *通信串口波特率
     */
    public static final int BAUD_COM3 = 115200;
    /**
     *打印串口波特率
     */
    public static final int BAUD_COM4 = 9600;
    /**
     * 串口数据位长度
     */
    public static final int SERIAL_PORT_DATABITS = 8;
    /**
     * 串口停止位长度
     */
    public static final int SERIAL_PORT_STOPBITS = 1;


}
