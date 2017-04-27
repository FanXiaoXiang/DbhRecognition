package com.bjfu.dbhrecognition.engine.decode;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.bjfu.dbhrecognition.engine.color_recognition.ColorRecognition;
import com.bjfu.dbhrecognition.engine.color_recognition.utils.ColorType;
import com.bjfu.dbhrecognition.engine.encode.EncodeLibBaseRed;
import com.bjfu.dbhrecognition.entity.Color;

/**
 * Created by 11827 on 2017/3/18.
 */

public class DecodeBaseRed extends Decode {

    private static volatile DecodeBaseRed decode;
    private int samplePlots = 2;

    private DecodeBaseRed() {
    }

    public static DecodeBaseRed getInstance() {
        if (decode == null) {
            synchronized (DecodeBaseRed.class) {
                if (decode == null) {
                    decode = new DecodeBaseRed();
                }
            }
        }
        return decode;
    }

    @Override
    public String getIntegerPart(Bitmap bm, Point start, Point end, ColorRecognition recognition) {
        if (bm == null || start == null || end == null || recognition == null || start == end)
            return null;
        sortPoint(start, end);
        double step;//获取每个模块的宽度
        double k = 0;
        double b = 0;

        if (start.x == end.x) {
            step = (end.y - start.y) * 1.0 / ((EncodeLibBaseRed.INT_PART_MODELS - 1) * 1.0);
        } else {
            step = (end.x - start.x) * 1.0 / ((EncodeLibBaseRed.INT_PART_MODELS - 1) * 1.0);
            k = (end.y - start.y) * 1.0 / (end.x - start.x) * 1.0;
            b = start.y - k * start.x;
        }
        //获取每个模块对应的颜色类型，并获取值，添加到buffer中
        StringBuffer buffer = new StringBuffer(EncodeLibBaseRed.INT_PART_MODELS);
        Color color = new Color();
        for (int i = 0; i < EncodeLibBaseRed.INT_PART_MODELS; i++) {
            if (i == 0 || i == EncodeLibBaseRed.INT_PART_MODELS - 1) {
                buffer.append(ColorType.BLACK.getValue());
            } else {
                if (start.x == end.x) {
                    getSamplePlotsColor(bm, start.x, (int) (start.y + i * step), samplePlots, color);
                } else {
                    double x = start.x + i * step;
                    getSamplePlotsColor(bm, (int) x, (int) (k * x + b), samplePlots, color);
                }
                buffer.append(recognition.getColorType(color).getValue());
            }
        }
        //根据初始符、终止符判断是否为该编码类型，若不是该编码类型则返回null
        String code = buffer.toString().trim();
        if (code.length() != EncodeLibBaseRed.INT_PART_MODELS) return null;
        if (!code.endsWith(EncodeLibBaseRed.ENCODE_END))
            code = buffer.reverse().toString().trim();
        //若是该编码类型，截取数据区，然后解码两位数字位，并放入buffer中，因为解码失败返回-1，所以当buffer长度
        //为2时解码正确，此时返回相应值
        code = code.substring(3, 17);
        buffer.delete(0, buffer.length());
        buffer.append(EncodeLibBaseRed.getValue(code.substring(0, 7)));
        buffer.append(EncodeLibBaseRed.getValue(code.substring(7, 14)));
        if (buffer.length() == 2) return buffer.toString().trim();
        return null;
    }

    @Override
    public String getDecimalPart(Bitmap bm, Point start, Point end, ColorRecognition recognition) {
        if (bm == null || start == null || end == null || recognition == null || start == end)
            return null;
        sortPoint(start, end);
        double step;//获取每个模块的宽度
        double k = 0;
        double b = 0;
        if (start.x == end.x) {
            step = (end.y - start.y) * 1.0 / ((EncodeLibBaseRed.DECIMAL_PART_MODELS - 1) * 1.0);
        } else {
            step = (end.x - start.x) * 1.0 / ((EncodeLibBaseRed.DECIMAL_PART_MODELS - 1) * 1.0);
            k = (end.y - start.y) * 1.0 / (end.x - start.x) * 1.0;
            b = start.y - k * start.x;
        }
        //获取每个模块对应的颜色类型，并获取值，添加到buffer中
        StringBuffer buffer = new StringBuffer(EncodeLibBaseRed.DECIMAL_PART_MODELS);
        Color color = new Color();
        for (int i = 0; i < EncodeLibBaseRed.DECIMAL_PART_MODELS; i++) {
            if (i == 0 || i == EncodeLibBaseRed.DECIMAL_PART_MODELS - 1) {
                buffer.append(ColorType.BLACK.getValue());
            } else {
                if (start.x == end.x) {
                    getSamplePlotsColor(bm, start.x, (int) (start.y + i * step), samplePlots, color);
                } else {
                    double x = start.x + i * step;
                    getSamplePlotsColor(bm, (int) x, (int) (k * x + b), samplePlots, color);
                }
                buffer.append(recognition.getColorType(color).getValue());
            }
        }
        //根据初始符、终止符判断是否为该编码类型，若不是该编码类型则返回null
        String code = buffer.toString().trim();
        if (code.length() != EncodeLibBaseRed.DECIMAL_PART_MODELS) return null;
        if (!code.endsWith(EncodeLibBaseRed.ENCODE_END))
            code = buffer.reverse().toString().trim();
        //截取数据区，解码，并将数据放入buffer中，若长度为1，说明解码正确，返回相应的小数值
        code = code.substring(3, 10);
        buffer.delete(0, buffer.length());
        buffer.append(EncodeLibBaseRed.getValue(code));
        if (buffer.length() == 1) return buffer.toString().trim();
        return null;
    }

    private void sortPoint(Point start, Point end) {
        if (start == null || end == null) return;
        int temp;
        if (start.x == end.x) {
            if (start.y > end.y) {
                temp = start.y;
                start.y = end.y;
                end.y = temp;
            }
            return;
        }
        if (start.y == end.y) {
            if (start.x > end.x) {
                temp = start.x;
                start.x = end.x;
                end.x = temp;
            }
            return;
        }

        if (start.x > end.x) {
            temp = start.x;
            start.x = end.x;
            end.x = temp;

            temp = start.y;
            start.y = end.y;
            end.y = temp;
        }
    }

    /**
     * 获取样本的颜色
     *
     * @param x           当前点坐标
     * @param y           当前点坐标
     * @param samplePlots 样本围绕当前坐标圈数
     * @param color       样本点颜色容器
     * @return 即颜色容器
     */
    private Color getSamplePlotsColor(Bitmap bitmap, int x, int y, int samplePlots, Color color) {
        if (x < samplePlots || y < samplePlots ||
                x > bitmap.getWidth() - 1 - samplePlots ||
                y > bitmap.getHeight() - 1 - samplePlots) {
            color.reset(bitmap.getPixel(x, y));
            return color;
        }
        int r = 0, g = 0, b = 0;
        for (int deltaX = -samplePlots; deltaX <= samplePlots; deltaX++) {
            for (int deltaY = -samplePlots; deltaY <= samplePlots; deltaY++) {
                color.reset(bitmap.getPixel(x + deltaX, y + deltaY));
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
            }
        }
        int pointNum = (2 * samplePlots + 1) * (2 * samplePlots + 1);
        color.reset(0xff, r / pointNum, g / pointNum, b / pointNum);
        return color;
    }
}
