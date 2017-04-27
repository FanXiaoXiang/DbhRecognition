package com.bjfu.dbhrecognition.activity.controller;

import android.hardware.Camera;

import com.bjfu.androidlib.engine.camera.CameraManager;
import com.bjfu.dbhrecognition.activity.fragment.MainFragment;
import com.bjfu.dbhrecognition.activity.mode.MainMode;
import com.bjfu.dbhrecognition.listener.OnParseFinishListener;
import com.bjfu.dbhrecognition.utils.State;

import java.util.List;

/**
 * Created by 11827 on 2017/3/20.
 */

public class MainController implements CameraManager.OnPhotoSaveStateListener, OnParseFinishListener {
    private MainFragment fragment;
    private MainMode mode;

    private CameraManager cameraManager;
    private Camera camera;

    private boolean isCapturing = false;

    public MainController(MainFragment fragment) {
        this.fragment = fragment;
        mode = new MainMode(fragment.getActivity());
        cameraManager = CameraManager.getInstance();
        if ((camera = cameraManager.getCamera()) == null) return;
        Camera.Parameters parameters = cameraManager.getCamera().getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        parameters.setPictureSize(sizes.get(0).width, sizes.get(0).height);
        cameraManager.getCamera().setParameters(parameters);
    }

    public void capture() {
        isCapturing = true;
        cameraManager.takePicture(this);
    }

    public void cancelCapture() {
        isCapturing = false;
    }

    public Camera getCamera() {
        return camera;
    }

    public void releaseCamera() {
        cameraManager.releaseCamera();
        camera = null;
    }

    /**
     * 打开闪光灯
     *
     * @return
     */
    public Camera onLightFlash() {
        if (camera == null) return null;
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        return camera;
    }

    /**
     * 关闭闪光灯
     *
     * @return
     */
    public Camera offLightFlash() {
        if (camera == null) return null;
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        return camera;
    }

    public String getLightFlashState() {
        if (camera == null) return null;
        return camera.getParameters().getFlashMode();
    }

    /**
     * 若解析正确则显示结果
     * 若解析错误则根据{@link #isCapturing}进行再次扫描或停止扫描
     *
     * @param result 解析结果
     */
    @Override
    public void onParseFinish(String result) {
        if (result != null) {
            fragment.finishRecognize(result);
        } else if (isCapturing) {
            capture();
        } else {
            fragment.finishRecognize(null);
        }
    }

    @Override
    public void hasSavePhoto(String path) {
    }

    @Override
    public void hasGetPictureData(final byte[] data) {
        if (camera != null) camera.startPreview();
        if (data == null) {
            fragment.finishRecognize(null);
            return;
        }
        if (mode != null) mode.parsePic(data, this);
    }

}
