package com.qifanrui.icarepro.uitls;

import android.content.Context;

/**
 * Created by de on 2015/10/23.
 */
public class DimensionUitls {

    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dip(Context context, int pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
