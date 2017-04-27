package com.bjfu.androidlib.engine.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

import com.bjfu.androidlib.engine.camera.utils.CameraArgs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 11827 on 2016/8/1.
 */
public class CameraManager implements Camera.PictureCallback {
    private static String Tag = "CameraManager";
    private static volatile CameraManager manager;
    private OnPhotoSaveStateListener stateListener;

    private File file;
    private Camera c;//相机对象
    private CameraArgs cameraArgs;

    private boolean savePicture;

    private CameraManager() {
        getCamera();
    }

    public static CameraManager getInstance() {
        if (manager == null) {
            synchronized (CameraManager.class) {
                if (manager == null) {
                    manager = new CameraManager();
                }
            }
        }
        return manager;
    }

    /**
     * 获取相机对象
     *
     * @return
     */
    public Camera getCamera() {
        if (c != null) return c;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            c.setDisplayOrientation(90);
            Camera.Parameters parameters = c.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            c.setParameters(parameters);
            cameraArgs = new CameraArgs(c);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(Tag, "Camera is not available (in use or does not exist)");
            return null;
        }
        return c; // returns null if camera is unavailable
    }

    public void releaseCamera() {
        try {
            if (c != null)
                c.release();
            c = null;
        } catch (Exception e) {

        }
    }

    /**
     * Check if this device has a camera
     */
    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void takePicture(String path, String name, OnPhotoSaveStateListener stateListener) {
        this.stateListener = stateListener;
        savePicture = true;
        File _path = new File(path);
        if (!_path.exists()) _path.mkdirs();
        File _name = new File(_path, name);
        if (!_name.exists()) try {
            _name.createNewFile();
            file = _name;
        } catch (IOException e) {
            Log.e(Tag, "照片存储路径创建异常");
            return;
        }
        getCamera().takePicture(null, null, this);
    }

    public void takePicture(OnPhotoSaveStateListener stateListener) {
        this.stateListener = stateListener;
        savePicture = false;
        getCamera().takePicture(null, null, this);
    }

    public CameraArgs getCameraArgs() {
        return cameraArgs;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        if (savePicture) {
            if (file == null) {
                if (stateListener != null) stateListener.hasSavePhoto(null);
                return;
            }
            savePhoto(data);
        } else
            stateListener.hasGetPictureData(data);
    }

    private void savePhoto(byte[] data) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            bos.flush();
        } catch (IOException e) {
            if (stateListener != null) stateListener.hasSavePhoto(null);
            file = null;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e) {
                if (stateListener != null) stateListener.hasSavePhoto(null);
                file = null;
            }
            if (stateListener != null && file != null)
                stateListener.hasSavePhoto(file.getAbsolutePath());
            file = null;
        }
    }

    public interface OnPhotoSaveStateListener {

        void hasSavePhoto(String path);

        void hasGetPictureData(byte[] data);
    }

}
