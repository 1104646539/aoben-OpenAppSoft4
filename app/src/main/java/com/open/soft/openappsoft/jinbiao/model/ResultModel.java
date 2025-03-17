package com.open.soft.openappsoft.jinbiao.model;

public class ResultModel {

	public int id;//检测流水号
	public String number;
	public String company_name;//检测单位
	public String persion;//检验员
	public String shiji;//
	public String project_name;//检测项目
	public String lin;//临界值
	public String check_value;//检测值
	public String style_long;//样品浓度
	public String check_result;//检测结果
	public long time;//检测时间
	public String sample_name;//样品名称
	public String sample_type;//样品类型
	//限量标准
	public String xian;
	public String concentrateUnit;
	public String sample_unit;//商品来源
	public String sample_number;//数据库版本为2
	public String companyCode;//组织机构
//	public String productId;//样品id

	// 新增
//	public String sample_id; // 样品编号
//	public int upload_status = 0; // 上传状态，默认未上传
}
