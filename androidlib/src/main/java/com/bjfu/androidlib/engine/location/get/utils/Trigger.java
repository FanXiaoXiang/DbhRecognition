package com.bjfu.androidlib.engine.location.get.utils;

/**
 * Created by 11827 on 2017/2/19.
 */

public interface Trigger {
    public boolean start(StatusChangeListener listener);

    public void stop();
}
