package com.qifanrui.icarepro.uitls;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by de on 2015/10/23.
 */
public class SharedPreferencesUitls {

    public static String USERNMAE="usernmae";
    public static String PASSWORD="password";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public SharedPreferencesUitls(Context context) {
        sharedPreferences = context.getSharedPreferences("config.xml", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void savaStringData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key){
        return sharedPreferences.getString(key,null);
    }
}
