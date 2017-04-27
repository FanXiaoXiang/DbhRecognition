package com.bjfu.androidlib.engine.location.coordinate_convert;

import com.bjfu.androidlib.engine.location.coordinate_convert.utils.SplitSymbol;

import java.io.Serializable;

/**
 * Created by 11827 on 2017/2/16.
 */

public class LongitudeCoordinate implements Serializable {
    private double longitude;//经度
    private double latitude;//纬度
    private double altitude;//高程

    public LongitudeCoordinate(String coo) {
        String[] coos = coo.split(SplitSymbol.COMMA);
        if (coos == null || coos.length != 3) return;
        try {
            latitude = Double.parseDouble(coos[0]);
            longitude = Double.parseDouble(coos[1]);
            altitude = Double.parseDouble(coos[2]);
        } catch (Exception e) {
            throw new RuntimeException("parse String to LongitudeCoordinate exception");
        }
    }

    public LongitudeCoordinate(double latitude, double longitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public LongitudeCoordinate() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "" + latitude + SplitSymbol.COMMA + longitude + SplitSymbol.COMMA + altitude;
    }
}
