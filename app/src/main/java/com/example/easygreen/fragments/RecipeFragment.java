package com.example.easygreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.easygreen.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class RecipeFragment extends Fragment {
    private ChipGroup cgFilterContainer;
    private Chip chip1, chip2, chip3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        inflateViews(view);
        return view;
    }

    private void inflateViews(View view) {
        cgFilterContainer = view.findViewById(R.id.cgFilterContainer);
        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);
    }
}