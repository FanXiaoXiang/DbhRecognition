package com.bjfu.androidlib.activity;

import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bjfu.androidlib.R;

import java.util.ArrayList;
import java.util.List;

public abstract class CheckPermissionsActivity extends BaseActivity {

    private List<String> permissions;
    private List<String> unGrantedPermissions;

    private ImageView ivCheckingLoading;
    private ImageView ivBackground;
    private AnimationDrawable loading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 23) {
            startActivity();
            return;
        }
    }


    protected void initVariables() {
        permissions = new ArrayList<String>();
    }

    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_check_permissions);
        ivCheckingLoading = (ImageView) findViewById(R.id.ivCheckingLoading);
        ivCheckingLoading.setBackgroundResource(getLoadingRes());
        showLoading(true);

        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        ivBackground.setBackgroundResource(getBackgroundRes());
    }

    protected void loadData() {
        registerPermissions(permissions);
        unGrantedPermissions = checkUnGrantedPermissions(permissions);
        if (unGrantedPermissions == null || unGrantedPermissions.size() <= 0)
            startActivity();
        else
            requestPermission();
    }

    /**
     * show {@link #ivCheckingLoading} animation drawable
     *
     * @param show
     */
    private void showLoading(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    ivCheckingLoading.setVisibility(View.VISIBLE);
                    loading = (AnimationDrawable) ivCheckingLoading.getBackground();
                    loading.start();
                    return;
                }
                ivCheckingLoading.setVisibility(View.INVISIBLE);
                if (loading == null) return;
                loading.stop();
                loading = null;
            }
        });
    }

    /**
     * register all permissions
     *
     * @param permissions
     */
    protected abstract void registerPermissions(List<String> permissions);

    /**
     * check not granted permissions
     *
     * @param permissions
     * @return
     */
    private List<String> checkUnGrantedPermissions(List<String> permissions) {
        List<String> unGrantedPermissions = new ArrayList<String>();
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissions.add(p);
            }
        }
        return unGrantedPermissions;
    }

    /**
     * request a permission
     *
     * @param permission
     * @param questCode  set it to {@link #unGrantedPermissions} index
     */
    private void requestPermission(String permission, int questCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, questCode);
    }

    /**
     * request un granted permission in {@link #unGrantedPermissions}
     */
    private void requestPermission() {
        requestPermission(unGrantedPermissions.get(0), 0);
    }

    /**
     * request result callback method
     *
     * @param requestCode  the {@link #unGrantedPermissions} index
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            unGrantedPermissions.remove(requestCode);
        }

        if (unGrantedPermissions.size() == 0) {
            showLoading(false);
            startActivity();
        } else {
            requestPermission();
        }
    }

    /**
     * 设置载入动画资源
     *
     * @return
     */
    protected abstract int getLoadingRes();

    protected abstract int getBackgroundRes();

    protected abstract void startActivity();
}
