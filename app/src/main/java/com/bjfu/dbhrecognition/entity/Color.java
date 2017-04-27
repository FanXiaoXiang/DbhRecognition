package com.bjfu.dbhrecognition.entity;

/**
 * Created by 11827 on 2016/11/23.
 */

public class Color {
    private int c;
    private int alpha, red, green, blue;

    public Color(int alpha, int red, int green, int blue) {
        reset(alpha, red, green, blue);
    }

    public Color(int color) {
        reset(color);
    }

    public Color(Color color) {
        this.c = color.getC();
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public Color() {
        reset(0xff000000);
    }

    public Color reset(int color) {
        c = color;
        alpha = (color & 0xff000000) >>> 24;
        red = (color & 0x00ff0000) >>> 16;
        green = (color & 0x0000ff00) >>> 8;
        blue = color & 0x000000ff;
        return this;
    }

    public Color reset(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
        return this;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return alpha + " " + red + " " + green + " " + blue;
    }
}
