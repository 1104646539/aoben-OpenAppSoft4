package com.open.soft.openappsoft.multifuction.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.utils.http.Global;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.activity.LoginActivity;
import com.open.soft.openappsoft.util.InterfaceURL;

/**
 * 上传网址设置
 */
public class EditURLDialog extends Dialog {
    private Context context;
    private TextView tv_title, tv_url, tv_user, tv_pw, tv_hint, tv_hint2, tv_hint3;
    private EditText et_url, et_input1, et_input2, et_input3, et_input4, et_input5;
    private Button btn_save, btn_cancel;
    private View contentView;
    private View ll_l, ll_2, ll_3, ll_4, ll_5, ll_6;


    public EditURLDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }


    private void init() {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_url_layout, null, false);
        et_url = (EditText) contentView.findViewById(R.id.et_url);
        et_input1 = (EditText) contentView.findViewById(R.id.et_input1);
        et_input2 = (EditText) contentView.findViewById(R.id.et_input2);
        et_input3 = (EditText) contentView.findViewById(R.id.et_input3);
        et_input4 = (EditText) contentView.findViewById(R.id.et_input4);
        et_input5 = (EditText) contentView.findViewById(R.id.et_input5);
        btn_save = (Button) contentView.findViewById(R.id.btn_save);
        btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
        tv_hint = (TextView) contentView.findViewById(R.id.tv_hint_et);
        tv_hint2 = (TextView) contentView.findViewById(R.id.tv_hint_et2);
        tv_hint3 = (TextView) contentView.findViewById(R.id.tv_hint_et3);
        tv_title = (TextView) contentView.findViewById(R.id.dialog_edit_title);
        ll_l = contentView.findViewById(R.id.ll_l);
        ll_2 = contentView.findViewById(R.id.ll_2);
        ll_3 = contentView.findViewById(R.id.ll_3);
        ll_4 = contentView.findViewById(R.id.ll_4);
        ll_5 = contentView.findViewById(R.id.ll_5);
        ll_6 = contentView.findViewById(R.id.ll_6);


//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void showDilaog(final OnUrlSave onUrlSave) {

        String url_api = LoginActivity.sp_ServiceUrl.query("url_api").toString();//修改默认是服务器
//        if (!"0".equals(url_api)) {
//            et_url.setText(url_api);//如果不为null那就显示保存的
//        } else {
            et_url.setText(Global.BASE_URL);//否则使用默认的网址
//        }
        et_input1.setText(Global.URL_LOGIN);
//        et_input2.setText(Global.URL_GetAreaList);
//        et_input3.setText(Global.URL_GetCardQRInfo);
//        et_input4.setText(Global.URL_GetSamplingInfo);
        et_input5.setText(Global.URL_SendResult);
        showDilaog(onUrlSave, "上传设置", "网址");
    }

    public void showDilaog(final OnOrderSave onUrlSave, String title, String hint, String hint2, String hint3) {
        this.onOrderSave = onUrlSave;
        if (isShowing()) {
            dismiss();
        }
//        ll_l.setVisibility(View.VISIBLE);
//        ll_2.setVisibility(View.VISIBLE);
//        ll_3.setVisibility(View.VISIBLE);
//        ll_4.setVisibility(View.GONE);
//        ll_5.setVisibility(View.GONE);
//        ll_6.setVisibility(View.GONE);

        tv_title.setText(title);
        tv_hint.setText(hint);
        tv_hint2.setText(hint2);
        tv_hint3.setText(hint3);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 修改，添加配置信息到本地

                LoginActivity.sp_ServiceUrl.save("url_api", et_url.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_Login", et_input1.getText().toString().trim());
//                LoginActivity.sp_ServiceUrl.save("url_GetAreaList", et_input2.getText().toString().trim());
//                LoginActivity.sp_ServiceUrl.save("url_GetQrInfo", et_input3.getText().toString().trim());
//                LoginActivity.sp_ServiceUrl.save("url_GetSamplingInfo", et_input4.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_SendResult", et_input5.getText().toString().trim());

                if (!isNull2()) {
                    if (onUrlSave != null) {
                        onUrlSave.onOrderSave(et_input1.getText().toString().trim());
                        dismiss();
                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        show();
        setContentView(contentView);
    }

    public void showDilaog(final OnOrderSave onUrlSave, String title, String hint) {
        this.onOrderSave = onUrlSave;
        if (isShowing()) {
            dismiss();
        }
        ll_l.setVisibility(View.GONE);
//        et_user.setText(Global.TESTING_UNIT_NAME);
//        et_pw.setText(Global.TESTING_UNIT_NUMBER);
        tv_title.setText(title);
        tv_hint.setText(hint);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 修改，添加配置信息到本地

                LoginActivity.sp_ServiceUrl.save("url_api", et_url.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_Login", et_input1.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_GetAreaList", et_input2.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_GetQrInfo", et_input3.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_GetSamplingInfo", et_input4.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_SendResult", et_input5.getText().toString().trim());

                if (!isNull()) {
                    if (onUrlSave != null) {
                        onUrlSave.onOrderSave(et_url.getText().toString().trim());
                        dismiss();
                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        show();
        setContentView(contentView);
    }

    public void showDilaog(final OnUrlSave onUrlSave, String title, String hint) {
        this.onUrlSave = onUrlSave;
        if (isShowing()) {
            dismiss();
        }
        ll_l.setVisibility(View.VISIBLE);
//        et_user.setText(Global.TESTING_UNIT_NAME);
//        et_pw.setText(Global.TESTING_UNIT_NUMBER);
        tv_title.setText(title);
        tv_hint.setText(hint);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 修改，添加配置信息到本地

                LoginActivity.sp_ServiceUrl.save("url_api", et_url.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_Login", et_input1.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_GetAreaList", et_input2.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_GetQrInfo", et_input3.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_GetSamplingInfo", et_input4.getText().toString().trim());
                LoginActivity.sp_ServiceUrl.save("url_SendResult", et_input5.getText().toString().trim());

                // 从本地上取出对应的值
                Object url_api = LoginActivity.sp_ServiceUrl.query("url_api");
                String s = url_api.toString();


                if (!isNull()) {
                    if (onUrlSave != null) {
                        onUrlSave.onUrlSave(et_url.getText().toString().trim(),
                                et_input1.getText().toString().trim(),
                                et_input2.getText().toString().trim()
                                , et_input3.getText().toString().trim()
                                , et_input4.getText().toString().trim()
                                , et_input5.getText().toString().trim());
                        dismiss();
                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        show();
        setContentView(contentView);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = 900;
        params.height = 600;
        getWindow().setAttributes(params);
    }

    private boolean isNull() {
        if (et_url.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "请输入", Toast.LENGTH_SHORT).show();
            return true;
        }
//        else if (et_user.getText().toString().trim().isEmpty()) {
//            Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show();
//            return true;
//        } else if (et_pw.getText().toString().trim().isEmpty()) {
//            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return false;
    }

    private boolean isNull2() {
        if (et_url.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "请输入旧密码", Toast.LENGTH_SHORT).show();
            return true;
        } else if (et_input1.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "请输入新密码", Toast.LENGTH_SHORT).show();
            return true;
        } else if (et_input2.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "请输入新密码", Toast.LENGTH_SHORT).show();
            return true;
        }
        String newpsw = et_url.getText().toString().trim();
        String psw1 = et_input1.getText().toString().trim();
        String psw2 = et_input2.getText().toString().trim();

        if (!psw1.equals(psw2)) {
            Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (psw1.equals(newpsw)) {
            Toast.makeText(context, "新密码和旧密码不能一样", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!newpsw.equals(Global.admin_psw)) {
            Toast.makeText(context, "旧密码输入错误", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    OnUrlSave onUrlSave;
    OnOrderSave onOrderSave;

    public void setOnUrlSave(OnUrlSave onUrlSave) {
        this.onUrlSave = onUrlSave;
    }

    public interface OnOrderSave {
        void onOrderSave(String msg);
    }

    public interface OnUrlSave {
        void onUrlSave(String url, String et_1, String et_2, String et_3, String et_4, String et_5);
    }

}
