package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.easygreen.R;
import com.example.easygreen.fragments.DiscoverFragment;
import com.example.easygreen.fragments.InventoryFragment;
import com.example.easygreen.fragments.RecipeFragment;
import com.example.easygreen.fragments.ShoppingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    public String inventory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_inventory){
                    currentFragment = new InventoryFragment();

                } else if (item.getItemId() == R.id.action_recipes){
                    currentFragment = new RecipeFragment();

                    Intent i = getIntent();
                    Bundle bundle = getIntent().getExtras();
                    if(bundle != null){
                        inventory = bundle.getString("inventory");
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("key", inventory);
                        currentFragment.setArguments(bundle2);
                    }

                } else if (item.getItemId() == R.id.action_shopping_list){
                    currentFragment = new ShoppingListFragment();

                } else if (item.getItemId() == R.id.action_discover){
                    currentFragment = new DiscoverFragment();
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, currentFragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_inventory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_recipes);
    }
}