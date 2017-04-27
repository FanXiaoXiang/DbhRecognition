package com.bjfu.androidlib.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjfu.androidlib.R;

public abstract class BaseFragment extends Fragment {

    protected View root;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(getLayoutId(), container, false);
        initVariables();
        initView(savedInstanceState, root);
        loadData();
        return root;
    }

    protected abstract int getLayoutId();

    protected abstract void initVariables();

    protected abstract void initView(Bundle savedInstanceState, View root);

    protected abstract void loadData();
}
