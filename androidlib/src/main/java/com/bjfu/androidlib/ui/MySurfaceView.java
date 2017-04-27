package com.bjfu.androidlib.ui;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by 11827 on 2016/8/1.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Activity context;
    private Camera camera;
    private SurfaceHolder holder;

    public MySurfaceView(Activity context, Camera camera) {
        super(context);
        this.context = context;
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) return;
        try {
            camera.stopPreview();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
