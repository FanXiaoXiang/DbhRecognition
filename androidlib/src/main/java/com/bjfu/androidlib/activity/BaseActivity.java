package com.bjfu.androidlib.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bjfu.androidlib.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView(savedInstanceState);
        loadData();
    }

    protected abstract void initVariables();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void loadData();
}
