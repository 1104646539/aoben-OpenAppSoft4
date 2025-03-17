package com.example.qrcodescan;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Barcode2D {
    public static final int VENDOR_ID = 1317;
    public static final int PRODUCT_ID = 42156;
    public static final int PRODUCT_ID_NEW = 42188;
    public static final String ACTION_USB_PERMISSION = "com.niutu.barcode2d.USB_PERMISSION";
    public static final int BARDCODE_2D_CMD_START = 1;
    public static final int BARDCODE_2D_CMD_STOP = 2;
    private Context context = null;
    private UsbManager myUsbManager = null;
    private UsbDevice myUsbDevice = null;
    private UsbInterface myUsbInterface = null;
    private UsbDeviceConnection myUsbDeviceConnection = null;
    private UsbEndpoint myUsbEndpointIn = null;
    private UsbEndpoint myUsbEndpointOut = null;
    private static final int REPORT_LEN = 64;
    private static final String LOG_TAG = "libbarcode";

    public Barcode2D(Context ctxt) {
        this.context = ctxt;
    }

    public int open_barcode2d_dev() {
        if (this.myUsbManager == null) {
            this.myUsbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
            if (this.myUsbManager == null) {
                this.releaseUsbResource();
                return -1;
            }
        }

        if (this.myUsbDevice == null) {
            HashMap<String, UsbDevice> hm = this.myUsbManager.getDeviceList();
            Set<String> keys = hm.keySet();
            Iterator<String> kis = keys.iterator();
            while (kis.hasNext()) {
                UsbDevice ud = hm.get(kis.next());
                if (ud != null) {
                    int pid = ud.getProductId();
                    if (pid == 42188) {
                        this.myUsbDevice = ud;
                    }
                }
            }
//            HashMap<String, UsbDevice> deviceList = this.myUsbManager.getDeviceList();
//            if (!deviceList.isEmpty()) {
//                Iterator var2 = deviceList.values().iterator();
//
//                label69:
//                while (true) {
//                    UsbDevice device;
//                    int vendorId;
//                    int productId;
//                    do {
//                        if (!var2.hasNext()) {
//                            break label69;
//                        }
//
//                        device = (UsbDevice) var2.next();
//                        vendorId = device.getVendorId();
//                        productId = device.getProductId();
//                    } while ((vendorId != 1317 || productId != 42156) && (vendorId != 1317 || productId != 42188));
//
//                    this.myUsbDevice = device;
//                }
//            }

            if (this.myUsbDevice == null) {
                this.releaseUsbResource();
                return -1;
            }
        }

        if (this.myUsbInterface == null) {
            this.myUsbInterface = this.myUsbDevice.getInterface(0);
            if (this.myUsbInterface == null) {
                this.releaseUsbResource();
                return -1;
            }
        }

        if (this.myUsbDeviceConnection == null) {
            Log.d("myUsbDevice", "myUsbDevice=" + myUsbDevice);
            if (!this.myUsbManager.hasPermission(this.myUsbDevice)) {
                PendingIntent pi = PendingIntent.getBroadcast(this.context, 0, new Intent("com.niutu.barcode2d.USB_PERMISSION"), 0);
                this.myUsbManager.requestPermission(this.myUsbDevice, pi);
                this.releaseUsbResource();
                return 1;
            }

            if (myUsbManager != null) {
                this.myUsbDeviceConnection = this.myUsbManager.openDevice(this.myUsbDevice);
            }
            if (this.myUsbDeviceConnection == null) {
                this.releaseUsbResource();
                return -1;
            }

            if (this.myUsbInterface == null) {
                this.releaseUsbResource();
                return -1;
            }
            if (!this.myUsbDeviceConnection.claimInterface(this.myUsbInterface, true)) {
                this.releaseUsbResource();
                return -1;
            }
        }

        if (this.myUsbEndpointIn == null) {
            this.myUsbEndpointIn = this.myUsbInterface.getEndpoint(0);
            if (this.myUsbEndpointIn == null) {
                this.releaseUsbResource();
                return -1;
            }
        }

        if (this.myUsbEndpointOut == null) {
            this.myUsbEndpointOut = this.myUsbInterface.getEndpoint(1);
            if (this.myUsbEndpointOut == null) {
                this.releaseUsbResource();
                return -1;
            }
        }

        return 0;
    }

    public int cmd_barcode2d_dev(int cmd, int waitTime) {
        int ret = 0;
        byte cmdLen = 0;
        byte[] cmdBuf = new byte[64];
        if (this.myUsbDeviceConnection != null && this.myUsbEndpointIn != null) {
            Arrays.fill(cmdBuf, (byte) 0);
            switch (cmd) {
                case 1:
                    cmdBuf[2] = 22;
                    cmdBuf[3] = 84;
                    cmdBuf[4] = 13;
                    cmdBuf[5] = 33;
                    cmdLen = 4;
                    break;
                case 2:
                    cmdBuf[2] = 22;
                    cmdBuf[3] = 85;
                    cmdBuf[4] = 13;
                    cmdBuf[5] = 33;
                    cmdLen = 4;
                    break;
                default:
                    ret = -1;
            }

            cmdBuf[0] = 4;
            cmdBuf[1] = cmdLen;
            cmdBuf[63] = 0;
            if (ret == 0) {
                int num;
                if (waitTime < 0) {
                    num = this.myUsbDeviceConnection.bulkTransfer(this.myUsbEndpointOut, cmdBuf, 64, 3600000);
                } else {
                    num = this.myUsbDeviceConnection.bulkTransfer(this.myUsbEndpointOut, cmdBuf, 64, waitTime);
                }

                if (num != 64) {
                    ret = -2;
                }
            }

            return ret;
        } else {
            return -2;
        }
    }

    public String read_barcode2d_dev(int waitTime) {
        byte[] packet = new byte[64];
        Log.d("read_barcode2d_dev", "read_barcode2d_dev myUsbDeviceConnection=" + myUsbDeviceConnection + "myUsbEndpointIn=" + myUsbEndpointIn);
        if (this.myUsbDeviceConnection != null && this.myUsbEndpointIn != null) {
            int num;
            if (waitTime == 0) {
                num = this.myUsbDeviceConnection.bulkTransfer(this.myUsbEndpointIn, packet, 64, 0);
            } else if (waitTime < 0) {
                while (true) {
                    num = this.myUsbDeviceConnection.bulkTransfer(this.myUsbEndpointIn, packet, 64, 3600000);
                    if (num == 0) {
                        continue;
                    }
                }
            } else {
                num = this.myUsbDeviceConnection.bulkTransfer(this.myUsbEndpointIn, packet, 64, waitTime);
            }

            if (num == 0) {
                return "";
            } else if (num < 2) {
                return null;
            } else if (num != 64) {
                return null;
            } else if (packet[1] > 62) {
                return null;
            } else {
                String str = new String(packet, 2, packet[1]);
                return str;
            }
        } else {
            return null;
        }
    }

    public int close_barcode2d_dev() {
        this.releaseUsbResource();
        return 0;
    }

    private void releaseUsbResource() {
        if (this.myUsbDeviceConnection != null) {
            if (this.myUsbInterface != null) {
                this.myUsbDeviceConnection.releaseInterface(this.myUsbInterface);
            }

            this.myUsbDeviceConnection.close();
        }

        this.myUsbInterface = null;
        this.myUsbDeviceConnection = null;
        this.myUsbManager = null;
        this.myUsbDevice = null;
        this.myUsbEndpointIn = null;
        this.myUsbEndpointOut = null;
    }
}
