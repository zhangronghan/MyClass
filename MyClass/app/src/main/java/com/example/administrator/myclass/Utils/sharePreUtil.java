package com.example.administrator.myclass.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/9/12.
 */

public  class SharePreUtil {
    private static final String FIRST_RUN="first_run";
    private static final String FIRST_SP="first_sp";

    //判断是否是第一次使用
    public static boolean getFirstRun(Context context){
        SharedPreferences sp=context.getSharedPreferences(FIRST_SP,Context.MODE_PRIVATE);
        return sp.getBoolean(FIRST_RUN,true);
    }

    public static void setFirstRun(Context context,boolean isFirst){
        SharedPreferences sp=context.getSharedPreferences(FIRST_SP,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(FIRST_RUN,isFirst);
        editor.commit();
    }



    private static final String NIGHT_MODE="night_mode";
    private static final String IS_NIGHT_MODE="sp_night_mode";

    //夜间模式
    public static boolean isNightMode(Context context){
        SharedPreferences sp=context.getSharedPreferences(NIGHT_MODE,context.MODE_PRIVATE);
        return sp.getBoolean(IS_NIGHT_MODE,false);
    }

    public static void setNightMode(Context context, boolean b) {
        SharedPreferences sp=context.getSharedPreferences(NIGHT_MODE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(IS_NIGHT_MODE,b);
        editor.commit();
    }




}
