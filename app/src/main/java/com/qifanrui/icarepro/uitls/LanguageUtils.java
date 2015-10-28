package com.qifanrui.icarepro.uitls;

import android.content.Context;

import java.util.Locale;

/**
 * Created by de on 2015/10/28.
 */
public class LanguageUtils {

    /**
     *获取android本地语言
     * @param context
     * @return
     */
    public static String getLanguageTypeCode(Context context){
        Locale locale=context.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }

}
