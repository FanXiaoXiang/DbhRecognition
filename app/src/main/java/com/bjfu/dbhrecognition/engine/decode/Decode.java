package com.bjfu.dbhrecognition.engine.decode;


import android.graphics.Bitmap;
import android.graphics.Point;

import com.bjfu.dbhrecognition.engine.color_recognition.ColorRecognition;
import com.bjfu.dbhrecognition.entity.Color;

/**
 * Created by 11827 on 2017/3/19.
 */

public abstract class Decode {
    public abstract String getIntegerPart(Bitmap bm, Point start, Point end, ColorRecognition recognition);

    public abstract String getDecimalPart(Bitmap bm, Point start, Point end, ColorRecognition recognition);
}
