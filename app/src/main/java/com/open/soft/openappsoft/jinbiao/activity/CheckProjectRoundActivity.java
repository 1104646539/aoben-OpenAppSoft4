package com.open.soft.openappsoft.jinbiao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.adapter.ResultListViewAdapter;
import com.open.soft.openappsoft.jinbiao.base.BaseActivity;
import com.open.soft.openappsoft.jinbiao.model.LineModel;

import java.util.List;

public class CheckProjectRoundActivity extends BaseActivity {

	private ListView listview = null;
	private ResultListViewAdapter adapter = null;
	private List<LineModel> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_projectround_edit);

		adapter = new ResultListViewAdapter(this, list);
		listview = (ListView) findViewById(R.id.check_project_round_listview);
		listview.setAdapter(adapter);
	}

	public void ClickBack(View v) {
		this.back();
	}

}
