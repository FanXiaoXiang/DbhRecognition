package com.bjfu.dbhrecognition.listener;

/**
 * Created by 11827 on 2017/4/1.
 */

public interface OnCheckedPermissionListener {
    public void onCheckFinish(String permission, int requestCode, boolean success);
}
