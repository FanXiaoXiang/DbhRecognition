package com.bjfu.dbhrecognition.activity.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bjfu.androidlib.activity.fragment.BaseTitleFragment;
import com.bjfu.androidlib.ui.MySurfaceView;
import com.bjfu.dbhrecognition.R;
import com.bjfu.dbhrecognition.activity.MainActivity;
import com.bjfu.dbhrecognition.activity.controller.MainController;
import com.bjfu.dbhrecognition.inter_face.ICheckedPermissionCallBack;
import com.bjfu.dbhrecognition.listener.OnCheckedPermissionListener;


public class MainFragment extends BaseTitleFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private MySurfaceView mySurfaceView;
    private ImageView ivCenterLine;
    private FrameLayout flSurfaceView;
    private CheckBox cbFlash;
    private ImageButton ibCancelScan;

    private ImageButton ibStartScan;
    private MainController controller;
    private Animation alphaAnim;
    private ShowResultDialogFragment dialogFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initVariables() {
        controller = new MainController(this);
        alphaAnim = new AlphaAnimation(0, 1);
        alphaAnim.setRepeatCount(Animation.INFINITE);
        alphaAnim.setRepeatMode(Animation.REVERSE);
        alphaAnim.setDuration(500);
    }

    @Override
    protected void initView(Bundle savedInstanceState, View root) {
        ivCenterLine = (ImageView) root.findViewById(R.id.ivCenterLine);
        flSurfaceView = (FrameLayout) root.findViewById(R.id.flSurfaceView);
        cbFlash = (CheckBox) root.findViewById(R.id.cbFlash);
        ibCancelScan = (ImageButton) root.findViewById(R.id.ibCancelScan);
        ibStartScan = (ImageButton) root.findViewById(R.id.ibStartScan);

        cbFlash.setOnCheckedChangeListener(this);
        ibCancelScan.setOnClickListener(this);
        ibCancelScan.setVisibility(View.INVISIBLE);
        ibStartScan.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (controller.getCamera() != null) {
            mySurfaceView = new MySurfaceView(getActivity(), controller.getCamera());
            flSurfaceView.addView(mySurfaceView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (controller.getCamera() != null) {
            controller.getCamera().startPreview();
        }
    }

    @Override
    public void onPause() {
        if (controller.getCamera() != null) {
            controller.getCamera().stopPreview();
        }
        if (alphaAnim.hasStarted()) alphaAnim.cancel();
        super.onPause();
    }

    @Override
    public void onStop() {
        controller.releaseCamera();
        controller = null;
        super.onStop();
    }

    /**
     * 取消扫描
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibStartScan:
                if (!alphaAnim.hasStarted()) ivCenterLine.startAnimation(alphaAnim);
                ibStartScan.setVisibility(View.INVISIBLE);
                ibCancelScan.setVisibility(View.VISIBLE);
                controller.capture();
                break;
            case R.id.ibCancelScan:
                ibStartScan.setVisibility(View.VISIBLE);
                ibCancelScan.setVisibility(View.INVISIBLE);
                controller.cancelCapture();
                if (alphaAnim.hasStarted()) alphaAnim.cancel();
                break;
        }
    }

    /**
     * 闪光灯操作
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            controller.onLightFlash();
        } else
            controller.offLightFlash();
    }

    public void finishRecognize(final String result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onClick(ibCancelScan);
                if (dialogFragment == null) dialogFragment = new ShowResultDialogFragment();
                if (result == null)
                    dialogFragment.setMessage(getString(R.string.parse_error));
                else
                    dialogFragment.setMessage(result);
                dialogFragment.show(getActivity().getSupportFragmentManager(), null);
            }
        });

    }
}
