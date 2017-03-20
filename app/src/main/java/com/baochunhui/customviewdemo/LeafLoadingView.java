package com.baochunhui.customviewdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Random;

/**
 * Created by Eric on 2017/3/20.
 */

public class LeafLoadingView extends View {
    private static final String TAG = "LeafLoadingView";
    // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    //总进度
    private static final int TOTAL_PROGRESS=100;
    //叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME=3000;
    //叶子转动一个周期的时间
    private static final long LEAF_ROTATE_TIME=2000;

    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;


    // 用于控制绘制的进度条距离左／上／下的距离
    private static final int LEFT_MARGIN=9;

    //用于控制绘制的进度条距离右的距离
    private static final int RIGHT_MARGIN=25;

    private int mLeftMargin,mRightMargin;

    private Resources mResourses;//资源

    private long mLeafFloatTime=LEAF_FLOAT_TIME;//
    private long mLeafRotateTime=LEAF_ROTATE_TIME;
    //树叶图片
    private Bitmap mLeafBitmap;
    //树叶的宽高
    private int mLeafWidth,mLeafHight;
    //框
    private Bitmap mOuterBitmap;
    //框的宽高
    private int mOuterWidth,mOuterHeight;
    //画笔
    private Paint mBitmapPaint,mOrangePaint,mWhitePaint;
    //
    private int mTotalHeight,mTotalWidth;
    //progress的宽度
    private int mProgressWidth;
    //进度条的位置
    private int mCurrentProgressPosition;
    //半径
    private int mArcRadius ,mArcRightLocation;

    //整个框的
    private Rect mOuterSrcRect,mOuterdestRect;
    //里面内容的
    private RectF mWhiteRectF,mOrangeRectF,mArcRectF;
    //当前进度
    private int mProgress;
    //
    List<Leaf> mLeafInfos;
    // 中等振幅大小
    private long mMiddleAmplitude=MIDDLE_AMPLITUDE;
    //振幅差
    private int mAmplitudeDisparity=AMPLITUDE_DISPARITY;


    public LeafLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //
        mResourses=getResources();
        mLeftMargin=UiUtils.dipToPix(context,LEFT_MARGIN);
        mRightMargin=UiUtils.dipToPix(context,RIGHT_MARGIN);
        //
        mLeafFloatTime=LEAF_FLOAT_TIME;
        mLeafRotateTime=LEAF_ROTATE_TIME;
        //
        initBitmap();

        initPaint();

    }

    private void initPaint() {
        mBitmapPaint=new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        //
        mWhitePaint=new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(WHITE_COLOR);
        //
        mOrangePaint=new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);
    }

    /**
     * 初始化Bitmap
     */

    private void initBitmap() {
        //树叶
          mLeafBitmap=((BitmapDrawable) mResourses.getDrawable(R.mipmap.leaf)).getBitmap();
          mLeafWidth=mLeafBitmap.getWidth();
          mLeafHight=mLeafBitmap.getHeight();
        //框
         mOuterBitmap=((BitmapDrawable)mResourses.getDrawable(R.mipmap.leaf_kuang)).getBitmap();
         mOuterWidth=mOuterBitmap.getWidth();
         mOuterHeight=mOuterBitmap.getHeight();
    }
    //


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight=h;
        mTotalWidth=w;
        //
        mProgressWidth=mTotalWidth-mLeftMargin-mRightMargin;
        //
        mArcRadius=(mTotalHeight-mLeftMargin*2)/2;
        //框的src
        mOuterSrcRect=new Rect(0,0,mOuterWidth,mOuterHeight);
        //框的dest
        mOuterdestRect=new Rect(0,0,mTotalWidth,mTotalHeight);
        //
        mWhiteRectF=new RectF(mLeftMargin+mCurrentProgressPosition,mLeftMargin,
                mTotalWidth-mRightMargin,mTotalHeight-mLeftMargin);
        //
        mOrangeRectF=new RectF(mLeftMargin+mArcRadius,mLeftMargin,
                mCurrentProgressPosition,mTotalHeight-mLeftMargin);
        //
        mArcRectF=new RectF(mLeftMargin,mLeftMargin,
                mLeftMargin+mArcRadius*2,mTotalHeight-mLeftMargin);
        //
        mArcRightLocation=mLeftMargin+mArcRadius;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        drawProgressAndLeafs(canvas);




    }

    private void drawProgressAndLeafs(Canvas canvas) {
        if (mProgress>=TOTAL_PROGRESS){
            mProgress=0;
        }
        //进度条的位置
        mCurrentProgressPosition=mProgressWidth*mProgress/TOTAL_PROGRESS;
        //
        if(mCurrentProgressPosition<mArcRadius){
            Log.i(TAG, "mProgress = " + mProgress + "---mCurrentProgressPosition = "
                    + mCurrentProgressPosition
                    + "--mArcProgressWidth" + mArcRadius);
            //绘制白色arc
            canvas.drawArc(mArcRectF,90,180,false,mWhitePaint);
            //绘制白色矩形
            mWhiteRectF.left=mArcRightLocation;
            canvas.drawRect(mWhiteRectF,mWhitePaint);
            //绘制叶子
            drawLeafs(canvas);
            //



        }

    }

    /**
     * 绘制叶子
     * @param canvas
     */

    private void drawLeafs(Canvas canvas) {
           mLeafRotateTime=mLeafFloatTime<=0? LEAF_ROTATE_TIME:mLeafFloatTime;
        //
        long currentTime=System.currentTimeMillis();
        for(int i=0;i<mLeafInfos.size();i++){

            Leaf leaf=mLeafInfos.get(i);
            if (leaf.startTime<=currentTime&&leaf.startTime!=0){
                //绘制叶子-根据当前的时间绘制出叶子的x,y
                getLeafLocation(leaf,currentTime);

            }



        }



    }

    private void getLeafLocation(Leaf leaf, long currentTime) {

        long intervalTime=currentTime-leaf.startTime;
        if (intervalTime<0){
            return;
        }else if (intervalTime>mLeafFloatTime){
            leaf.startTime=System.currentTimeMillis()+new Random().nextInt((int)mLeafFloatTime);

        }
        //
        float fraction=intervalTime/mLeafFloatTime;
        leaf.x=(int)(mProgressWidth-fraction*mProgressWidth);
        leaf.y=getLocationY(leaf);

    }
    //
    private int getLocationY(Leaf leaf){
         //y=A(wx+Q)+h
         float w=(float) ((float) 2*Math.PI/mProgressWidth);
        float a=mMiddleAmplitude;
        switch (leaf.type){
            case Little:
                a=mMiddleAmplitude-mAmplitudeDisparity;
                break;
            case Middle:
                a=mMiddleAmplitude;
                break;
            case Big:
                a=mMiddleAmplitude+mAmplitudeDisparity;
                break;
            default:
                break;
        }
        return (int)(a*Math.sin(leaf.x*w))+mArcRadius*2/3;
    }
    //
    private class Leaf{
        //在绘制部分的位置
        float x,y;
        //控制叶子飘动的幅度
        StartType type;
        //旋转角度
        int rotateAngle;
        //顺时针0/逆时针1
        int rotateDirection;
        //开始时间ms
        long startTime;

    }

    private enum StartType{
        Little,Middle,Big
    }


}
