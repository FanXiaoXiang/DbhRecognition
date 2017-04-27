package com.bjfu.androidlib.engine.location.coordinate_convert.convert;

import com.bjfu.androidlib.engine.location.coordinate_convert.LongitudeCoordinate;
import com.bjfu.androidlib.engine.location.coordinate_convert.RectangularCoordinate;
import com.bjfu.androidlib.engine.location.coordinate_convert.convert.utils.Zoning;
import com.bjfu.androidlib.engine.location.coordinate_convert.ellipsoids.utils.Ellipsoid;

/**
 * Created by 11827 on 2017/2/16.
 */

public class ForwardConvert {
    private Ellipsoid ellipsoid;
    private Parameters p;

    public ForwardConvert(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
        p = new Parameters(ellipsoid);
    }

    public void resetEllipsoid(Ellipsoid ellipsoid) {
        if (this.ellipsoid == ellipsoid) return;
        this.ellipsoid = ellipsoid;
        p.initParameter(ellipsoid);
    }

    public RectangularCoordinate convert(LongitudeCoordinate lc, Zoning zoning) {
        double B = lc.getLatitude() / 180 * Math.PI;//纬度
        double L = lc.getLongitude() / 180 * Math.PI;//经度

        double z = 0.0;//分带号
        double L0 = 0.0;//中央经线

        switch (zoning) {
            case DEG_3:
                z = Math.floor(lc.getLongitude() / 3 + 0.5);
                L0 = z * 3 * Math.PI / 180;
                break;
            case DEG_6:
                z = 6 * Math.floor((lc.getLongitude() + 3) / 6 + 0.5);
                L0 = z * 6 * Math.PI / 180;
                break;
        }

        double lii = L - L0;//经度差
        double sinB = Math.sin(B);
        double cosB = Math.cos(B);

        double N = p.c / Math.sqrt(1 + p.ei * p.ei * cosB * cosB);
        double t = Math.tan(B);
        double nSinCosB = N * sinB * cosB;
        double ita = p.ei * cosB;
        double nCosB = N * cosB;
        double _2SinCosB = sinB * cosB * 2;
        double _4SinCosB = _2SinCosB * (1 - 2 * sinB * sinB) * 2;
        double _6SinCosB = _2SinCosB * Math.sqrt(1 - _4SinCosB * _4SinCosB) + _4SinCosB * Math.sqrt(1 - _2SinCosB * _2SinCosB);

        double X = p.a0 * B - p.a2 / 2.0 * _2SinCosB + p.a4 * _4SinCosB / 4.0 - p.a6 / 6.0 * _6SinCosB;

        double x = nSinCosB * lii * lii / 2.0
                + nSinCosB * cosB * cosB * Math.pow(lii, 4) * (5 - t * t + 9 * ita * ita + 4 * Math.pow(ita, 4)) / 24.0
                + nSinCosB * Math.pow(cosB, 4) * Math.pow(lii, 6) * (61 - 58 * t * t + Math.pow(t, 4)) / 720.0
                + X;
        double y = nCosB * Math.pow(lii, 1) + nCosB * cosB * cosB * (1 - t * t + ita * ita) / 6.0 * Math.pow(lii, 3)
                + nCosB * Math.pow(lii, 5) * Math.pow(cosB, 4) * (5 - 18 * t * t
                + Math.pow(t, 4) + 14 * ita * ita - 58 * ita * ita * t * t) / 120.0;
        y += 500000;
        return new RectangularCoordinate(x, y, lc.getAltitude());
    }

    private class Parameters {

        public double a;//长半轴
        public double b;//短半轴
        public double f;//扁率
        public double fi;//扁率倒数
        public double e;//第一偏心率
        public double ei;//第二偏心率
        public double c;
        public double m0, m2, m4, m6, m8;
        public double a0, a2, a4, a6, a8;
        public double pii;

        public Parameters(Ellipsoid ellipsoid) {
            initParameter(ellipsoid);
        }

        public void initParameter(Ellipsoid ellipsoid) {
            a = ellipsoid.getLongAxle();
            System.out.println("长半轴 = " + a);
            f = ellipsoid.getF();
            System.out.println("扁率 = " + f);
            b = a * (1 - f);
            System.out.println("短半轴 = " + f);
            fi = 1 / f;
            System.out.println("扁率倒数 = " + fi);
            c = a * a / b;

            double flag = Math.sqrt(a * a - b * b);
            e = flag / a;
            ei = flag / b;
            System.out.println("第一偏心率 : " + e);
            System.out.println("第一偏心率平方 : " + e * e);
            System.out.println("第二偏心率 : " + ei);

            flag = e * e;
            m0 = a * (1 - flag);
            m2 = 3 / 2 * flag * m0;
            m4 = 5 * flag * m2;
            m6 = 7 / 6 * flag * m4;
            m8 = 9 / 8 * flag * m6;

            a0 = m0 + m2 / 2 + 3 / 8 * m4 + 5 / 16 * m6 + 35 / 128 * m8;
            a2 = m2 / 2 + m4 / 2 + 15 / 32 * m6 + 7 / 16 * m8;
            a4 = m4 / 8 + 3 / 16 * m6 + 7 / 32 * m8;
            a6 = m6 / 32 + m8 / 16;
            a8 = m8 / 128;
            pii = 180 / Math.PI * 3600;
        }
    }
}
