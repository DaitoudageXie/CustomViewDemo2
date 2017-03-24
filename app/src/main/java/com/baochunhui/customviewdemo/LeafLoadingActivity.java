package com.baochunhui.customviewdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class LeafLoadingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case REFRESH_PROGRESS:
                    if (mProgress<40){
                        mProgress+=1;
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,new Random().nextInt(800));
                        mLeafLoadingView.setProgress(mProgress);
                    }else{
                        mProgress+=1;
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,new Random().nextInt(1200));
                        mLeafLoadingView.setProgress(mProgress);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private static final int REFRESH_PROGRESS = 0x10;
    private View mFanView;
    private Button mClearButton;
    private LeafLoadingView mLeafLoadingView;
    private SeekBar mAmpireSeekBar;
    private SeekBar mDistanceSeekBar;
    private TextView mMplitudeText;
    private TextView mDisparityText;
    //
    private int mProgress = 0;

    private TextView mProgressText;
    private View mAddProgress;
    private SeekBar mFloatTimeSeekBar;

    private SeekBar mRotateTimeSeekBar;
    private TextView mFloatTimeText;
    private TextView mRotateTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_loading);
        initViews();

        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);

    }

    private void initViews() {
        mFanView=findViewById(R.id.fan_pic);
        RotateAnimation rotateAnimation=AnimationUtils.initRotateAnimation(false,1500,true, Animation.INFINITE);
        mFanView.startAnimation(rotateAnimation);
        mClearButton= (Button) findViewById(R.id.clear_progress);
        mClearButton.setOnClickListener(this);
        //
        mLeafLoadingView= (LeafLoadingView) findViewById(R.id.leaf_loading);
        mMplitudeText = (TextView) findViewById(R.id.text_ampair);
        mMplitudeText.setText("当前振幅"+
                mLeafLoadingView.getMiddleAmplitude());


        mDisparityText = (TextView) findViewById(R.id.text_disparity);
        mDisparityText.setText("修改XX,当前"+
                String.valueOf(mLeafLoadingView.getMplitudeDisparity()));

        mAmpireSeekBar = (SeekBar) findViewById(R.id.seekBar_ampair);
        mAmpireSeekBar.setOnSeekBarChangeListener(this);
        mAmpireSeekBar.setProgress(mLeafLoadingView.getMiddleAmplitude());
        mAmpireSeekBar.setMax(50);

        mDistanceSeekBar = (SeekBar) findViewById(R.id.seekBar_distance);
        mDistanceSeekBar.setOnSeekBarChangeListener(this);
        mDistanceSeekBar.setProgress(mLeafLoadingView.getMplitudeDisparity());
        mDistanceSeekBar.setMax(20);

        mAddProgress = findViewById(R.id.add_progress);
        mAddProgress.setOnClickListener(this);
        mProgressText = (TextView) findViewById(R.id.text_progress);

        mFloatTimeText = (TextView) findViewById(R.id.text_float_time);
        mFloatTimeSeekBar = (SeekBar) findViewById(R.id.seekBar_float_time);
        mFloatTimeSeekBar.setOnSeekBarChangeListener(this);
        mFloatTimeSeekBar.setMax(5000);
        mFloatTimeSeekBar.setProgress((int) mLeafLoadingView.getLeafFloatTime());
        mFloatTimeText.setText("修改当前漂浮时间"+
                String.valueOf(mLeafLoadingView.getLeafFloatTime()));

        mRotateTimeText = (TextView) findViewById(R.id.text_rotate_time);
        mRotateTimeSeekBar = (SeekBar) findViewById(R.id.seekBar_rotate_time);
        mRotateTimeSeekBar.setOnSeekBarChangeListener(this);
        mRotateTimeSeekBar.setMax(5000);
        mRotateTimeSeekBar.setProgress((int) mLeafLoadingView.getLeafRotateTime());
        mRotateTimeText.setText("当前旋转时间"+
                String.valueOf(mLeafLoadingView.getLeafRotateTime()));

    }

    @Override
    public void onClick(View view) {
        if (view == mClearButton) {
            mLeafLoadingView.setProgress(0);
            mHandler.removeCallbacksAndMessages(null);
            mProgress = 0;
        } else if (view == mAddProgress) {
            mProgress++;
            mLeafLoadingView.setProgress(mProgress);
            mProgressText.setText(String.valueOf(mProgress));
        }





    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar == mAmpireSeekBar) {
            mLeafLoadingView.setMiddleAmplitude(progress);
            mMplitudeText.setText(getString(R.string.current_mplitude,
                    String.valueOf(progress)));
        } else if (seekBar == mDistanceSeekBar) {
            mLeafLoadingView.setMplitudeDisparity(progress);
            mDisparityText.setText(getString(R.string.current_Disparity,
                    String.valueOf(progress)));
        } else if (seekBar == mFloatTimeSeekBar) {
            mLeafLoadingView.setLeafFloatTime(progress);
            mFloatTimeText.setText(getResources().getString(R.string.current_float_time,
                    String.valueOf(progress)));
        }
        else if (seekBar == mRotateTimeSeekBar) {
            mLeafLoadingView.setLeafRotateTime(progress);
            mRotateTimeText.setText(getResources().getString(R.string.current_rotate_time,
                    String.valueOf(progress)));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}