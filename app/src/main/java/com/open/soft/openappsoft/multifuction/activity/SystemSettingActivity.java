package com.open.soft.openappsoft.multifuction.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.multifuction.model.CheckResult;
import com.open.soft.openappsoft.multifuction.model.Project;
import com.open.soft.openappsoft.multifuction.resource.SPResource;
import com.open.soft.openappsoft.multifuction.util.APPUtils;
import com.open.soft.openappsoft.multifuction.util.Global;
import com.open.soft.openappsoft.multifuction.util.SerialUtils;

/**
 * 系统设置
 */
public class SystemSettingActivity extends Activity implements View.OnClickListener {


    //    private Button btnLocation;
//    private Button btnUploadSetting;
    private Button btnSample;
    private Button btnProject;
    private Button btnDebug;
    private int debugClickCount;
    private Activity act;
    private Button btnIdSetting;
    private Button btnUploadSetting;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        act = this;
        sp = getSharedPreferences(SPResource.FILE_NAME, MODE_PRIVATE);
        btnIdSetting = (Button) findViewById(R.id.btn_id_setting);
        btnUploadSetting = (Button) findViewById(R.id.btn_upload_setting);
        btnDebug = (Button) findViewById(R.id.btn_debug);
        btnDebug.setOnClickListener(this);
        btnProject = (Button) findViewById(R.id.btn_project);
        btnProject.setOnClickListener(this);
//        btnLocation = findViewById(R.id.btn_location);
//        btnLocation.setOnClickListener(this);
        btnSample = (Button) findViewById(R.id.btn_sample);
        btnSample.setOnClickListener(this);
        btnIdSetting.setOnClickListener(this);
        btnUploadSetting.setOnClickListener(this);
//        btnUploadSetting = findViewById(R.id.btn_upload_setting);
//        btnUploadSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_id_setting) {
            showIdSettingDialog();
        } else if (i == R.id.btn_sample) {
            startActivity(new Intent(this, SampleActivity.class));
        } else if (i == R.id.btn_upload_setting) {
            showUploadSettingDialog();
        } else if (i == R.id.btn_project) {
            startActivity(new Intent(this, ProjectActivity.class));
        } else if (i == R.id.btn_debug) {
            if (debugClickCount == 0) {
                debugClickCount++;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        debugClickCount = 0;
                    }
                }, 2000);
            }
            if (debugClickCount > 5) {
                debugClickCount = 0;
                showDebugPwdDialog();
            } else {
                debugClickCount++;
            }
        }
    }

    private void showIdSettingDialog() {

        final EditText et = new EditText(this);
        et.setText(Global.device_id);
        new AlertDialog.Builder(this)
                .setView(et)
                .setMessage("请设置设备ID")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.edit().putString(SPResource.KEY_DEVICE_ID, et.getText().toString()).apply();
                        Global.device_id = et.getText().toString();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void showUploadSettingDialog() {

        View inflate = View.inflate(this, R.layout.dialog_upload_setting, null);
        final EditText et = (EditText) inflate.findViewById(R.id.et_upload_url);
        final RadioButton rbAutoUpload = (RadioButton) inflate.findViewById(R.id.rb_auto_upload);
        final RadioButton rbManualUpload = (RadioButton) inflate.findViewById(R.id.rb_manual_upload);
        if (Global.uploadModel == 1) {
            rbAutoUpload.setChecked(true);
        } else {
            rbManualUpload.setChecked(true);
        }
//        et.setText(Global.BASE_URL);
        new AlertDialog.Builder(this)
                .setView(inflate)
                .setTitle("上传设置")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(SPResource.KEY_UPLOAD_URL, et.getText().toString());
                        if (rbAutoUpload.isChecked()) {
                            editor.putInt(SPResource.KEY_UPLOAD_MODE, 1).apply();
                            Global.uploadModel = 1;
                        } else {
                            editor.putInt(SPResource.KEY_UPLOAD_MODE, 2).apply();
                            Global.uploadModel = 2;
                        }
//                        Global.BASE_URL = et.getText().toString();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void showDebugPwdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText etPwd = new EditText(this);
//        et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPwd.setHint("请输入密码");
        builder.setView(etPwd);
        builder.setTitle("密码")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = etPwd.getText().toString();
                        if (TextUtils.isEmpty(pwd) || !"TM1111".equals(pwd)) {
                            APPUtils.showToast(act, "密码错误");
                        } else {
                            showDebugInfoDialog();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    private void showDebugInfoDialog() {

        View view = View.inflate(act, R.layout.dialog_debug_info, null);
        final EditText etcardWarmTime = (EditText) view.findViewById(R.id.et_card_test_time);
        final EditText etfenguangReactionTime = (EditText) view.findViewById(R.id.et_fenguang_test_time);
        final EditText etReactionTime = (EditText) view.findViewById(R.id.et_card_reaction_time);
        etcardWarmTime.setText(Global.cardWarmTime + "");
        etfenguangReactionTime.setText(Global.fenguangReactionTime + "");
        etReactionTime.setText(Global.cardReactionTime + "");
        Button btnSetDefaultSetting = (Button) view.findViewById(R.id.btn_set_default_setting);
        Button btnFit = (Button) view.findViewById(R.id.btn_fit);
        btnFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SerialUtils.COM3_SendData(Global.FIT_LIGHT)) {
                    APPUtils.showToast(act, "校准失败");
                }
            }
        });
        btnSetDefaultSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckResult().deleteAll(CheckResult.class);
                new Project().deleteAll(Project.class);
                Global.cardWarmTime = 3 * 60;
                Global.fenguangReactionTime = 3 * 60;
                Global.cardReactionTime = 10 * 60;
            }
        });
        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        Button btnOpenDebug = (Button) view.findViewById(R.id.btn_open_debug);

        final Dialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.getWindow().setWindowAnimations(R.style.DialogAnim);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etcardWarmTime.getText().toString().trim())
                        || TextUtils.isEmpty(etReactionTime.getText().toString().trim())) {
                    APPUtils.showToast(act, "数据不允许为空");
                    return;
                }
                int cardWarmTime, cardReactionTime, fenguangReactionTime, limitValue;
                try {
                    cardWarmTime = Integer.parseInt(etcardWarmTime.getText().toString().trim());
                    cardReactionTime = Integer.parseInt(etReactionTime.getText().toString().trim());
                    fenguangReactionTime = Integer.parseInt(etfenguangReactionTime.getText().toString().trim());
                    if (cardWarmTime < 30 || cardWarmTime > 180) {
                        APPUtils.showToast(act, "卡片检测时间必须大于30且小于180s");
                        return;
                    }
                    if (fenguangReactionTime < 10 || fenguangReactionTime > 180) {
                        APPUtils.showToast(act, "分光检测时间必须大于10且小于180s");
                        return;
                    }
                    if (cardReactionTime < 10 || cardReactionTime > 600) {
                        APPUtils.showToast(act, "卡片反应时间必须大于10且小于600s");
                        return;
                    }
                    Global.fenguangReactionTime = fenguangReactionTime;
                    Global.cardWarmTime = cardWarmTime;
                    Global.cardReactionTime = cardReactionTime;
                    SharedPreferences.Editor editor = getSharedPreferences(SPResource.FILE_NAME, MODE_PRIVATE).edit();
                    if (Global.DEBUG) {
                        editor.putInt(SPResource.KEY_CARD_REACTION_TIME, cardReactionTime);
                        editor.putInt(SPResource.KEY_CARD_WARM_TIME, cardWarmTime).apply();
                        editor.putInt(SPResource.KEY_FENGUANG_REACTION_TIME, fenguangReactionTime).apply();
                    }

//                    editor.putInt(SPResource.KEY_CARD_TEST_LIMIT_VALUE, Global.limitValue).apply();
                    APPUtils.showToast(act, "保存成功");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    APPUtils.showToast(act, "请输入整数");
                }
            }
        });
        btnOpenDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(act, DebugActivity.class));
            }
        });
        dialog.show();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
}
