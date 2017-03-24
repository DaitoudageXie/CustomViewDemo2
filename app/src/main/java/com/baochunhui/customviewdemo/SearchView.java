package com.baochunhui.customviewdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Eric on 2017/3/24.
 */

public class SearchView extends View {

   //画笔
    private Paint mPaint;
    //
    private int mWidth;
    private int mHeight;

    //
    private State mCurrentState=State.NONE;
    //放大镜和外部圆环
    private Path pathSearch;
    private Path pathCircle;
    //
    private PathMeasure mMeasure;
    //默认动画时间
    private int defaultDuration=2000;
    // 控制各个过程的动画
    private ValueAnimator mStaringAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue=0;

    //
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    //
    private Handler mAnimationHandler;

    //
    private boolean isOver=false;
    private int count=0;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAll();
    }

    private void initAll() {
        initPaint();
        initPath();
        initHandler();
        initListener();
        initAnimator();
        //
        // 进入开始动画
        mCurrentState = State.STARTING;
        mStaringAnimator.start();


    }

    private void initAnimator() {
        mStaringAnimator=new ValueAnimator().ofFloat(0,1).setDuration(defaultDuration);
        mSearchingAnimator=new ValueAnimator().ofFloat(0,1).setDuration(defaultDuration);
        mEndingAnimator=new ValueAnimator().ofFloat(0,1).setDuration(defaultDuration);
        //
        mStaringAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);
        //
        mSearchingAnimator.addListener(mAnimatorListener);
        mStaringAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);

    }

    private void initListener() {

        mUpdateListener=new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue=(float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        };

        mAnimatorListener=new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mAnimationHandler.sendEmptyMessage(0);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }

    private void initHandler() {

        mAnimationHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurrentState){
                    case STARTING:
                        //从开始动画转搜索动画
                        isOver=false;
                        mCurrentState=State.SEARCHING;
                        mStaringAnimator.removeAllListeners();
                        mSearchingAnimator.start();
                        break;
                    case SEARCHING:
                        if (!isOver){ // 如果搜索未结束 则继续执行搜索动画
                            mSearchingAnimator.start();
                            Log.e("Update", "RESTART");
                            count++;
                            if (count>2){       // count大于2则进入结束状态
                                isOver = true;
                            }
                        }else{
                            mCurrentState=State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;
                    case ENDING:
                        mCurrentState=State.NONE;
                        break;
                }
            }
        };
    }
    private void initPath() {
        pathCircle=new Path();
        pathSearch=new Path();
        mMeasure=new PathMeasure();
        //
        RectF oval1=new RectF(-50,-50,50,50);//放大镜圆环
        pathSearch.addArc(oval1,45,359.5f);
        //
        RectF oval2=new RectF(-100,-100,100,100);//外环
        pathCircle.addArc(oval2,45,359.9f);
        //
        float[] pos=new float[2];
        mMeasure.setPath(pathCircle,false);
        mMeasure.getPosTan(0,pos,null);
        pathSearch.lineTo(pos[0],pos[1]);
        //
        Log.i("TAG", "pos=" + pos[0] + ":" + pos[1]);
    }

    private void initPaint() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public static enum State{
        NONE,
        STARTING,
        SEARCHING,
        ENDING
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

        drawSearch(canvas);



    }

    private void drawSearch(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        //
        canvas.translate(mWidth/2,mHeight/2);
        //
        canvas.drawColor(Color.parseColor("#0082D7"));
        //
        switch (mCurrentState){
            case NONE:
                canvas.drawPath(pathSearch,mPaint);
                break;
            case STARTING:
                 mMeasure.setPath(pathSearch,false);
                Path dst=new Path();
                mMeasure.getSegment(mMeasure.getLength()*mAnimatorValue,mMeasure.getLength(),dst,true);
                canvas.drawPath(dst,mPaint);
                break;
            case SEARCHING:
                mMeasure.setPath(pathCircle,false);
                Path dst2=new Path();
                float stop=mMeasure.getLength()*mAnimatorValue;
                float start=(float) (stop-(0.5-Math.abs(0.5-mAnimatorValue))*200f);
                //
                mMeasure.getSegment(start,stop,dst2,true);
                canvas.drawPath(dst2,mPaint);
                break;
            case ENDING:
                mMeasure.setPath(pathSearch,false);
                Path dst3=new Path();
                mMeasure.getSegment(mMeasure.getLength()*mAnimatorValue,mMeasure.getLength(),dst3,true);
                canvas.drawPath(dst3,mPaint);
                break;
            default:
                break;

        }

    }
}
