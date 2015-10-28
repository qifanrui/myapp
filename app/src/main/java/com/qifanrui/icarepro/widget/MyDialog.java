package com.qifanrui.icarepro.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.qifanrui.icarepro.R;
import com.qifanrui.icarepro.uitls.DimensionUitls;

/**
 * Created by de on 2015/10/22.
 */
public class MyDialog {

    private  Dialog mDialog;
    private  View view;
    private Context context;



    public MyDialog(Context context){
        mDialog = new AlertDialog.Builder(context).create();
        view = View.inflate(context, R.layout.load_dialog, null);
        this.context=context;
    }

    public void show(String tvString) {
        mDialog.show();
        TextView textView = (TextView) view.findViewById(R.id.tv_load_text);
        textView.setText(tvString);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        //设置dialog的属性

        WindowManager.LayoutParams lp= mDialog.getWindow().getAttributes();
        lp.alpha=0.7f;
        lp.width = DimensionUitls.dip2px(context,100); // 宽度
        lp.height = DimensionUitls.dip2px(context, 100);//高度
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.load_dialog_bg);
    }


    public void dismiss() {
        mDialog.dismiss();
    }

}
