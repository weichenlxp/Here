package edu.hebut.here.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WindowUtils {

    //获取屏幕宽度
    public static int getScreenWidth(Context context) {
        //获取窗口管理服务
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //屏幕参数对象
        DisplayMetrics outMetrics = new DisplayMetrics();

        //获取屏幕参数
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }
}
