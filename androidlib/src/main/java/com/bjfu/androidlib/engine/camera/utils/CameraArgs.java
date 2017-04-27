package com.bjfu.androidlib.engine.camera.utils;

import android.hardware.Camera;

import java.util.List;

/**
 * Created by 11827 on 2016/8/1.
 */
public class CameraArgs {
    //修正值
    private float FOCAL_LENGTH_REVISE_VALUE = .0F;
    private float HORIZONTAL_ANGLE_REVISE_VALUE = 12.1664F;
    private float VERTICAL_ANGLE_REVISE_VALUE = 12.59112f;
    //SurfaceView视场角修正
    private float SURFACE_VERTICAL_ANGLE_REVISE_VALUE = -1.1569F;

    //常量值
    private float FOCAL_LENGTH_CONSTANT = .0F;
    private float HORIZONTAL_ANGLE_CONSTANT = .0F;
    private float VERTICAL_ANGLE_CONSTANT = .0F;

    private float focalLength;//焦距
    private float horizontalAngle;//水平角
    private float verticalAngle;//水平角
    private float surfaceVerticalAngle;//水平角
    private float photoWidth;//相幅宽
    private float photoHeight;//相幅高
    private int maxZoom;//最大焦距
    private List<Integer> zoomRatios;//缩放率

    public CameraArgs(Camera camera) {
        if (camera == null) return;
        Camera.Parameters parameters = camera.getParameters();
        FOCAL_LENGTH_CONSTANT = parameters.getFocalLength();
        HORIZONTAL_ANGLE_CONSTANT = parameters.getHorizontalViewAngle();
        VERTICAL_ANGLE_CONSTANT = parameters.getVerticalViewAngle();

        focalLength = FOCAL_LENGTH_CONSTANT + FOCAL_LENGTH_REVISE_VALUE;
        horizontalAngle = HORIZONTAL_ANGLE_CONSTANT + HORIZONTAL_ANGLE_REVISE_VALUE;
        verticalAngle = VERTICAL_ANGLE_CONSTANT + VERTICAL_ANGLE_REVISE_VALUE;

        surfaceVerticalAngle = VERTICAL_ANGLE_CONSTANT + SURFACE_VERTICAL_ANGLE_REVISE_VALUE;

        maxZoom = parameters.getMaxZoom();
        Camera.Size pictureSize = parameters.getPictureSize();
        zoomRatios = parameters.getZoomRatios();
        photoWidth = pictureSize.width;
        photoHeight = pictureSize.height;
    }

    public float getFocalLength() {
        return focalLength;
    }

    public float getHorizontalAngle() {
        return horizontalAngle;
    }

    public float getVerticalAngle() {
        return verticalAngle;
    }

    public float getPhotoWidth() {
        return photoWidth;
    }

    public float getPhotoHeight() {
        return photoHeight;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public List<Integer> getZoomRatios() {
        return zoomRatios;
    }

    public float getSurfaceVerticalAngle() {
        return surfaceVerticalAngle;
    }

    public void setFocalLengthReviseValue(float focalLengthReviseValue) {
        this.FOCAL_LENGTH_REVISE_VALUE = focalLengthReviseValue;
        focalLength = FOCAL_LENGTH_CONSTANT + FOCAL_LENGTH_REVISE_VALUE;
    }

    public void setHorizontalAngleReviseValue(float horizonalAngleReviseValue) {
        this.HORIZONTAL_ANGLE_REVISE_VALUE = horizonalAngleReviseValue;
        horizontalAngle = HORIZONTAL_ANGLE_CONSTANT + HORIZONTAL_ANGLE_REVISE_VALUE;
    }

    public void setVerticalAngleReviseValue(float verticalAngleReviseValue) {
        this.VERTICAL_ANGLE_REVISE_VALUE = verticalAngleReviseValue;
        verticalAngle = VERTICAL_ANGLE_CONSTANT + VERTICAL_ANGLE_REVISE_VALUE;
    }

    public void setSurfaceVerticalAngleReviseValue(float surfaceVerticalAngleReviseValue) {
        this.SURFACE_VERTICAL_ANGLE_REVISE_VALUE = surfaceVerticalAngleReviseValue;
        surfaceVerticalAngle = VERTICAL_ANGLE_CONSTANT + SURFACE_VERTICAL_ANGLE_REVISE_VALUE;
    }

    public void addFocalLengthReviseValue(float add) {
        setFocalLengthReviseValue(FOCAL_LENGTH_REVISE_VALUE + add);
    }

    public void addHorizontalAngleReviseValue(float add) {
        setHorizontalAngleReviseValue(HORIZONTAL_ANGLE_REVISE_VALUE + add);
    }

    public void addVerticalAngleReviseValue(float add) {
        setVerticalAngleReviseValue(VERTICAL_ANGLE_REVISE_VALUE + add);
    }

    @Override
    public String toString() {
        return "CameraArgs{" +
                "focalLength=" + focalLength +
                ", horizontalAngle=" + horizontalAngle +
                ", verticalAngle=" + verticalAngle +
                ", photoWidth=" + photoWidth +
                ", photoHeight=" + photoHeight +
                ", maxZoom=" + maxZoom +
                ", zoomRatios=" + zoomRatios +
                '}';
    }
}
