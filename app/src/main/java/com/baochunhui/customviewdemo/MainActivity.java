package com.baochunhui.customviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    PieView mPieView;
    Button btnLeafLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPieView= (PieView) findViewById(R.id.myPieView);
        btnLeafLoading= (Button) findViewById(R.id.btn_toLeafLoading);
        btnLeafLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LeafLoadingActivity.class);
                startActivity(intent);
            }
        });
        initPieView();
    }

    private void initPieView() {
        ArrayList<PieData> mData=new ArrayList<>();
        PieData pie1=new PieData(1000,"one");
        PieData pie2=new PieData(2000,"two");
        PieData pie3=new PieData(1254,"two");
        PieData pie4=new PieData(3521,"two");
        PieData pie5=new PieData(2541,"two");
        PieData pie6=new PieData(1254,"two");
        mData.add(pie1);
        mData.add(pie2);
        mData.add(pie3);
        mData.add(pie4);
        mData.add(pie5);
        mData.add(pie6);
        mPieView.setStartAngle(0);
        mPieView.setData(mData);
    }
}
