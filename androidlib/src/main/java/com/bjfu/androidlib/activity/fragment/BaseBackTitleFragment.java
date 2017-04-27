package com.bjfu.androidlib.activity.fragment;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by 11827 on 2017/3/2.
 */

public abstract class BaseBackTitleFragment extends BaseFragment {
    protected RelativeLayout rlActionBar;
    protected ImageButton ibBack;
    protected TextView tvTitle;

    protected ImageView ivLoading;
    protected TextView tvLoading;
    protected LinearLayout llLoading;
    private RotateAnimation animation;

    protected void switchActionBarVisible() {
        if (rlActionBar.getVisibility() == View.VISIBLE) {
            rlActionBar.setVisibility(View.INVISIBLE);
        } else {
            rlActionBar.setVisibility(View.VISIBLE);
        }
    }

    protected void showPinkLoading(boolean load) {
        if (!load) {
            if (animation != null) animation.cancel();
            llLoading.setVisibility(View.INVISIBLE);
        } else {
            llLoading.setVisibility(View.VISIBLE);
            if (animation == null) {
                animation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setDuration(1000);
                animation.setRepeatMode(Animation.RESTART);
            }
            ivLoading.startAnimation(animation);
        }
    }
}
