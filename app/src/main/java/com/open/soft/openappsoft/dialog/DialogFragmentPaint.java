package com.open.soft.openappsoft.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gsls.gt.GT;
import com.open.soft.openappsoft.R;
import com.open.soft.openappsoft.jinbiao.activity.CheckActivity;
import com.open.soft.openappsoft.jinbiao.activity.CheckPaintActivity;

import java.util.ArrayList;
import java.util.List;

@GT.Annotations.GT_AnnotationDialogFragment(R.layout.dialog_paint)
public class DialogFragmentPaint extends GT.GT_Dialog.BaseDialogFragment implements AdapterView.OnItemSelectedListener {

    @GT.Annotations.GT_View(R.id.move_chart)
    private LineChart mChart;
    @GT.Annotations.GT_View(R.id.btn_confirm)
    private Button btn_confirm;
    @GT.Annotations.GT_View(R.id.btn_cancel)
    private Button btn_cancel;

    private List<List<Entry>> lists = new ArrayList<>();
    // 绘图控件
    private int[] ints = {};


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        build(this, view);
//        setFullScreen();
        setClickExternalNoHideDialog();
        Bundle arguments = getArguments();
        if(arguments != null){
            ints = arguments.getIntArray("ints");
        }

        mChart = (LineChart) view.findViewById(R.id.move_chart);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);


        InitChart();

        /*Timber.i("scanstart:"+scanstart);
        Timber.i("scanend:"+scanend);
        Timber.i("len1:"+len1);
        Timber.i("buffer1:"+buffer1);*/
//        ClickDraw1(view);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i = 0; i < 1; i++) {
            lists.add(new ArrayList<>());
        }
        initData(600);


    }


    private void InitChart() {


        mChart.getDescription().setEnabled(false);// no description text

        mChart.setTouchEnabled(true);// enable touch gestures

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(6000);
        leftAxis.setAxisMinimum(0);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(10);
        // if disabled, scaling can be done on X0- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.getAxisRight().setEnabled(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMaximum(6000);
        rightAxis.setAxisMinimum(0);
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        rightAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(10);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);


    }

    public static DialogFragmentPaint newInstance(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }

        DialogFragmentPaint fragment = new DialogFragmentPaint();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CheckPaintActivity.REQUEST_CODE && resultCode == -1) {
            ints = data.getIntArrayExtra("ints");
            String str = "";
            for (int i = 0; i < ints.length; i++) {
                str += ints[i] + "";
            }

        }else if(requestCode == CheckActivity.REQUEST_CODE && resultCode == -1){
            ints = data.getIntArrayExtra("ints");
            String str = "";
            for (int i = 0; i < ints.length; i++) {
                str += ints[i] + "";
            }
        }
    }


    private void initData(int max) {
        //循环添加设置x轴和y轴的点
        GT.Thread.runJava(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {

                    LineData lineData = new LineData();// //线的总管理

                    for (int a = 0; a < lists.size(); a++) {
//                        lists.get(a).add(new Entry(i, new Random().nextInt(1600) + 800 + a));
                        lists.get(a).add(new Entry(i + 300, ints[i]));
                        LineDataSet one = new LineDataSet(lists.get(a), "曲线信息");//将数据赋值到你的线条上
                        one.setColor(Color.parseColor("#67BCFF"));//设置线的颜色
                        one.setDrawCircles(false);//设置是否绘制点，默认是true
                        one.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置Mode为直线，这也是默认的Mode
                        lineData.addDataSet(one);
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChart.setData(lineData);//把线条设置给你的lineChart上
                            mChart.notifyDataSetChanged();
                            mChart.invalidate();
                        }
                    });


                    //去掉旧的
//                    for (int a = 0; a < lists.size(); a++) {
//                        if (lists.get(a).size() > 100) {
//                            lists.get(a).remove(0);
//                        }
//                    }

                    //延时时间
//                    GT.Thread.sleep(1);

                }
            }
        });


    }

    /*public void ClickDraw1(View v) {

        new Thread() {

            @Override
            public void run() {
                SystemClock.sleep(5000);
                int minDataLenght = (Integer.parseInt("900") - Integer.parseInt("300")) * 2 + 6; //  +5是因为dr值至少为形如0.123，所以至少要+6

                imageData = RecImageData(minDataLenght);

                handlerMess1.sendEmptyMessage(100);
            }
        }.start();

    }

    private byte[] RecImageData(int minDataLength) {
        byte[] response = new byte[4096];
//		int select = HardwareControler.select(ToolUtils.devfd, 2, 20);
//		if(select == 0){
//			return null;
//		}
        int currentDataLength = 0;
        int errorCount = 0;
        byte[] buffer = new byte[1024];
        while (errorCount < 100) {
//            int len = HardwareControler.read(Global.DEV_COM3, buffer, buffer.length);
            int len = len1;
            buffer = buffer1;
            if (len < 1) {
                errorCount++;
            } else {
                System.arraycopy(buffer, 0, response, currentDataLength, len);
                Array.setByte(buffer, 0, (byte) '\0');
            }
            currentDataLength += len;
            if (currentDataLength >= minDataLength) {
                break;
            }
            SystemClock.sleep(500);
        }
        if (errorCount >= 100) {
            return null;
        }
        int index = -1;
        for (int i = 0; i < currentDataLength; i++) {
            if (response[i] == ',') {
                index = i;
                break;
            }
        }
        if (index == -1) {   //如果不包含逗号，则说明没有dr值，数据不完整
            return null;
        }


        byte[] data = new byte[currentDataLength - index - 1];
        System.arraycopy(response, index + 1, data, 0, data.length);

        return data;
    }


    private Handler handlerMess1 = new Handler() {
        @Override
        @SuppressLint("HandlerLeak")//
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
//                    int[] ints = ToolUtils.byte2Int(imageData);
                    String ints_data = "";
                    for(int i=0;i<ints.length;i++){
                        ints_data += ints[i] + ",";
                    }
                    Timber.i("ints_data:"+ints_data);
                    int scanStart = Integer.parseInt("320");
                    InitChart();
                    try {
                        setData(ints, scanStart);

                    } catch (ClassCastException e1) {
                        e1.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private void InitChart() {


        mChart.getDescription().setEnabled(false);// no description text

        mChart.setTouchEnabled(true);// enable touch gestures

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(6000);
        leftAxis.setAxisMinimum(0);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(10);
        // if disabled, scaling can be done on X0- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.getAxisRight().setEnabled(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMaximum(6000);
        rightAxis.setAxisMinimum(0);
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        rightAxis.setDrawZeroLine(false);
        leftAxis.setLabelCount(10);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

    }


    public void setData(int[] imageData, int scanStart) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMaximum(scanStart + imageData.length);
        xAxis.setAxisMinimum(scanStart);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        // if disabled, scaling can be done on X0- and y-axis separately
        mChart.setPinchZoom(true);
        xAxis.setLabelCount(20);


        ArrayList<Entry> yVals = new ArrayList<Entry>();
        String sum = "";
        for (int a : imageData) {
            sum += a + ",";
        }
        for (int i = 0; i < imageData.length; i++) {
            yVals.add(new Entry(scanStart + i, imageData[i]));
        }

        LineDataSet set1;

        set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.5f);
        set1.setDrawCircles(false);
        set1.setLineWidth(3.0f);
        set1.setCircleRadius(40f);
        set1.setCircleColor(Color.WHITE);
//			set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.RED);
        set1.setDrawFilled(false);
//			set1.setFillColor(Color.WHITE);
//			set1.setFillAlpha(100);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTypeface(Typeface.DEFAULT);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        // set data
        mChart.setData(data);

        mChart.getLegend().setEnabled(false);

//			mChart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        mChart.invalidate();
    }*/

}
