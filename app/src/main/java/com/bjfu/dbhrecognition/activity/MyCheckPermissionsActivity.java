package com.bjfu.dbhrecognition.activity;

import android.content.Intent;

import com.bjfu.androidlib.activity.CheckPermissionsActivity;
import com.bjfu.dbhrecognition.R;

import java.util.List;

/**
 * Created by 11827 on 2017/3/20.
 */

public class MyCheckPermissionsActivity extends CheckPermissionsActivity {
    @Override
    protected void registerPermissions(List<String> permissions) {
        String[] perm = getResources().getStringArray(R.array.Permissions);
        if (perm == null || perm.length <= 0) return;
        for (String p : perm) {
            permissions.add(p);
        }
    }

    @Override
    protected void onRestart() {
        
    }

    @Override
    protected int getLoadingRes() {
        return R.drawable.blue_loading;
    }

    @Override
    protected int getBackgroundRes() {
        return R.mipmap.forest;
    }

    @Override
    protected void startActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
