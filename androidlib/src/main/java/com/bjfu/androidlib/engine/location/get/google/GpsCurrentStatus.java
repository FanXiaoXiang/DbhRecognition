package com.bjfu.androidlib.engine.location.get.google;

/**
 * Created by 11827 on 2017/2/20.
 */
public enum GpsCurrentStatus {
    GPS_EVENT_FIRST_FIX("第一次定位"),
    GPS_EVENT_SATELLITE_STATUS("卫星状态改变"),
    GPS_EVENT_STARTED("定位启动"),
    GPS_EVENT_STOPPED("定位结束");

    private String status;

    private GpsCurrentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
