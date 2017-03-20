package com.baochunhui.customviewdemo;

/**
 * Created by Eric on 2017/3/17.
 */

public class PieData {
    //用户关心数据
    private String name;//
    private float value;//
    private float percentage;
    //非用户关心数据
    private int color=0; //颜色
    private float angle=0;//角度

    public PieData(float value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
