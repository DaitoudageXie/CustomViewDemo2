package com.baochunhui.customviewdemo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Eric on 2017/3/20.
 */

public class UiUtils {
    static public int getScreenWithPixes(Context context){
        DisplayMetrics dm=new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
       return dm.widthPixels;
    }
    //
    static public float getScreenDensity(Context context){

        try{
            DisplayMetrics dm=new DisplayMetrics();
            ((WindowManager)context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            return dm.density;

        }catch (Exception e){
            return DisplayMetrics.DENSITY_DEFAULT;
        }

    }

    static public int dipToPix(Context context,int dip){
               return (int)(dip*getScreenDensity(context)+0.5f);


    }
}
