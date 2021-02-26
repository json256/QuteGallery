package com.illusory.qutegallery.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.illusory.qutegallery.R;
import com.illusory.qutegallery.viewmodel.ThumbViewModel;

public class ThumbFragment extends Fragment {

    private ThumbViewModel viewModel;

    public static ThumbFragment newInstance() {
        return new ThumbFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.thumb_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ThumbViewModel.class);
    }

}