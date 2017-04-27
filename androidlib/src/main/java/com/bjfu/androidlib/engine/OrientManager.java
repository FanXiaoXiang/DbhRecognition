package com.bjfu.androidlib.engine;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by 11827 on 2017/2/16.
 */

public class OrientManager implements SensorEventListener {
    private static OrientManager manager = null;

    private static final int SAMPLES = 5;
    //回传方向的监听器
    private IDirectionListener mHorizontalListener, mVerticalListener;

    //传感器管理者
    private SensorManager sensorManager;
    //磁场传感器和加速度传感器
    private Sensor magSensor, accSensor;

    private Direction direction;
    private DeviceState state;

    //加速度传感器数据
    float[] accValues = new float[3];
    //地磁传感器数据
    float[] magValues = new float[3];
    float[] out_R = new float[16];
    //旋转矩阵，用来保存磁场和加速度的数据
    float r[] = new float[16];
    float I[] = new float[16];
    //模拟方向传感器的数据（原始数据为弧度）
    float values[] = new float[3];

    private int count = 0;
    private float[] azimuths = new float[SAMPLES];
    private float[] verticals = new float[SAMPLES];
    private float[] rolls = new float[SAMPLES];

    private OrientManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static OrientManager getInstance(Context context) {
        synchronized (OrientManager.class) {
            if (manager == null) {
                synchronized (OrientManager.class) {
                    manager = new OrientManager(context);
                }
            }
        }
        return manager;
    }

    /**
     * 注册回传方向的{@link IDirectionListener}，并注册传感器监听
     * 若{@link IDirectionListener}为空，则会注册失败
     *
     * @param mListener 方向监听接口
     */
    public void registerDirectionListener(DeviceState state, IDirectionListener mListener) {
        if (mListener == null) return;
        switch (this.state = state) {
            case HORIZONTAL:
                this.mHorizontalListener = mListener;
                this.mVerticalListener = null;
                break;
            case VERTICAL:
                this.mVerticalListener = mListener;
                this.mHorizontalListener = null;
                break;
        }
        if (accSensor != null && magSensor != null) {
            sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
            sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        }
    }

    public void unregisterDirectionListener() {
        if (mHorizontalListener == null && mVerticalListener == null) return;
        if (accSensor != null) sensorManager.unregisterListener(this, accSensor);
        if (magSensor != null) sensorManager.unregisterListener(this, magSensor);
    }

    /**
     * 处理传感器监听事件
     *
     * @param event 传感器事件
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            //方位磁场传感器事件时
            case Sensor.TYPE_MAGNETIC_FIELD:
                magValues = event.values;
                break;
            //当为加速度传感器事件时
            case Sensor.TYPE_ACCELEROMETER:
                accValues = event.values;
                break;
        }
        if (direction == null) direction = new Direction();
        switch (state) {
            case HORIZONTAL:
                calculateOrientBaseHorizontal();
                break;
            case VERTICAL:
                calculateOrientBaseVertical();
                break;
        }
    }

    /**
     * 当传感器精度发生改变时触发
     *
     * @param sensor   相应传感器
     * @param accuracy 传感器精度
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * 计算方位角
     */
    private void calculateOrientBaseVertical() {

        sensorManager.getRotationMatrix(r, I, accValues, magValues);
        sensorManager.remapCoordinateSystem(r, SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_X, out_R);
        values = sensorManager.getOrientation(out_R, values);
        float azimuth = values[0];
        azimuth = (float) Math.toDegrees(azimuth);
        while (azimuth < 0) {
            azimuth += 360;
        }

        while (azimuth > 360) {
            azimuth -= 360;
        }
        float vertical = values[1];
        vertical = (float) Math.toDegrees(vertical);
        float roll = values[2];
        roll = (float) Math.toDegrees(roll);

        if (count < SAMPLES) {
            azimuths[count] = azimuth;
            verticals[count] = vertical;
            rolls[count] = roll;
            count++;
        }
        if (count < SAMPLES) return;
        count = 0;
        azimuth = getAverage(azimuths);
        vertical = getAverage(verticals);
        roll = getAverage(rolls);

        direction.azimuth = azimuth;
        direction.vertical = vertical+90;
        direction.roll = roll;
        if (mVerticalListener != null) {
            mVerticalListener.onDirectionChange(direction);
        }
    }

    /**
     * 计算方位角
     */
    private void calculateOrientBaseHorizontal() {
        sensorManager.getRotationMatrix(out_R, null, accValues, magValues);
        values = sensorManager.getOrientation(out_R, values);
        float azimuth = values[0];
        azimuth = (float) Math.toDegrees(azimuth);
        while (azimuth < 0) {
            azimuth += 360;
        }

        while (azimuth > 360) {
            azimuth -= 360;
        }
        float vertical = values[1];
        vertical = (float) Math.toDegrees(vertical);
        float roll = values[2];
        roll = (float) Math.toDegrees(roll);

        if (count < SAMPLES) {
            azimuths[count] = azimuth;
            verticals[count] = vertical;
            rolls[count] = roll;
            count++;
        }
        if (count < SAMPLES) return;
        count = 0;
        azimuth = getAverage(azimuths);
        vertical = getAverage(verticals);
        roll = getAverage(rolls);

        direction.azimuth = azimuth;
        direction.vertical = vertical;
        direction.roll = roll;

        if (mHorizontalListener != null) {
            mHorizontalListener.onDirectionChange(direction);
        }
    }

    private float getAverage(float[] numbers) {
        double sum = 0;
        for (float f : numbers) {
            sum += f;
        }
        return (float) (sum / numbers.length);
    }

    public class Direction {
        public float azimuth;//方位角
        public float vertical;//垂直角
        public float roll;//翻滚角
    }

    public interface IDirectionListener {
        public void onDirectionChange(Direction direction);
    }

    public enum DeviceState {
        HORIZONTAL(0), VERTICAL(1);
        private int value;

        DeviceState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
