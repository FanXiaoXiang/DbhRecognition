package com.bjfu.dbhrecognition.utils;

import android.graphics.Bitmap;

/**
 * Created by 11827 on 2017/3/19.
 */

public class State {
    private static volatile State state;

    private State() {
    }

    public static State getSate() {
        if (state == null) {
            state = new State();
        }
        return state;
    }

    private Bitmap bitmap=null;
    private String info=null;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
