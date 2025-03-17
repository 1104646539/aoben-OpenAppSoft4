package com.open.soft.openappsoft.jinbiao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;

public class ProjectManagerActivity extends BaseActivity implements OnClickListener {

	
	private Button btn_CheckUnit;
	private Button btn_Checker;
	private Button btn_Project;
	private Button btn_ReagentCompany;
	private Button btn_CheckPicture;
	private Button btn_CardCompany;
	private Button btn_SampleType;
	private Button btn_SampleName;
	private Button btn_SampleUnit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_manager_main);
		
		btn_CheckUnit = (Button) findViewById(R.id.activity_project_manager_btn_checkunit);
		btn_Checker = (Button) findViewById(R.id.activity_project_manager_btn_checker);
		btn_Project = (Button) findViewById(R.id.activity_project_manager_btn_project);
		btn_ReagentCompany = (Button) findViewById(R.id.activity_project_manager_btn_reagentcompany);
		btn_CheckPicture = (Button) findViewById(R.id.activity_project_manager_btn_checkpicture);
		btn_CardCompany = (Button) findViewById(R.id.activity_project_manager_btn_cardcompany);
		btn_SampleType = (Button) findViewById(R.id.activity_project_manager_btn_sampletype);
		btn_SampleName = (Button) findViewById(R.id.activity_project_manager_btn_samplename);
		btn_SampleUnit = (Button) findViewById(R.id.activity_project_manager_btn_sampleunit);
		
		btn_CheckUnit.setOnClickListener(this);
		btn_Checker.setOnClickListener(this);
		btn_Project.setOnClickListener(this);
		btn_ReagentCompany.setOnClickListener(this);
		btn_CheckPicture.setOnClickListener(this);
		btn_CardCompany.setOnClickListener(this);
		btn_SampleType.setOnClickListener(this);
		btn_SampleName.setOnClickListener(this);
		btn_SampleUnit.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		int i = v.getId();//检测单位
//检验员
//检测项目
//试剂厂商
//检测图片
//卡厂商
//样品类型
//样品名称
//商品来源
		if (i == R.id.activity_project_manager_btn_checkunit) {
			intent = new Intent(this, CheckProjectCompanyActivity.class);
			intent.putExtra("source", "1");
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_checker) {
			intent = new Intent(this, CheckProjectCompanyActivity.class);
			intent.putExtra("source", "2");
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_project) {
			intent = new Intent(this, CheckProjectActivity.class);
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_reagentcompany) {
			intent = new Intent(this, CheckShiJiActivity.class);
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_checkpicture) {
			intent = new Intent(this, CheckResultPhotoActivity.class);
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_cardcompany) {
			intent = new Intent(this, ProductActivity.class);
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_sampletype) {
			intent = new Intent(this, TypeActivity.class);
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_samplename) {
			intent = new Intent(this, SampleActivity.class);
			startActivity(intent);
		} else if (i == R.id.activity_project_manager_btn_sampleunit) {
			intent = new Intent(this, CheckProjectCompanyActivity.class);
			intent.putExtra("source", "3");
			startActivity(intent);
		}
	}
}
