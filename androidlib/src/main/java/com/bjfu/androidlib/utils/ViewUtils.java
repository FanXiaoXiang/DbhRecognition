package com.bjfu.androidlib.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 11827 on 2017/2/17.
 */

public class ViewUtils {
    public static String round(double db, int decimal) {
        if (decimal < 0)
            throw new RuntimeException("double round exception , because the decimal is negative.");
        //将double转换为String
        StringBuffer temp = new StringBuffer();
        temp.append("" + db);
        int dotIndex = -1;
        int len = temp.length();
        for (int i = 0; i < len; i++) {
            if (temp.charAt(i) == '.') {
                dotIndex = i;
                break;
            }
        }
        //若无小数点则添加小数点
        if (dotIndex < 0) temp.append(".");
        //补0
        for (int i = 0; i < decimal; i++) {
            temp.append('0');
        }

        //将String转化为字符数组
        char[] arr = temp.toString().toCharArray();
        //若要求的小数最后一位下一位>='5'，要进位
        if (arr[dotIndex + decimal + 1] > '4') {
            arr[dotIndex + decimal] += 1;
            for (int i = dotIndex + decimal; i > 0; i--) {
                if (arr[i] == '.') continue;
                if (arr[i] > '9') {
                    arr[i] = '0';
                    if (arr[i - 1] == '.')
                        arr[i - 2] += 1;
                    else
                        arr[i - 1] += 1;
                } else
                    break;
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dotIndex + decimal + 1; i++) {
            if (i == 0 && arr[i] > '9') {
                sb.append("10");
            } else
                sb.append(arr[i]);
        }
        return sb.toString();
    }

    public static final double getDouble(TextView tv, double defaultValue) {
        String str = tv.getText().toString();
        return convertToDouble(str, defaultValue);
    }

    public static final double getDouble(TextView tv, int decimal, double defaultValue) {
        String str = "0" + tv.getText().toString();
        double db = convertToDouble(str, defaultValue);
        return convertToDouble(ViewUtils.round(db, decimal), defaultValue);
    }

    public static final int getInt(TextView tv, int defaultValue) {
        String str = "0" + tv.getText().toString();
        return convertToInt(str, defaultValue);
    }

    public static final String convertToString(Object obj, String defaultValue) {
        if (obj == null || "".equals(obj.toString().trim()))
            return defaultValue;
        return obj.toString();
    }

    public static final int convertToInt(Object obj, int defaultValue) {
        if (obj == null || "".equals(obj.toString().trim()))
            return defaultValue;
        try {
            return Integer.valueOf(obj.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(obj.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public static final long convertToLong(Object obj, long defaultValue) {
        if (obj == null || "".equals(obj.toString().trim()))
            return defaultValue;
        try {
            return Long.valueOf(obj.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(obj.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public static final double convertToDouble(Object obj, double defaultValue) {
        if (obj == null || "".equals(obj.toString().trim()))
            return defaultValue;
        try {
            return Double.valueOf(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static final float convertToFloat(Object obj, float defaultValue) {
        if (obj == null || "".equals(obj.toString().trim()))
            return defaultValue;
        try {
            return Float.valueOf(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static final void showMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static final void showMessage(Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
