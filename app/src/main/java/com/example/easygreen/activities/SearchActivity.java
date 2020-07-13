package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easygreen.R;
import com.example.easygreen.activities.groups.DairyActivity;
import com.example.easygreen.activities.groups.SeafoodActivity;
import com.example.easygreen.activities.groups.FruitActivity;
import com.example.easygreen.activities.groups.GrainActivity;
import com.example.easygreen.activities.groups.MeatActivity;
import com.example.easygreen.activities.groups.VegetableActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void dairySelect(View view) {
        Intent i = new Intent(this, DairyActivity.class);
        startActivity(i);
    }

    public void vegetableSelect(View view) {
        Intent i = new Intent(this, VegetableActivity.class);
        startActivity(i);
    }

    public void fruitSelect(View view) {
        Intent i = new Intent(this, FruitActivity.class);
        startActivity(i);
    }

    public void grainSelect(View view) {
        Intent i = new Intent(this, GrainActivity.class);
        startActivity(i);
    }

    public void meatSelect(View view) {
        Intent i = new Intent(this, MeatActivity.class);
        startActivity(i);
    }

    public void seafoodSelect(View view) {
        Intent i = new Intent(this, SeafoodActivity.class);
        startActivity(i);
    }
}