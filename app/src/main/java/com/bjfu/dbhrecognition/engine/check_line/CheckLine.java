package com.bjfu.dbhrecognition.engine.check_line;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.bjfu.dbhrecognition.engine.color_recognition.ColorRecognition;
import com.bjfu.dbhrecognition.engine.color_recognition.utils.ColorType;
import com.bjfu.dbhrecognition.entity.Color;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 11827 on 2017/3/21.
 */

public class CheckLine {
    private float SCAN_THETA_ANGLE = 0.1f;
    private Bitmap bitmap;
    private int samplePlots = 1;
    private ColorType colorType;
    private ColorRecognition recognition;
    private Point point;
    private Set<Point> lines;
    private Canvas canvas;
    private Paint yellowPaint;
    private Paint greenPaint;

    public CheckLine(Bitmap bitmap, Point point, ColorRecognition recognition) {
        lines = new HashSet<>();
        yellowPaint = new Paint();
        yellowPaint.setColor(android.graphics.Color.YELLOW);
        greenPaint = new Paint();
        greenPaint.setColor(android.graphics.Color.GREEN);

        reset(bitmap, point, recognition);
    }

    public CheckLine reset(Bitmap bitmap, Point point, ColorRecognition recognition) {
        this.bitmap = bitmap;
        canvas = new Canvas(bitmap);
        this.point = point;
        this.recognition = recognition;
        lines.clear();
        Color color = new Color();
        colorType = recognition.getColorType(getSamplePlotsColor(point.x, point.y, samplePlots, color));
        findLines();
        return this;
    }

    /**
     * 查找所有线条
     */
    private void findLines() {
        int pointR = findLineRadius(point, colorType);
        int r = Math.round(pointR * 1.5f);
        int x, y;
        double rad;
        Color color = new Color();
        for (float theta = 0.0f; theta < 360.0f; theta += 1) {
            rad = theta / 180.0f * Math.PI;
            x = (int) Math.round(point.x + r * Math.cos(rad));
            y = (int) Math.round(point.y + r * Math.sin(rad));
            if (x < 0 || y < 0 || x > bitmap.getWidth() - 1 || y > bitmap.getHeight() - 1)
                continue;
            if (colorType == recognition.getColorType(getSamplePlotsColor(x, y, samplePlots, color))) {
                Point p = new Point();
                theta = move2LineCenter(point, p, r, theta, colorType) + 1;
                lines.add(p);
            }
        }
        if (lines.size() <= 0) return;
        for (Point end : lines) {
            extendLine(point, end, pointR, colorType);
//            canvas.drawCircle(end.x, end.y, pointR, greenPaint);
        }
//        canvas.drawCircle(point.x, point.y, pointR, yellowPaint);
    }

    /**
     * 获取线宽以内圆的最大半径
     *
     * @param center 中心点
     * @param type   该点的颜色类型
     * @return 该圆的半径
     */
    private int findLineRadius(Point center, ColorType type) {
        int r = 0;
        boolean isChecking = true;
        Color color = new Color();
        int x, y;
        double rad;
        while (isChecking) {
            r += samplePlots * 2 + 1;
            for (float theta = 0.0f; theta < 360.0f; theta += 5.0f) {
                rad = theta / 180.0f * Math.PI;
                x = (int) Math.round(center.x + r * Math.cos(rad));
                y = (int) Math.round(center.y + r * Math.sin(rad));
                if (x < 0 || y < 0 || x > bitmap.getWidth() - 1 || y > bitmap.getHeight() - 1)
                    return r;
                if (type != recognition.getColorType(getSamplePlotsColor(x, y, samplePlots, color))) {
                    return r;
                }
            }
        }
        return r;
    }

    /**
     * 根据端点与中心点连线构成直线与中心点在r半径交点来延伸线条
     *
     * @param center 中心点
     * @param end    端点
     * @param step   步长
     */
    private void extendLine(Point center, Point end, int step, ColorType type) {
        float theta;
        int r = (int) Math.round(Math.sqrt((end.y - center.y) * (end.y - center.y) + (end.x - center.x) * (end.x - center.x)));
        Color color = new Color();
        int x, y;
        do {
            r += step;
            theta = (float) Math.atan2(end.y - center.y, end.x - center.x);
            if (theta < 0) theta += 2 * Math.PI;
            x = (int) (r * Math.cos(theta)) + center.x;
            y = (int) (r * Math.sin(theta)) + center.y;
            if (x < 0 || y < 0 || x > bitmap.getWidth() - 1 || y > bitmap.getHeight() - 1) {
                break;
            }
            if (type != recognition.getColorType(getSamplePlotsColor(x, y, samplePlots, color))) {
                for (int i = r - 1; i >= r - step; i--) {
                    x = (int) (i * Math.cos(theta)) + center.x;
                    y = (int) (i * Math.sin(theta)) + center.y;
                    if (type == recognition.getColorType(getSamplePlotsColor(x, y, samplePlots, color))) {
                        end.x = x;
                        end.y = y;
                    }
                }
                break;
            } else {
                end.x = x;
                end.y = y;
                move2LineCenter(center, end, r, (float) (theta * 180 / Math.PI), type);
            }
        } while (true);
    }

    /**
     * 将端点移动到线的中间
     *
     * @param center 中点
     * @param end    端点
     * @param r      半径
     * @param type   端点颜色类型
     * @param theta  此时端点所对应角度
     * @return 顺时针结束点
     */
    private float move2LineCenter(Point center, Point end, int r, float theta, ColorType type) {
        float thetaClockwise = theta;
        float thetaAntiClockwise = theta;
        Color color = new Color();
        int x, y;
        for (; thetaClockwise < theta + 90; thetaClockwise += SCAN_THETA_ANGLE) {
            x = (int) Math.round(center.x + r * Math.cos(thetaClockwise / 180 * Math.PI));
            y = (int) Math.round(center.y + r * Math.sin(thetaClockwise / 180 * Math.PI));
            if (x < 0 || y < 0 || x > bitmap.getWidth() - 1 || y > bitmap.getHeight() - 1) {
                thetaClockwise -= SCAN_THETA_ANGLE;
                break;
            }
            if (type != recognition.getColorType(getSamplePlotsColor(x, y, samplePlots, color))) {
                thetaClockwise -= SCAN_THETA_ANGLE;
                break;
            }
        }

        for (; thetaAntiClockwise >= theta - 90; thetaAntiClockwise -= SCAN_THETA_ANGLE) {
            x = (int) Math.round(center.x + r * Math.cos(thetaAntiClockwise / 180 * Math.PI));
            y = (int) Math.round(center.y + r * Math.sin(thetaAntiClockwise / 180 * Math.PI));
            if (x < 0 || y < 0 || x > bitmap.getWidth() - 1 || y > bitmap.getHeight() - 1) {
                thetaAntiClockwise += SCAN_THETA_ANGLE;
                break;
            }
            if (type != recognition.getColorType(getSamplePlotsColor(x, y, samplePlots, color))) {
                thetaAntiClockwise += SCAN_THETA_ANGLE;
                break;
            }
        }
        theta = (thetaClockwise + thetaAntiClockwise) / 2;
        end.x = (int) Math.round(center.x + r * Math.cos(theta / 180 * Math.PI));
        end.y = (int) Math.round(center.y + r * Math.sin(theta / 180 * Math.PI));
        return thetaClockwise;
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
    private Color getSamplePlotsColor(int x, int y, int samplePlots, Color color) {
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

    public Point[] getEnds() {
        Point[] points = null;
        if (lines.size() > 0) {
            points = new Point[lines.size()];
            Iterator<Point> iterator = lines.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                points[i++] = iterator.next();
            }
        }
        return points;
    }
}
