package com.example.smarthome;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smarthome.Model.DataBean;
import com.example.smarthome.Model.DataCallBack;
import com.example.smarthome.Model.DataModel;
import com.example.smarthome.Model.DataRecevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private LineChartView lineChart;

    String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};//X轴的标注
    int[] score= {50,42,90,33,10,74,22,18,79,20};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    List<AxisValue> mAxisYValues = new ArrayList<>();

    PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        lineChart=findViewById(R.id.line_chart);
        pieChartView=findViewById(R.id.pie_chart);
        
//
//        getAxisXLables();//获取x轴的标注
//        getAxisPoints();//获取坐标点
//        initLineChart();//初始化

        initPicChart(30);


    }

    private void initPicChart(int num) {

        //初始化数据
        List<SliceValue> values = new ArrayList<SliceValue>();

        SliceValue sliceValue = new SliceValue((float)num, Color.parseColor("#1E90FF")).setLabel("湿度:" +num+"%");
        values.add(sliceValue);

        sliceValue = new SliceValue((float)100-num,Color.parseColor("#0000CD")).setLabel("");
        values.add(sliceValue);


        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
      //  data.setHasLabelsOnlyForSelected(true);
        //data.setHasLabelsOutside(true);
       // data.setHasCenterCircle(true);

        data.setSlicesSpacing(4);


        pieChartView.setPieChartData(data);
    }

    private void getAxisPoints() {


        for (int i = 0; i <score .length; i++) {
            mPointValues.add(new PointValue(i, score[i]).setLabel(score[i]+" °C"));
        }
    }
    private void initLineChart() {
        Line line = new Line(mPointValues);  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setStrokeWidth(1);
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setPointRadius(3);
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setColor(Color.argb(0xf0,0x43,0x6e,0xee));
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

//        Axis axisY = new Axis();  //Y轴
//        axisY.setName("");//y轴标注
//        axisY.setTextSize(10);//设置字体大小
//        data.setAxisYLeft(axisY);  //Y轴设置在左边


        Axis axisY = new Axis().setHasLines(false);
        axisY.setTextColor(Color.BLACK);
        axisY.setLineColor(Color.BLACK);
        axisY.setMaxLabelChars(6);//max label length, for example 60
        axisY.setValues(mAxisYValues);
        data.setAxisYLeft(axisY);  //Y轴设置在左边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

//        固定Y軸高度，設置x軸的移動
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.top =v.top+(v.top*2/3);
        v.bottom = v.bottom-(v.bottom*2/3);
        lineChart.setMaximumViewport(v);
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);

    }
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
        for(int i = -20; i <=100; i+= 10){
            mAxisYValues.add(new AxisValue(i).setLabel(i+" °C"));
        }

    }
}
