package com.qifanrui.icarepro;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.qifanrui.icarepro.uitls.HttpRequsestUtils;
import com.qifanrui.icarepro.uitls.LanguageUtils;
import com.qifanrui.icarepro.uitls.SharedPreferencesUitls;
import com.qifanrui.icarepro.widget.MyDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private MyDialog mDialog;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mLoginView;
    private TextView mErrorMsgView;
    private String user;
    private String pwd;

    private SharedPreferencesUitls spu;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject object = null;
            VolleyError error = null;
            boolean flag = false;
            String message = null;
            switch (msg.what) {
                case HttpRequsestUtils.LOGIN_SECCESS:

                    object = (JSONObject) msg.obj;
                    try {
                        HttpRequsestUtils.tokenId = object.getJSONObject("data").getString("token");
                        flag = object.getBoolean("status");
                        message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        Log.i("HttpRequsestUtils", object.toString());
                        HttpRequsestUtils.doGet(getApplicationContext(), "http://192.168.1.9:8080/webservice/rest/care/events/now/"+ LanguageUtils.getLanguageTypeCode(getApplicationContext()), handler, HttpRequsestUtils.NOW_DATA_SECCESS);
                        spu.savaStringData(SharedPreferencesUitls.USERNMAE, user);
                        spu.savaStringData(SharedPreferencesUitls.PASSWORD, pwd);
                    } else {
                        mDialog.dismiss();
                        mErrorMsgView.setVisibility(View.VISIBLE);
                        mErrorMsgView.setText(message);
                    }

                    break;

                case HttpRequsestUtils.NETWORK_ERROR:
                    error = (VolleyError) msg.obj;
                    Log.i("HttpRequsestUtils", "error:" + error.toString());
                    mErrorMsgView.setVisibility(View.VISIBLE);
                    mErrorMsgView.setText("不能连接到服务器");
                    mDialog.dismiss();
                    break;

                case HttpRequsestUtils.NOW_DATA_SECCESS:
                    object = (JSONObject) msg.obj;
                    try {
                        JSONArray alarming = object.getJSONObject("data").getJSONArray("alarming");
                        JSONArray general = object.getJSONObject("data").getJSONArray("general");
                        JSONArray warning = object.getJSONObject("data").getJSONArray("warning");
                        Log.i("HttpRequsestUtils", "length：" + alarming.length() + "data=====" + alarming.toString());
                        Log.i("HttpRequsestUtils", "length：" + general.length() + "data=====" + general.toString());
                        Log.i("HttpRequsestUtils", "length：" + warning.length() + "data=====" + warning.toString());
                        Log.i("HttpRequsestUtils", "status：" + object.getJSONObject("data").getInt("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mDialog.dismiss();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        spu = new SharedPreferencesUitls(this);
        initview();

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = spu.getStringData(SharedPreferencesUitls.USERNMAE);
        pwd = spu.getStringData(SharedPreferencesUitls.PASSWORD);

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {
            mUsernameView.setText(user);
            mPasswordView.setText(pwd);
        }
    }

    private void initview() {
        mUsernameView = (EditText) findViewById(R.id.et_username);
        mPasswordView = (EditText) findViewById(R.id.et_password);
        mErrorMsgView = (TextView) findViewById(R.id.tv_error_msg);
        mLoginView = (Button) findViewById(R.id.bt_login);

        mDialog = new MyDialog(this);

        mUsernameView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPasswordView.setText("");
                mErrorMsgView.setText("");
                mErrorMsgView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mErrorMsgView.setText("");
                mErrorMsgView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLoginView.setOnClickListener(this);

        mLoginView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLoginView.setAlpha(0.5f);

                        break;
                    case MotionEvent.ACTION_UP:
                        mLoginView.setAlpha(1.0f);
                        break;

                }

                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
        }
    }


    private void login() {
        user = mUsernameView.getText().toString().trim();
        pwd = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(user) && user.length() <= 0) {
            mErrorMsgView.setVisibility(View.INVISIBLE);
            mErrorMsgView.setText(R.string.username_not_null);
            return;
        }
        if (TextUtils.isEmpty(pwd) && pwd.length() <= 0) {
            mErrorMsgView.setVisibility(View.INVISIBLE);
            mErrorMsgView.setText(R.string.password_ont_null);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user);
            jsonObject.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequsestUtils.doPost(getApplicationContext(), "http://192.168.1.9:8080/webservice/rest/system/user/auth", jsonObject, handler, HttpRequsestUtils.LOGIN_SECCESS);
        mDialog.show(getString(R.string.logging));
    }
}

