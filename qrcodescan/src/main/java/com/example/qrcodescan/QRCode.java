package com.example.qrcodescan;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.TextView;

//import com.gsls.gt.GT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class QRCode {
    public static final String QRCODE_LOCAL_MSG_SAMPLE = "QRCODE_LOCAL_MSG_SAMPLE";
    public static final String QRCODE_LOCAL_MSG_CARD = "QRCODE_LOCAL_MSG_CARD";
    public static final String QRCODE_LOCAL_MSG = "QRCODE_LOCAL_MSG";
    public final static String TAG = "QRCode";
    //qrcode
    private Barcode2D barcode2D = null;
    private boolean usbDialogFinish = true;
    /**
     * 每次扫码成功都会返回的 T /r/n 屏蔽掉
     */
    private byte[] T = new byte[]{84, 6, 13, 10};
    /**
     * 每次关闭扫码功能（超时）都会返回的 U /r/n 屏蔽掉
     */
    private byte[] U = new byte[]{85, 6, 13, 10};
    /**
     * 每次扫码成功都会返回的 T /r/n 屏蔽掉
     */
    private byte[] T_Not_N = new byte[]{84, 6};
    /**
     * 每次关闭扫码功能（超时）都会返回的 U /r/n 屏蔽掉
     */
    private byte[] U_Not_N = new byte[]{85, 6};

    //region usb广播具体操作，暂时不用
    private PendingIntent mPermissionIntent = null;
    private static final String ACTION_USB_PERMISSION_BASE = "com.tnd.USB_PERMISSION.";
    private final String ACTION_USB_PERMISSION = ACTION_USB_PERMISSION_BASE + hashCode();
    private TextView tv_check_company;
    private TextView tv_check_persion;
    private TextView tv_check_sample;
    private TextView tv_check_project;
    private TextView tv_check_type;
    private Context context;
    private int requestCode = 3210;
    UsbManager um;

    public QRCode(Context context) {
        this.context = context;
        um = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    /**
     * register BroadcastReceiver to monitor USB events
     *
     * @throws
     */
    public synchronized void register() {
        if (mPermissionIntent == null) {
            mPermissionIntent = PendingIntent.getBroadcast(context, requestCode, new Intent(ACTION_USB_PERMISSION), 0);
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            // ACTION_USB_DEVICE_ATTACHED never comes on some devices so it should not be added here
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            // android.app.IntentReceiverLeaked: Activity com.open.soft.openappsoft.activity.MainActivity has leaked IntentReceiver com.example.qrcodescan.QRCode$1@107e1bd2 that was originally registered here. Are you missing a call to unregisterReceiver()?
            context.registerReceiver(mUsbReceiver, filter);
        }
    }

    //销毁USB广播
    public void unregisterReceiverUsb(Context context){
        context.unregisterReceiver(mUsbReceiver);
    }

    /**
     * BroadcastReceiver for USB permission
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            Log.e(TAG, "ACTION=================" + action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    Log.e(TAG, "getProductId：" + device.getProductId());
                    if (device.getProductId() == 41234 || device.getProductId() == 1) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            Log.e(TAG, "获取权限成功：" + device.getDeviceName());
                        } else {
                            Log.e(TAG, "获取权限失败：" + device.getDeviceName());
                        }
                    } else {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            Log.e(TAG, "获取权限成功2：" + device.getDeviceName());
                            usbDialogFinish = true;
                            startScanListener();
                        } else {
                            Log.e(TAG, "获取权限失败2：" + device.getDeviceName());

                        }
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                final UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.i(TAG, "USB设备已插入==============  " + device.toString() + "\n\n");
                if (isQrCodeDevice(context)) {
                    Log.i(TAG, "USB设备已插入============== 是扫码枪,正在请求权限 " + device.toString() + "\n\n");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                    context.registerReceiver(mUsbReceiver, filter);
                    um.requestPermission(device, pendingIntent);
                }
//                startScanListener();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.d(TAG, "USB设备拔出==============");
                if (!isQrCodeDevice(context)) {

                    Log.d(TAG, "USB设备拔出==============isQrCodeDevice = false");
                    if (barcode2D != null) {
                        barcode2D.close_barcode2d_dev();
                        barcode2D = null;
                    }
                }
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED) || action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.d(TAG, "onReceive: " + "U盘移除了");
            }
        }
    };
    private static final String MOUNTS_FILE = "/proc/mounts";

    public void startScanListener() {
        if (!isQrCodeDevice(context)) {
            Log.d(TAG, "startScanListener isQrCodeDevice=false");
            return;
        }
//        usbDialogFinish = true;
        final StringBuilder stringBuilder = new StringBuilder();
        new Thread() {
            public void run() {
                while (true) {
                    while (true) {
                        if (usbDialogFinish) {
                            try{
                                if(barcode2D == null){
                                    barcode2D = new Barcode2D(context);
                                }
                                int r = barcode2D.open_barcode2d_dev();
                                if (r == 0) {
                                    Log.d(TAG, "二维码设备连接成功 usbDialogFinish=" + usbDialogFinish);
                                    usbDialogFinish = false;
                                    break;
                                } else if (r == 1) {
                                    Log.d(TAG, "正在授权 usbDialogFinish=" + usbDialogFinish);
                                }
                            }catch (NullPointerException e){
                            }
                        }
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    while (true) {
                        String str = barcode2D.read_barcode2d_dev(0);
                        if (str != null) {
                            Log.d(TAG, "扫码成功=" + str);
                            for (int i = 0; i < str.getBytes().length; i++) {
                                Log.d(TAG, "str.getBytes()=" + str.getBytes()[i]);
                            }

                            if (Arrays.equals(str.getBytes(), T) ||
                                    Arrays.equals(str.getBytes(), U) ||
                                    Arrays.equals(str.getBytes(), U_Not_N) ||
                                    Arrays.equals(str.getBytes(), T_Not_N)) {
                                continue;
                            }
                            stringBuilder.append(str);
                            str = stringBuilder.toString();
                            Log.d(TAG, "isScaning str=" + str);
                            if (str.contains("\r\n")) {
                                Log.d(TAG, "isScaning 带换行的的");
                                int index = str.indexOf("\r\n");
                                String temp = stringBuilder.substring(0, index + 2);
                                stringBuilder.delete(0, index + 1);
                                scanParameterSuccess(temp);
                            }
                        } else {
                            Log.d(TAG, "二维码设备读取失败");
//                            barcode2D.close_barcode2d_dev();
                            break;
                        }
                    }
                }
            }
        }.start();
    }

    public String sampleCode = "";
    public String cardCode = "";

    private void scanParameterSuccess(final String temp) {
//                Toast.makeText(CheckActivity.this, "scanParameterSuccess=" + temp, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "scanParameterSuccess temp=" + temp);
        String temp2 = temp.replaceAll("\r", "");
        temp2 = temp2.replaceAll("\n", "");
        if (oScanParameterSuccess != null) {
            oScanParameterSuccess.oScanParameterSuccess(temp2);
        }
//                if (statusDialog != null) {
//                    if (statusDialog.getStatus() == StatusDialog.STATUS_START_SCAN_QRCODE_SAMPLE) {
//                        sampleCode = ToolUtil.getSampleQrCode(temp2);
//                        if (sampleCode == null || sampleCode.isEmpty()) {
//
//                        }
//                        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_SAMPLE);
//                        checkPresenter.GetSamplingInfo(new GetSamplingInfoBean(sampleCode,com.example.utils.http.Global.admin_pt), 1, CheckActivity.this);
//                        statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_SAMPLE);
//                        et_Sample_Num.setText(sampleCode);
//                    } else if (statusDialog.getStatus() == StatusDialog.STATUS_START_SCAN_QRCODE_CARD) {
//                        cardCode = temp2;
//                        statusDialog.setStatus(StatusDialog.STATUS_END_SCAN_QRCODE_CARD);
//                        checkPresenter.GetCardQRInfo(new GetQRInfoBean(cardCode), 1, CheckActivity.this);
//                        statusDialog.setStatus(StatusDialog.STATUS_START_VERITY_QRCODE_CARD);
//                    }
//                }
    }

    OnScanParameterSuccess oScanParameterSuccess;

    public void setoScanParameterSuccess(OnScanParameterSuccess oScanParameterSuccess) {
        this.oScanParameterSuccess = oScanParameterSuccess;
    }

    public interface OnScanParameterSuccess {
        void oScanParameterSuccess(String msg);
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
}
