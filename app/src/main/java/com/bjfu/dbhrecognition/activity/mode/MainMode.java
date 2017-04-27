package com.bjfu.dbhrecognition.activity.mode;

import android.content.Context;

import com.bjfu.dbhrecognition.engine.DecodeEngine;
import com.bjfu.dbhrecognition.listener.OnParseFinishListener;

/**
 * Created by 11827 on 2017/3/20.
 */

public class MainMode {
    private Context context;

    public MainMode(Context context) {
        this.context = context;
    }

    public void parsePic(byte[] data, OnParseFinishListener listener) {
        new DecodeEngine(data, listener);
    }
}
