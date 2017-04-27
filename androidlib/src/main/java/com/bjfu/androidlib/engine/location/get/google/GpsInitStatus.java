package com.bjfu.androidlib.engine.location.get.google;

/**
 * Created by 11827 on 2017/2/19.
 */

public enum GpsInitStatus {

    AVAILABLE("当前GPS状态为可见状态"),
    OUT_OF_SERVICE("当前GPS状态为服务区外状态"),
    TEMPORARILY_UNAVAILABLE("当前GPS状态为暂停服务状态");
    private String status;

    private GpsInitStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
