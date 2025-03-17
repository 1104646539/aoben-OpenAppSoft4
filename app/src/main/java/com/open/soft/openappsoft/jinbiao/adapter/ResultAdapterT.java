package com.open.soft.openappsoft.jinbiao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.model.ResultModel;
import com.open.soft.openappsoft.jinbiao.util.ToolUtils;

import java.util.List;

public class ResultAdapterT extends BaseAdapter {

	private Context context = null;
	private List<ResultModel> list = null;

	public ResultAdapterT(Context context, List<ResultModel> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}
	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		ViewHolder holder = null;

		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = View.inflate(context, R.layout.result_listview_item, null);
			holder.number = (TextView) arg1.findViewById(R.id.result_id);
			holder.checkedOrg = (TextView)arg1.findViewById(R.id.tv_checked_org);
			holder.company_name = (TextView) arg1.findViewById(R.id.result_company);
			holder.persion = (TextView) arg1.findViewById(R.id.result_people);
			holder.shiji = (TextView) arg1.findViewById(R.id.result_shi_company);
			holder.projectname = (TextView) arg1.findViewById(R.id.result_list_item_project);
			holder.samplename = (TextView) arg1.findViewById(R.id.result_list_item_sample);
			holder.typename=(TextView)arg1.findViewById(R.id.result_list_item_type);
			holder.xian = (TextView) arg1.findViewById(R.id.result_xian);
			holder.lin = (TextView) arg1.findViewById(R.id.result_lin_value);
			holder.value = (TextView) arg1.findViewById(R.id.result_check_value);
			holder.style_long = (TextView) arg1.findViewById(R.id.result_hp);
			holder.result = (TextView) arg1.findViewById(R.id.result_result);
			holder.time = (TextView) arg1.findViewById(R.id.result_time);
			holder.result_lin = (LinearLayout) arg1.findViewById(R.id.result_lin);
			arg1.setTag(holder);
		}

		holder = (ViewHolder) arg1.getTag();
		ResultModel model = list.get(arg0);
		holder.number.setText(model.id+"");
		holder.checkedOrg.setText(model.sample_unit);
		holder.company_name.setText(model.company_name);
		holder.persion.setText(model.persion);
		holder.shiji.setText(model.shiji);
		holder.projectname.setText(model.project_name);
		if(model.concentrateUnit != null && !"null".equals(model.concentrateUnit)){
			holder.xian.setText(model.xian + model.concentrateUnit);
		}else{
			holder.xian.setText(model.xian);
		}
		holder.lin.setText(model.lin);
		holder.value.setText(model.check_value);
		holder.style_long.setText(model.style_long);
		holder.result.setText(model.check_result);
		holder.samplename.setText(model.sample_name);
		holder.typename.setText(model.sample_type);
		String time = null;
		time= ToolUtils.dateToString(ToolUtils.longToDate(model.time, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
		holder.time.setText(time);
		if (clickTemp == arg0) {
			holder.result_lin.setBackgroundColor(context.getResources().getColor(R.color.selected));
		} else {
			holder.result_lin.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
		return arg1;
	}

	static class ViewHolder {
		TextView checkedOrg;
		TextView number;
		TextView company_name;
		TextView persion;
		TextView shiji;
		TextView projectname;
		TextView xian;
		TextView samplename;
		TextView typename;
		TextView lin;
		TextView value;
		TextView style_long;
		TextView result;
		LinearLayout result_lin;
		TextView time;
	}

}
