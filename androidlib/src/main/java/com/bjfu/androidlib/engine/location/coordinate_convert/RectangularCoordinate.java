package com.bjfu.androidlib.engine.location.coordinate_convert;


import com.bjfu.androidlib.engine.location.coordinate_convert.utils.SplitSymbol;

import java.io.Serializable;

/**
 * Created by 11827 on 2017/2/16.
 */

public class RectangularCoordinate implements Serializable {
    public double x;
    public double y;
    public double h;

    public RectangularCoordinate(String coo) {
        String[] coos = coo.split(SplitSymbol.COMMA);
        if (coos == null || coos.length != 3) return;
        try {
            x = Double.parseDouble(coos[0]);
            y = Double.parseDouble(coos[1]);
            h = Double.parseDouble(coos[2]);
        } catch (Exception e) {
            throw new RuntimeException("parse String to RectangularCoordinate exception");
        }
    }

    public RectangularCoordinate(double x, double y, double h) {
        this.x = x;
        this.y = y;
        this.h = h;
    }

    public RectangularCoordinate() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    @Override
    public String toString() {
        return "" + x + SplitSymbol.COMMA + y + SplitSymbol.COMMA + h;
    }
}
