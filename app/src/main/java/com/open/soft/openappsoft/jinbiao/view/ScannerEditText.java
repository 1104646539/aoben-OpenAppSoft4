package com.open.soft.openappsoft.jinbiao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @author：King
 * @time：2020/11/10-18:41
 * @moduleName：适配与扫码枪的二维码，解决中文输入法影响扫码的问题
 * @businessIntroduction：
 * @loadLibrary：GT库
 */
public class ScannerEditText extends AppCompatEditText {
    // 扫码结果
    public String mResult = "";
    // 回调接口
    public ScanResultListener mScanResultListener;

    public void setScanResultListener(ScanResultListener scanResultListener) {
        mScanResultListener = scanResultListener;
    }

    public ScannerEditText(Context context) {
        super(context);
    }

    public ScannerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScannerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {

        if("Virtual".equalsIgnoreCase(event.getDevice().getName())) return super.dispatchKeyEventPreIme(event);
        // 如果想过滤特殊输入设备，则可使用event.getDevice()中的属性过滤
        // 并在非过滤条件后return super.dispatchKeyEventPreIme(event);
        if (0 == event.getUnicodeChar()) return true;

        // 每次按键后累计字符
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            mResult += (char) event.getUnicodeChar();
        }
        // 码枪默认使用KEYCODE_ENTER作为结束标志
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (mScanResultListener != null) mScanResultListener.onScanCompleted(mResult);
            mResult = "";
        }

        return true;
    }

    // 扫码结果回调
    public interface ScanResultListener{
        void onScanCompleted(String result);
    }

}
