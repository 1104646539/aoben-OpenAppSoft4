package com.open.soft.openappsoft.atp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.util.Global;

import java.io.UnsupportedEncodingException;

import timber.log.Timber;

public class SystemSetting extends Activity implements OnClickListener {


	/**
	 * 调零按钮
	 */
	private Button btn_Zero;
	/**
	 * 检测按钮
	 */
	private Button btn_Test;
	private TextView tv_RecData;

	private Button btn_ClearRecData;
	private TextView tv_SendData;
	private boolean receDataFlag = true;

	byte[] buf = new byte[512];

	/**
	 * 选中的设备的ID
	 */
	private int fd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_systemsetting);
		init();
	}

	private void init(){

		tv_RecData = (TextView) findViewById(R.id.activity_systemsetting_tv_RecData);
		tv_SendData = (TextView) findViewById(R.id.activity_systemsetting_tv_SendData);
		tv_RecData.setMovementMethod(new ScrollingMovementMethod());
		tv_SendData.setMovementMethod(new ScrollingMovementMethod());
		btn_Zero = (Button) findViewById(R.id.activity_systemsetting_btn_Zero);
		btn_Test = (Button) findViewById(R.id.activity_systemsetting_btn_Test);
		btn_ClearRecData = (Button) findViewById(R.id.activity_systemsetting_btn_ClearRecData);
		btn_ClearSend = (Button) findViewById(R.id.activity_systemsetting_btn_ClearSend);
		btn_Zero.setOnClickListener(this);
		btn_Test.setOnClickListener(this);
		btn_ClearRecData.setOnClickListener(this);
		btn_ClearSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_systemsetting_btn_Zero:
			SendData("LightSameATP");
			break;
		case R.id.activity_systemsetting_btn_Test:
			SendData(new String(Global.getValueInstruction));
			break;
		case R.id.activity_systemsetting_btn_ClearRecData:
			tv_RecData.setText("");
			break;
		case R.id.activity_systemsetting_btn_ClearSend:
			tv_SendData.setText("");
		default:
			break;
		}

	}

	private void SendData(String s) {

		int length = 0;
		try {
			length = HardwareControler.write(fd, s.getBytes("gbk"));
			if(length != s.getBytes().length){
				Toast.makeText(SystemSetting.this, "数据发送失败", Toast.LENGTH_SHORT).show();
			}else{
				tv_SendData.append(s + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void readData(){

			new Thread(new Runnable() {

				@Override
				public void run() {
					try{
					fd = HardwareControler.openSerialPort(Global.COM3_T2, 115200, 8, 1);
					while(receDataFlag){
						int m = HardwareControler.select(fd, 2, 20);
						int n;
						String text = "";
						if (m == 1)
						{
							while ((n = HardwareControler.read(fd, buf,	buf.length)) > 0)
							{
								try
								{
									Thread.sleep(90);
								} catch (InterruptedException e)
								{
									e.printStackTrace();
								}
								for (int i = 0; i < n; i++)
								{
									text += (char) buf[i];
								}
							};
							//Log.d("MC", "n:" + n);
							Message message = Message.obtain();
							message.obj = text;
							revHandler.sendMessage(message);
							//System.out.println(Arrays.toString(buf));
						}
						if(!receDataFlag){
							break;
						}
						try
						{
							Thread.sleep(100);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}

				}catch (UnsatisfiedLinkError e){
						Timber.i("打开串口失败atp调零界面");
					}}
			}).start();
		}
	@SuppressLint("HandlerLeak")
	private Handler revHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			tv_RecData.append((String) msg.obj);

			super.handleMessage(msg);
		}
	};
	private Button btn_ClearSend;


	@Override
	protected void onResume() {
		super.onResume();
		receDataFlag = true;
		readData();
	}

	@Override
	protected void onPause() {
		receDataFlag = false;
		super.onPause();
	}
}
