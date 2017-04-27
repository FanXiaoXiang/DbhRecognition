package com.bjfu.dbhrecognition.engine.check_border;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.bjfu.dbhrecognition.engine.color_recognition.ColorRecognition;
import com.bjfu.dbhrecognition.engine.color_recognition.utils.OrientType;
import com.bjfu.dbhrecognition.entity.Color;

/**
 * Created by 11827 on 2017/3/20.
 */

public class CheckBorder {
    private final float skipCoe = 0.042f;//0.056
    private int samplePlots = 1;
    private Bitmap bm;
    private ColorRecognition recognition;

    private OrientType type;
    private int position;

    private Point intTop, intBottom, decTop, decBottom;

    public CheckBorder(Bitmap bm, ColorRecognition recognition, OrientType type, int position) {
        this.bm = bm;
        this.type = type;
        this.position = position;
        this.recognition = recognition;
        findBorder();
    }

    private void findBorder() {
        int center, skip;
        int length = bm.getWidth();
        if (type == OrientType.VERTICAL) length = bm.getHeight();
        switch (type) {
            case HORIZONTAL:
                intTop = new Point(findBorder(samplePlots * 2 + 1, length / 2, position, type), position);
                decBottom = new Point(findBorder(length - (samplePlots * 2 + 1), length / 2, position, type), position);
                center = (intTop.x + decBottom.x) / 2;
                skip = (int) ((decBottom.x - intTop.x) * skipCoe);
                intBottom = new Point(findBorder(center - skip, intTop.x, position, type), position);
                decTop = new Point(findBorder(center + skip, decBottom.x, position, type), position);
                if (intBottom.x - intTop.x < decBottom.x - decTop.x) {
                    int temp = intTop.x;
                    intTop.x = decTop.x;
                    decTop.x = temp;

                    temp = intBottom.x;
                    intBottom.x = decBottom.x;
                    decBottom.x = temp;
                }
                break;
            case VERTICAL:
                intTop = new Point(position, findBorder(samplePlots * 2 + 1, length / 2, position, type));
                decBottom = new Point(position, findBorder(length - (samplePlots * 2 + 1), length / 2, position, type));
                center = (intTop.y + decBottom.y) / 2;
                skip = (int) ((decBottom.y - intTop.y) * skipCoe);
                intBottom = new Point(position, findBorder(center - skip, intTop.y, position, type));
                decTop = new Point(position, findBorder(center + skip, decBottom.y, position, type));
                if (intBottom.y - intTop.y < decBottom.y - decTop.y) {
                    int temp = intTop.y;
                    intTop.y = decTop.y;
                    decTop.y = temp;

                    temp = intBottom.y;
                    intBottom.y = decBottom.y;
                    decBottom.y = temp;
                }
                break;
        }
    }

    private int findBorder(int start, int end, int line, OrientType orient) {
        if (start == end) return start;
        Color color = new Color();
        int temp = start;
        int unit = (end - start) / Math.abs(end - start);
        for (; unit > 0 ? temp < end : temp > end; temp += unit) {
            if (orient == OrientType.HORIZONTAL) {
                if (recognition.isWhite(getSamplePlotsColor(temp, line, color)))
                    break;
            } else {
                if (recognition.isWhite(getSamplePlotsColor(line, temp, color)))
                    break;
            }
        }
        for (; unit > 0 ? temp < end : temp > end; temp += unit) {
            if (orient == OrientType.HORIZONTAL) {
                if (recognition.isBlack(getSamplePlotsColor(temp, line, color)))
                    break;
            } else {
                if (recognition.isBlack(getSamplePlotsColor(line, temp, color)))
                    break;
            }
        }

        int temp0 = temp;
        for (; unit > 0 ? temp < end : temp > end; temp += unit) {
            if (orient == OrientType.HORIZONTAL) {
                if (!recognition.isBlack(getSamplePlotsColor(temp, line, color))) {
                    temp -= unit;
                    break;
                }
            } else {
                if (!recognition.isBlack(getSamplePlotsColor(line, temp, color))) {
                    temp -= unit;
                    break;
                }
            }
        }
        return (temp0 + temp) / 2;
    }

    private Color getSamplePlotsColor(int x, int y, Color color) {
        if (x < samplePlots || y < samplePlots ||
                x > bm.getWidth() - 1 - samplePlots ||
                y > bm.getHeight() - 1 - samplePlots) {
            color.reset(bm.getPixel(x, y));
            return color;
        }
        int r = 0, g = 0, b = 0;
        for (int deltaX = -samplePlots; deltaX <= samplePlots; deltaX++) {
            for (int deltaY = -samplePlots; deltaY <= samplePlots; deltaY++) {
                color.reset(bm.getPixel(x + deltaX, y + deltaY));
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
            }
        }
        int pointNum = (2 * samplePlots + 1) * (2 * samplePlots + 1);
        color.reset(0xff, r / pointNum, g / pointNum, b / pointNum);
        return color;
    }

    public Point getIntTop() {
        return intTop;
    }

    public Point getIntBottom() {
        return intBottom;
    }

    public Point getDecTop() {
        return decTop;
    }

    public Point getDecBottom() {
        return decBottom;
    }
}
