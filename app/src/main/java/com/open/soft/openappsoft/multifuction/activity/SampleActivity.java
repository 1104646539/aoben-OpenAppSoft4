package com.open.soft.openappsoft.multifuction.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.multifuction.adapter.SampleAdapter;
import com.open.soft.openappsoft.multifuction.adapter.UserInputAdapter;
import com.open.soft.openappsoft.multifuction.dialog.OperateBaseDataDialog;
import com.open.soft.openappsoft.multifuction.model.SampleName;
import com.open.soft.openappsoft.multifuction.util.APPUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SampleActivity extends UserInputDataActivity<SampleName> {
	
	private static String url = null;

	protected void onCreate(Bundle savedInstanceState) {
		bean = new SampleName();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getCustomTitle() {
		return "样品名称编辑";
	}

	@Override
	protected UserInputAdapter<SampleName> getAdapter() {
		return new SampleAdapter(list, this);
	}

	@Override
	protected OperateBaseDataDialog<SampleName> getDialog(SampleName sample) {
		View view = View.inflate(this, R.layout.dialog_sample_edit, null);
		final Button btnSave = (Button) view.findViewById(R.id.btn_OK);
		final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final EditText etSampleName = (EditText) view.findViewById(R.id.et_sampleName);
		return new OperateBaseDataDialog<SampleName>(this, view, sample) {
			@Override
			protected void initCustomView(SampleName sample) {
				if (sample != null) {
					etSampleName.setText(sample.sampleName);
					etSampleName.setSelection(etSampleName.getText().toString().length());
				}
			}

			@Override
			protected SampleName getData(SampleName sample) {
				if (sample == null) {
					sample = new SampleName();
				}
				if (TextUtils.isEmpty(etSampleName.getText().toString())) {
					APPUtils.showToast(act, "请输入样品名称");
					return null;
				}
				sample.sampleName = etSampleName.getText().toString().trim();
				return sample;
			}

			@Override
			protected Button getSaveButton() {
				return btnSave;
			}

			@Override
			protected Button getCancelButton() {
				return btnCancel;
			}
		};
	}

	/**
	 * 根据输入流返回一个字符串
	 * @param is
	 * @return
	 * @throws 
	 */
	private static String getStringFromInputStream(InputStream is) throws Exception{

		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buff=new byte[1024];
		int len=-1;
		while((len=is.read(buff))!=-1){
			baos.write(buff, 0, len);
		}
		is.close();
		String html=baos.toString();
		baos.close();


		return html;
	}
}
