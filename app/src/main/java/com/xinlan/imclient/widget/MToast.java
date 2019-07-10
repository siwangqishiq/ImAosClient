package com.xinlan.imclient.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xinlan.imclient.R;

/**
 * Created by ecp on 2016/12/13.
 */
public class MToast extends Toast{
    public MToast(Context context) {
        super(context);
    }


    public static MToast makeText(Context context, CharSequence text, int duration) {
        MToast result = new MToast(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.m_toast, null);
        TextView tv = (TextView)v.findViewById(R.id.m_message);
        tv.setText(text);
        result.setView(v);
        result.setDuration(duration);
        return result;
    }
}
