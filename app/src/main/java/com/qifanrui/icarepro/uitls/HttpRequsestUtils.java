package com.qifanrui.icarepro.uitls;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Created by de on 2015/10/21.
 */
public class HttpRequsestUtils {

    private static final String TAG = "HttpRequsestUtils";
    public static RequestQueue requestQueue;
    public static String tokenId = null;
    public final static int NETWORK_ERROR = 400;
    public final static int LOGIN_SECCESS = 1000;
    public final static int NOW_DATA_SECCESS = 2000;

    public static void init(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.start();
        }
    }

    public static void doGet(Context context, String url, final Handler handler, final int seccess) {
        init(context);

        MyRequest request = new MyRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                Message message = Message.obtain();
                message.what = seccess;
                message.obj = object;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = Message.obtain();
                message.what = NETWORK_ERROR;
                message.obj = volleyError;
                handler.sendMessage(message);
            }
        });
        request.setHeaders("Set-Cookie", "CLIENTID:" + tokenId);
        requestQueue.add(request);
    }

    public static void doPost(Context context, String url, JSONObject object, final Handler handler, final int seccess) {
        init(context);
        MyRequest request = new MyRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                Message message = Message.obtain();
                message.what = seccess;
                message.obj = object;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = Message.obtain();
                message.what = NETWORK_ERROR;
                message.obj = volleyError;
                handler.sendMessage(message);
            }
        });
        request.setHeaders("Set-Cookie", "CLIENTID:" + tokenId);
        // Log.i(TAG，request.getHeaders().toString());
        requestQueue.add(request);
    }





}
