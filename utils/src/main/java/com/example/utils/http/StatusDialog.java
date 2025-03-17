package com.example.utils.http;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.R;
//import com.gsls.gt.GT;

public class StatusDialog {
    /**
     * 开始扫描样品二维码
     */
    public final static int STATUS_START_SCAN_QRCODE_SAMPLE = 11;
    /**
     * 结束扫描样品二维码
     */
    public final static int STATUS_END_SCAN_QRCODE_SAMPLE = 12;

    /**
     * 开始验证样品二维码
     */
    public final static int STATUS_START_VERITY_QRCODE_SAMPLE = 13;
    /**
     * 结束验证样品二维码
     */
    public final static int STATUS_END_VERITY_QRCODE_SAMPLE = 14;

    /**
     * 开始扫描检测卡二维码
     */
    public final static int STATUS_START_SCAN_QRCODE_CARD = 21;
    /**
     * 结束扫描检测卡二维码
     */
    public final static int STATUS_END_SCAN_QRCODE_CARD = 22;

    /**
     * 开始验证检测卡二维码
     */
    public final static int STATUS_START_VERITY_QRCODE_CARD = 23;
    /**
     * 结束验证检测卡二维码
     */
    public final static int STATUS_END_VERITY_QRCODE_CARD = 24;
    public int status = STATUS_START_SCAN_QRCODE_SAMPLE;
    Context context;
    Activity activity;
    Dialog progressDialog;

    TextView tv_hint, tv_hint_et;
    EditText et_input;
    ScannerEditText scannerEditText;
    Button btn_cancel, btn_confirm;
    View root;

    public StatusDialog(final Context context) {
        this.context = context;
        progressDialog = new Dialog(context);
        root = LayoutInflater.from(context).inflate(R.layout.dialog_status_check, null, false);
        progressDialog.show();
        progressDialog.setContentView(root);
        tv_hint = (TextView) root.findViewById(R.id.tv_hint);
        tv_hint_et = (TextView) root.findViewById(R.id.tv_hint_et);
        et_input = (EditText) root.findViewById(R.id.et_input);
        scannerEditText = root.findViewById(R.id.et_input2);




        scannerEditText.setScanResultListener(new ScannerEditText.ScanResultListener() {
            @Override
            public void onScanCompleted(String result) {
                et_input.setText(result);
                scannerEditText.setText(result);
            }
        });

        btn_cancel = (Button) root.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) root.findViewById(R.id.btn_confirm);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();



        statusChange();
    }

    public StatusDialog(final Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        progressDialog = new Dialog(context);
        root = LayoutInflater.from(context).inflate(R.layout.dialog_status_check, null, false);
        progressDialog.show();
        progressDialog.setContentView(root);
        tv_hint = (TextView) root.findViewById(R.id.tv_hint);
        tv_hint_et = (TextView) root.findViewById(R.id.tv_hint_et);
        et_input = (EditText) root.findViewById(R.id.et_input);
        scannerEditText = root.findViewById(R.id.et_input2);

        scannerEditText.setScanResultListener(new ScannerEditText.ScanResultListener() {
            @Override
            public void onScanCompleted(String result) {
                et_input.setText(result);
                scannerEditText.setText(result);
            }
        });

        btn_cancel = (Button) root.findViewById(R.id.btn_cancel);
        btn_confirm = (Button) root.findViewById(R.id.btn_confirm);
        progressDialog.setCancelable(false);
        progressDialog.dismiss();
        statusChange();
    }

    public void setStatus(int status) {
        this.status = status;
        statusChange();
        if (onProgressStatus != null) {
            onProgressStatus.onProgressStatusChange(status);
        }
    }

    public void show() {
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }


    private View view1;

    private void statusChange() {

        switch (status) {
            case STATUS_START_SCAN_QRCODE_SAMPLE:
                tv_hint.setText("请扫描样本二维码或手动输入样本编号");
                tv_hint_et.setText("样本编号:");
                btn_confirm.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                et_input.setText("");
                // 主动获取焦点
                scannerEditText.requestFocus();
                scannerEditText.setText("");
                tv_hint_et.setVisibility(View.VISIBLE);



//                et_input.setVisibility(View.VISIBLE);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null || et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (onProgressStatus != null) {
                            onProgressStatus.onConfirm(scannerEditText.getText().toString());
//                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }


                    }
                });
                break;
            case STATUS_END_SCAN_QRCODE_SAMPLE:

                tv_hint.setText("扫描成功，请等待");
                tv_hint_et.setText("");
//                scannerEditText.setText("");
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_hint_et.setVisibility(View.GONE);
                et_input.setVisibility(View.GONE);
                et_input.setText("");
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (onProgressStatus != null) {
//                            GT.logs("2号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }
                    }
                });
                break;
            case STATUS_START_VERITY_QRCODE_SAMPLE:

                tv_hint.setText("正在验证样品二维码，请等待");
//                tv_hint_et.setText("");
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_hint_et.setVisibility(View.GONE);
                et_input.setText("");
                scannerEditText.setText("");
                et_input.setVisibility(View.GONE);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (onProgressStatus != null) {
//                            GT.logs("3号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }
                    }
                });
                break;
            case STATUS_END_VERITY_QRCODE_SAMPLE:

                tv_hint.setText("验证样品二维码完毕");
                tv_hint_et.setText("");
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_hint_et.setVisibility(View.GONE);
                et_input.setText("");
//                scannerEditText.setText("");
                et_input.setVisibility(View.GONE);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (onProgressStatus != null) {
//                            GT.logs("4号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }
                    }
                });
                break;
            case STATUS_START_SCAN_QRCODE_CARD:

                tv_hint.setText("请扫描检测卡/试剂盒二维码");
                tv_hint_et.setText("检测卡:");
                btn_confirm.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_hint_et.setVisibility(View.VISIBLE);
                et_input.setText("");
                // 主动获取焦点
                scannerEditText.requestFocus();
                scannerEditText.setText("");
                et_input.setVisibility(View.VISIBLE);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (onProgressStatus != null) {
//                            GT.logs("5号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }
                    }
                });
                break;
            case STATUS_END_SCAN_QRCODE_CARD:
                tv_hint.setText("扫描成功，请等待");
                tv_hint_et.setText("");
//                scannerEditText.setText("");
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                et_input.setText("");
                tv_hint_et.setVisibility(View.GONE);
                et_input.setVisibility(View.GONE);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (onProgressStatus != null) {
//                            GT.logs("6号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }
                    }
                });
                break;
            case STATUS_START_VERITY_QRCODE_CARD:
                tv_hint.setText("正在验证检测卡二维码，请等待");
                tv_hint_et.setText("");
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_hint_et.setVisibility(View.GONE);
                et_input.setText("");
//                scannerEditText.setText("");
                et_input.setVisibility(View.GONE);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (onProgressStatus != null) {
//                            GT.logs("7号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                            et_input.setText("");
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                            et_input.setText("");
                        }
                    }
                });
                break;

            case STATUS_END_VERITY_QRCODE_CARD:
                tv_hint.setText("验证检测卡二维码完毕");
                tv_hint_et.setText("");
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                tv_hint_et.setVisibility(View.GONE);
                et_input.setVisibility(View.GONE);
                et_input.setText("");
//                scannerEditText.setText("");
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                          if (et_input.getText() == null|| et_input.getText().toString().isEmpty()) {
//                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (scannerEditText.getText() == null || scannerEditText.getText().toString().isEmpty()) {
                            Toast.makeText(context, "请输入编号", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (onProgressStatus != null) {
//                            GT.logs("8号");
                            onProgressStatus.onConfirm(et_input.getText().toString());
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onProgressStatus != null) {
                            onProgressStatus.onCancel(status);
                        }
                    }
                });
                break;
        }
    }

    public void setMessage(String msg) {
        if (msg != null) {
//            progressDialog.setMessage(msg);
            if (tv_hint != null) {
                tv_hint.setText(msg);
            }
        }

    }

    public int getStatus() {
        return status;
    }

    OnProgressStatusChange onProgressStatus;

    public void setOnProgressStatus(OnProgressStatusChange onProgressStatus) {
        this.onProgressStatus = onProgressStatus;
    }

    public interface OnProgressStatusChange {
        void onProgressStatusChange(int status);

        void onCancel(int status);

        void onConfirm(String number);

    }


}


