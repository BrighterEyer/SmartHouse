package com.example.smarthome.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;


import com.example.smarthome.Persent.MainPresenter;
import com.example.smarthome.R;

import com.example.smarthome.View.BaseActivity;
import com.example.smarthome.View.UiInterface;
import com.hanks.htextview.evaporate.EvaporateTextView;

/**
 * Created by joel.
 * Date: 2019/6/2
 * Time: 14:59
 * Description:
 */
public class Aty_Mian  extends BaseActivity<UiInterface, MainPresenter<UiInterface>>implements UiInterface {
    EvaporateTextView etv;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
//        getPresenter().ReadCo2("hzy");
//        getPresenter().ReadLedState("hzy");
//        getPresenter().ReadSmog("hzy");
//        getPresenter().ReadTemperature("hzy");
        etv=findViewById(R.id.etv_tem);


    }

    @Override
    public MainPresenter choicePresent() {
        return new MainPresenter();
    }

    @Override
    public void ReadCo2(int[] values, String[] times) {
        Log.i("MainAty", "ReadCo2: "+values.length+" "+times.length);

    }

    @Override
    public void ReadLedState(int i, String times) {
        Log.i("MainAty", "ReadLedState: "+i+" "+times);
    }

    @Override
    public void ReadSmog(int[] values, String[] times) {
        Log.i("MainAty", "ReadSmog: "+values.length+" "+times.length);
    }

    @Override
    public void ReadTemperature(int[] values, String[] times) {
        Log.i("MainAty", "ReadTemperature: "+values.length+" "+times.length);
    }
}
