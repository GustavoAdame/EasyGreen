package com.example.easygreen.activities;

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
    private Fragment inventoryFragment, recipeFragment;
    private Fragment shoppingListFragment, discoverFragment;
    public String inventory = "";
    public String itemAdded = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                inventoryFragment = new InventoryFragment();
                recipeFragment = new RecipeFragment();
                shoppingListFragment = new ShoppingListFragment();
                discoverFragment = new DiscoverFragment();

                if (item.getItemId() == R.id.action_inventory) {
                    getItem(inventoryFragment);
                    fragmentManager.beginTransaction().replace(R.id.flContainer, inventoryFragment).commit();
                } else if (item.getItemId() == R.id.action_recipes) {
                    passInventory(recipeFragment);
                    fragmentManager.beginTransaction().replace(R.id.flContainer, recipeFragment).commit();
                } else if (item.getItemId() == R.id.action_shopping_list) {
                    fragmentManager.beginTransaction().replace(R.id.flContainer, shoppingListFragment).commit();
                } else if (item.getItemId() == R.id.action_discover) {
                    fragmentManager.beginTransaction().replace(R.id.flContainer, discoverFragment).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_recipes);

        if(getItem(inventoryFragment)){
            bottomNavigationView.setSelectedItemId(R.id.action_inventory);
        } else if(passInventory(recipeFragment)){
            bottomNavigationView.setSelectedItemId(R.id.action_recipes);
        }
    }

    public boolean passInventory(Fragment recipeFragment){
        Bundle fromInventoryFragment = getIntent().getExtras();
        if(fromInventoryFragment != null){
            inventory = fromInventoryFragment.getString("inventory");
            if (inventory != null) {
                Bundle toRecipeFragment = new Bundle();
                toRecipeFragment.putString("inventory", inventory);
                recipeFragment.setArguments(toRecipeFragment);
                return true;
            }
        }
        return false;
    }

    public boolean getItem(Fragment inventoryFragment){
        Bundle fromGroupAdapter = getIntent().getExtras();
        if(fromGroupAdapter != null){
            itemAdded = fromGroupAdapter.getString("newItem");
            if (itemAdded != null) {
                Bundle toInventoryFragment = new Bundle();
                toInventoryFragment.putString("newItem", itemAdded);
                inventoryFragment.setArguments(toInventoryFragment);
                return true;
            }
        }
        return false;
    }

}