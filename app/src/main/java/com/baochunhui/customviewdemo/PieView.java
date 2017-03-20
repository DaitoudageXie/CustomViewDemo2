package com.baochunhui.customviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Eric on 2017/3/17.
 */

public class PieView extends View{

    private int[] mColors={0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    //
    private float mStartAngle=0f;
    //数据
    private ArrayList<PieData> mData;
    //
    private Paint mPaint=new Paint();
    //
    private int  mHeight , mWidth;//View的高宽

    public PieView(Context context, int[] mColors) {
        super(context);

    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        mWidth=w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null==mData){
            return;
        }
        float currentStartAngle=mStartAngle;
        canvas.translate(mWidth/2,mHeight/2);
        float r=(float)(Math.min(mHeight,mWidth)/2*0.8);
        RectF rect=new RectF(-r,-r,r,r);
        //
        for (int i=0;i<mData.size();i++){
            PieData pie=mData.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rect,currentStartAngle,pie.getAngle(),true,mPaint);
            currentStartAngle=currentStartAngle+pie.getAngle();
        }
    }

    /**
     * 设置开始角度
     * @param angle
     */
    public void setStartAngle(float angle){
        mStartAngle=angle;
    }

    /**
     * 初始化数据
     */
    public void initData(ArrayList<PieData> mData){
        if (mData==null||mData.size()==0){
            return;
        }
        float sumValue=0;
        for (int i=0;i<mData.size();i++){
            PieData pie=mData.get(i);
            sumValue+=pie.getValue();
            //设置颜色
            int j=i% mColors.length;
            pie.setColor(mColors[j]);
        }
        //设置角度
        float sumAngle=0;
        for (int i=0;i<mData.size();i++){
             PieData pie=mData.get(i);

            float percentage=pie.getValue()/sumValue;
            float angle=percentage*360;
            pie.setPercentage(percentage);
            pie.setAngle(angle);
            //
            sumAngle+=angle;
            Log.i("angle", "" + pie.getAngle());

        }

    }

    public void setData(ArrayList<PieData> mData){
        this.mData=mData;
        initData(mData);
        //重新绘制
        invalidate();
    }

}
