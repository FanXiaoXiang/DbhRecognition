package com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.utils;

/**
 * Created by 11827 on 2017/2/16.
 */

public class Ellipsoid {
    private String name;
    private double longAxle;//长半轴，短半轴
    private double f;//扁率

    public Ellipsoid(String name, double longAxle, double f) {
        this.name = name;
        this.longAxle = longAxle;
        this.f = f;
    }

    public String getName() {
        return name;
    }

    public double getLongAxle() {
        return longAxle;
    }

    public double getF() {
        return f;
    }
}
