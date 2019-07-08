package com.xinlan.imclient.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog  extends DialogFragment {
    private List<String> items = new ArrayList<String>(4);
    private List<View.OnClickListener> listeners = new ArrayList<View.OnClickListener>(4);

    public void addItem(final String item , final View.OnClickListener action){
        items.add(item);
        listeners.add(action);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] itemStrs = new String[items.size()];
        items.toArray(itemStrs);
        builder.setItems(itemStrs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog,final int which) {
                if(which >= 0 && which < listeners.size()){
                    listeners.get(which).onClick(getView());
                }
            }
        });
        return builder.create();
    }
}//end class
