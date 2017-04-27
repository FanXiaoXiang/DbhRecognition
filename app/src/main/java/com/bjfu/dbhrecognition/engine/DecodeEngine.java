package com.bjfu.dbhrecognition.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import com.bjfu.dbhrecognition.engine.check_border.CheckBorder;
import com.bjfu.dbhrecognition.engine.check_line.CheckLine;
import com.bjfu.dbhrecognition.engine.color_recognition.ColorRecognition;
import com.bjfu.dbhrecognition.engine.color_recognition.utils.OrientType;
import com.bjfu.dbhrecognition.engine.decode.DecodeBaseRGB;
import com.bjfu.dbhrecognition.engine.decode.DecodeBaseRed;
import com.bjfu.dbhrecognition.listener.OnParseFinishListener;

/**
 * Created by 11827 on 2017/3/20.
 */

public class DecodeEngine extends Thread {
    private Bitmap bitmap;
    private OnParseFinishListener listener;
    private CheckBorder checkBorder;
    private ColorRecognition recognition;
    private String intPart = null, decPart = null;
    private DecodeBaseRed decodeBaseRed;
    private DecodeBaseRGB decodeBaseRGB;
    private CheckLine checkLine;

    private Point intInnerPoint;
    private Point decInnerPoint;
    private Point decOuterPoint;

    public DecodeEngine(byte[] data, OnParseFinishListener listener) {
        this.listener = listener;
        initBitmap(data);
        decodeBaseRed = DecodeBaseRed.getInstance();
        decodeBaseRGB = DecodeBaseRGB.getInstance();
        run();
    }

    private void initBitmap(final byte[] data) {
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        bitmap = Bitmap.createBitmap(bm, bm.getWidth() / 3, bm.getHeight() / 4, bm.getWidth() / 3, bm.getHeight() / 2, matrix, false);
        bm.recycle();
    }

    @Override
    public void run() {
        //确定颜色识别器
        recognition = new ColorRecognition(bitmap, OrientType.VERTICAL, bitmap.getWidth() / 2);
        //获取整数、小数边界
        checkBorder = new CheckBorder(bitmap, recognition, OrientType.VERTICAL, bitmap.getWidth() / 2);
        //解码整数部分
        intPart = decodeBaseRed.getIntegerPart(bitmap, new Point(checkBorder.getIntBottom()), new Point(checkBorder.getIntTop()), recognition);
        if (intPart == null)
            intPart = decodeBaseRGB.getIntegerPart(bitmap, new Point(checkBorder.getIntBottom()), new Point(checkBorder.getIntTop()), recognition);
        if (intPart == null) {
            notify(null);
            return;
        }
        //确定小数部分范围
        if (checkBorder.getIntBottom().y < checkBorder.getDecTop().y) {
            intInnerPoint = checkBorder.getIntBottom();
            decInnerPoint = checkBorder.getDecTop();
            decOuterPoint = checkBorder.getDecBottom();
        } else {
            intInnerPoint = checkBorder.getIntTop();
            decInnerPoint = checkBorder.getDecBottom();
            decOuterPoint = checkBorder.getDecTop();
        }
        //确定线条
        Point[] intInner = cleanSurplusPoint(checkEnds(intInnerPoint));
        Point[] decInner = cleanSurplusPoint(checkEnds(decInnerPoint));
        Point[] decOuter = cleanSurplusPoint(checkEnds(decOuterPoint));

        Point centerPoint = requestLinesCenterPoint(intInner, decInner);//整数部分内侧线与小数部分内测线的中点
        Point decInnerIntersection = requestIntersection(decInner[0], decInnerPoint, decInner[1], centerPoint);//中点与小数部分内外侧线的交点，这两点连线便是小数区对应的小数值
        Point decOuterIntersection = requestIntersection(decOuter[0], decOuterPoint, decOuter[1], centerPoint);//中点与小数部分内外侧线的交点，这两点连线便是小数区对应的小数值
        if (!isInBorder(decInnerIntersection, bitmap) || !isInBorder(decOuterIntersection, bitmap)) {
            notify(null);
            return;
        }
        //解码小数部分
        decPart = decodeBaseRed.getDecimalPart(bitmap, decInnerIntersection, decOuterIntersection, recognition);
        if (decPart == null)
            decPart = decodeBaseRGB.getDecimalPart(bitmap, decInnerIntersection, decOuterIntersection, recognition);
        if (decPart == null) {
            notify(null);
            return;
        }
        notify(intPart + "." + decPart);
    }

    /**
     * 通过线条上一点检查线条端点
     *
     * @param p 检查参考点
     * @return 检查到的所有端点
     */
    private Point[] checkEnds(Point p) {
        if (checkLine == null)
            checkLine = new CheckLine(bitmap, p, recognition);
        else
            checkLine.reset(bitmap, p, recognition);
        return checkLine.getEnds();
    }

    /**
     * 对输入的点的数目大于2时进行排序，否则不进行排序
     *
     * @param points 要排序的点
     * @return 排好序地点
     */
    private Point[] cleanSurplusPoint(Point[] points) {
        if (points == null) return null;
        if (points.length < 2) return null;
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].x > points[j].x) {
                    int temp = points[i].x;
                    points[i].x = points[j].x;
                    points[j].x = temp;

                    temp = points[i].y;
                    points[i].y = points[j].y;
                    points[j].y = temp;
                }
            }
        }
        Point start = points[0];
        Point end = points[points.length - 1];
        points = new Point[2];
        points[0] = start;
        points[1] = end;
        return points;
    }

    /**
     * 过p1线段作中垂线与线段2交于一点，取p1中点与该店的中点作为两线段的中点并返回
     *
     * @param p1 要作中垂线的线段
     * @param p2 与中垂线相交的线段
     * @return 中心点
     */
    private Point requestLinesCenterPoint(Point[] p1, Point[] p2) {
        if (p1.length != 2 || p2.length != 2) return null;
        Point p1CenterPoint = new Point((p1[0].x + p1[1].x) / 2, (p1[0].y + p1[1].y) / 2);
        Point p2Intersection;
        if (p1[1].y - p1[0].y == 0) {
            double k2 = (p2[0].y * 1.0 - p2[1].y * 1.0) / (p2[0].x * 1.0 - p2[1].x * 1.0);
            double b2 = p2[0].y * 1.0 - k2 * p2[0].x * 1.0;
            p2Intersection = new Point(p1CenterPoint.x, (int) (k2 * p1CenterPoint.x + b2));
        } else {
            double k1 = (p1[0].x * 1.0 - p1[1].x * 1.0) / (p1[1].y * 1.0 - p1[0].y * 1.0);
            double b1 = p1CenterPoint.y - k1 * p1CenterPoint.x;
            p2Intersection = requestIntersection(p2, k1, b1);
        }
        p1CenterPoint.x = (p1CenterPoint.x + p2Intersection.x) / 2;
        p1CenterPoint.y = (p1CenterPoint.y + p2Intersection.y) / 2;
        return p1CenterPoint;
    }

    /**
     * 通过一条直线上两点 另一条直线参数来求直线交点
     *
     * @param p1 第一条直线上两点
     * @param k2 第二条直线斜率
     * @param b2 第二条直线截距
     * @return 直线的交点
     */
    private Point requestIntersection(Point[] p1, double k2, double b2) {
        if (p1.length != 2) return null;
        if (p1[0].x == p1[1].x) {
            return new Point(p1[0].x, (int) (k2 * p1[0].x + b2));
        }
        double k1 = (p1[0].y * 1.0 - p1[1].y * 1.0) / (p1[0].x * 1.0 - p1[1].x * 1.0);
        double b1 = p1[0].y * 1.0 - k1 * p1[0].x * 1.0;
        return requestIntersection(k1, b1, k2, b2);
    }

    /**
     * 求两条直线的交点
     *
     * @param k1 第一条直线斜率
     * @param b1 第一条直线截距
     * @param k2 第二条直线斜率
     * @param b2 第二条直线截距
     * @return 交点
     */
    private Point requestIntersection(double k1, double b1, double k2, double b2) {
        double x = (b2 - b1) / (k1 - k2);
        double y = k1 * x + b1;
        return new Point((int) x, (int) y);
    }

    private Point requestIntersection(Point lp1, Point lp2, Point lp3, Point point) {
        double k = (lp1.x - lp3.x) * 1.0 / (lp3.y - lp1.y) * 1.0;
        double b = point.y - k * point.x;
        return requestIntersection(lp1, lp2, lp3, k, b);
    }

    /**
     * 利用线上三点拟合曲线并求与y=kx+b交点
     *
     * @param lp1 线上一点
     * @param lp2 线上一点
     * @param lp3 线上一点
     * @param k   直线斜率
     * @param b   直线截距
     * @return 交点
     */
    private Point requestIntersection(Point lp1, Point lp2, Point lp3, double k, double b) {
        Point O = requestCircleCenter(lp1, lp2, lp3);
        if (O == null) {
            return requestIntersection(new Point[]{lp1, lp3}, k, b);
        }

        double r = getDistance(lp2, O);

        double a0 = 1 + k * k;
        double b0 = 2.0 * (k * b - k * O.y - O.x);
        double c0 = 1.0 * O.x * O.x + (b - O.y * 1.0) * (b - O.y * 1.0) - r * r;
        double delta = b0 * b0 - 4 * a0 * c0;
        if (delta < 0) return null;
        delta = Math.sqrt(delta);
        double x1 = (-b0 + delta) / (2 * a0);
        double x2 = (-b0 - delta) / (2 * a0);
        Point p1 = new Point((int) x1, (int) (k * x1 + b));
        Point p2 = new Point((int) x2, (int) (k * x2 + b));
        if (getDistance(p1, lp1) + getDistance(p1, lp2) + getDistance(p1, lp3) > getDistance(p2, lp1) + getDistance(p2, lp2) + getDistance(p2, lp3)) {
            return p2;
        } else {
            return p1;
        }
    }

    /**
     * 求圆心，若三点共线返回空
     *
     * @param p0
     * @param p1
     * @param p2
     * @return
     */
    private Point requestCircleCenter(Point p0, Point p1, Point p2) {
        double k1 = (p1.x - p0.x) * 1.0 / (p0.y - p1.y) * 1.0;
        double b1 = (p1.y + p0.y) / 2.0 - k1 * (p1.x + p0.x) / 2.0;

        double k2 = (p2.x - p1.x) * 1.0 / (p1.y - p2.y) * 1.0;
        double b2 = (p1.y + p2.y) / 2.0 - k2 * (p1.x + p2.x) / 2.0;
        if (k1 == k2) return null;
        return requestIntersection(k1, b1, k2, b2);
    }

    /**
     * 求两点距离
     *
     * @param p1
     * @param p2
     * @return
     */
    private double getDistance(Point p1, Point p2) {
        double t = (p2.x * 1.0 - p1.x) * (p2.x * 1.0 - p1.x) + (p2.y * 1.0 - p1.y) * (p2.y * 1.0 - p1.y);
        return Math.sqrt(t);
    }

    private boolean isInBorder(Point point, Bitmap bitmap) {
        if (point.x < 0 || point.x > bitmap.getWidth() - 1 || point.y < 0 || point.y > bitmap.getHeight() - 1)
            return false;
        return true;

    }

    /**
     * 当解析结束时通知前台
     *
     * @param data
     */
    private void notify(String data) {
        if (listener != null) listener.onParseFinish(data);
    }
}
