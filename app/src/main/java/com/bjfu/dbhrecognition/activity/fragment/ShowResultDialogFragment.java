package com.bjfu.dbhrecognition.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.bjfu.dbhrecognition.R;


/**
 * Created by 11827 on 2016/11/24.
 */

public class ShowResultDialogFragment extends DialogFragment {

    private String message = null;
    private TextView tvMsg;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diameter_result);
        tvMsg = new TextView(getActivity());
        tvMsg.setText(message);
        tvMsg.setTextSize(18);
        tvMsg.setTextColor(Color.RED);
        tvMsg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(tvMsg);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public void setMessage(String message) {
        this.message = message;
        if (tvMsg == null) return;
        tvMsg.setText(message);
    }
}
