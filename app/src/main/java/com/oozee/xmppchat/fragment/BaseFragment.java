package com.oozee.xmppchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.oozee.xmppchat.ui.MainActivity;

/**
 * Created by Pc-Android-1 on 8/30/2016.
 */
public class BaseFragment extends Fragment {

    public MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
    }
}
