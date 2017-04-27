package com.bjfu.dbhrecognition.engine.color_recognition;


import android.graphics.Bitmap;

import com.bjfu.dbhrecognition.engine.color_recognition.utils.ColorType;
import com.bjfu.dbhrecognition.engine.color_recognition.utils.OrientType;
import com.bjfu.dbhrecognition.entity.Color;

/**
 * Created by 11827 on 2017/3/19.
 */

public class ColorRecognition {
    private final float definition = 0.25f;
    private Color maxColor;
    private Color minColor;
    private Color avgColor;
    private Color topColor;
    private Color bottomColor;

    public ColorRecognition(Bitmap bm, OrientType type, int position) {
        avgColor = new Color(0xff000000);
        maxColor = new Color(0xff000000);
        minColor = new Color(0xff000000);
        topColor = new Color(0xff000000);
        bottomColor = new Color(0xff000000);
        reset(bm, type, position);
    }

    public ColorRecognition reset(Bitmap bm, OrientType type, int position) {
        Color temp = new Color(0xff000000);
        switch (type) {
            case HORIZONTAL:
                for (int x = 0, y = position; x < bm.getWidth(); x++) {
                    temp.reset(bm.getPixel(x, y));
                    if (x == 0) {
                        topColor.reset(temp.getC());
                        bottomColor.reset(temp.getC());
                    } else {
                        if (temp.getRed() > maxColor.getRed())
                            maxColor.setRed(temp.getRed());
                        if (temp.getGreen() > maxColor.getGreen())
                            maxColor.setGreen(temp.getGreen());
                        if (temp.getBlue() > maxColor.getBlue())
                            maxColor.setBlue(temp.getBlue());

                        if (temp.getRed() < minColor.getRed())
                            minColor.setRed(temp.getRed());
                        if (temp.getGreen() < minColor.getGreen())
                            minColor.setGreen(temp.getGreen());
                        if (temp.getBlue() < minColor.getBlue())
                            minColor.setBlue(temp.getBlue());
                    }
                }
                break;
            case VERTICAL:
                for (int y = 0, x = position; y < bm.getHeight(); y++) {
                    temp.reset(bm.getPixel(x, y));
                    if (y == 0) {
                        maxColor.reset(temp.getC());
                        minColor.reset(temp.getC());
                    } else {
                        if (temp.getRed() > maxColor.getRed())
                            maxColor.setRed(temp.getRed());
                        if (temp.getGreen() > maxColor.getGreen())
                            maxColor.setGreen(temp.getGreen());
                        if (temp.getBlue() > maxColor.getBlue())
                            maxColor.setBlue(temp.getBlue());

                        if (temp.getRed() < minColor.getRed())
                            minColor.setRed(temp.getRed());
                        if (temp.getGreen() < minColor.getGreen())
                            minColor.setGreen(temp.getGreen());
                        if (temp.getBlue() < minColor.getBlue())
                            minColor.setBlue(temp.getBlue());
                    }
                }
                break;
        }
        resetParas();
        return this;
    }

    private void resetParas() {

        avgColor.setRed((minColor.getRed() + maxColor.getRed()) / 2);
        avgColor.setGreen((minColor.getGreen() + maxColor.getGreen()) / 2);
        avgColor.setBlue((minColor.getBlue() + maxColor.getBlue()) / 2);

        topColor.setRed((int) (avgColor.getRed() + (maxColor.getRed() - avgColor.getRed()) * definition));
        topColor.setGreen((int) (avgColor.getGreen() + (maxColor.getGreen() - avgColor.getGreen()) * definition));
        topColor.setBlue((int) (avgColor.getBlue() + (maxColor.getBlue() - avgColor.getBlue()) * definition));

        bottomColor.setRed((int) (avgColor.getRed() - (avgColor.getRed() - minColor.getRed()) * definition));
        bottomColor.setGreen((int) (avgColor.getGreen() - (avgColor.getGreen() - minColor.getGreen()) * definition));
        bottomColor.setBlue((int) (avgColor.getBlue() - (avgColor.getBlue() - minColor.getBlue()) * definition));
    }

    public ColorType getColorType(Color color) {
        if (isBlack(color)) return ColorType.BLACK;
        if (isWhite(color)) return ColorType.WHITE;
        if (isRed(color)) return ColorType.RED;
        if (isGreen(color)) return ColorType.GREEN;
        if (isBlue(color)) return ColorType.BLUE;
        return ColorType.OTHER;
    }

    public ColorType getColorType(int color) {
        return getColorType(new Color(color));
    }

    public boolean isWhite(Color color) {
        if (color.getRed() > topColor.getRed() && color.getGreen() > topColor.getGreen() && color.getBlue() > topColor.getBlue()) {
            return true;
        }
        return false;
    }

    public boolean isWhite(int color) {
        return isWhite(new Color(color));
    }

    public boolean isBlack(Color color) {
        if (color.getRed() < bottomColor.getRed() && color.getGreen() < bottomColor.getGreen() && color.getBlue() < bottomColor.getBlue()) {
            return true;
        }
        return false;
    }

    public boolean isBlack(int color) {
        return isBlack(new Color(color));
    }


    public boolean isRed(Color color) {
        if (isWhite(color)) return false;
        if (isBlack(color)) return false;
        if (color.getRed() > color.getBlue() && color.getRed() > color.getGreen()) return true;
        return false;
    }

    public boolean isRed(int color) {
        return isRed(new Color(color));
    }


    public boolean isGreen(Color color) {
        if (isWhite(color)) return false;
        if (isBlack(color)) return false;
        if (color.getGreen() > color.getBlue() && color.getGreen() > color.getRed()) return true;
        return false;
    }

    public boolean isGreen(int color) {
        return isGreen(new Color(color));
    }

    public boolean isBlue(Color color) {
        if (isWhite(color)) return false;
        if (isBlack(color)) return false;
        if (color.getBlue() > color.getRed() && color.getBlue() > color.getGreen()) return true;
        return false;
    }

    public boolean isBlue(int color) {
        return isBlue(new Color(color));
    }

}
