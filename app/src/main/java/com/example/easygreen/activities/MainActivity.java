package com.example.easygreen.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.easygreen.R;
import com.example.easygreen.fragments.AccountFragment;
import com.example.easygreen.fragments.DiscoverFragment;
import com.example.easygreen.fragments.InventoryFragment;
import com.example.easygreen.fragments.RecipeFragment;
import com.example.easygreen.fragments.ShoppingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    /***** Fragment References **************/
    final Fragment inventoryFragment = new InventoryFragment();
    final Fragment recipeFragment = new RecipeFragment();
    final Fragment shoppingListFragment = new ShoppingListFragment();
    final Fragment discoverFragment = new DiscoverFragment();
    final Fragment accountFragment = new AccountFragment();

    /**** Local Variables ***********/
    public String inventory = "";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = recipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        /**** If true, then change icon selected to Recipes *********************/
        if(passInventory()){
            bottomNavigationView.setSelectedItemId(R.id.action_recipes);
        }

        /********** Create a stack of fragments and hide them *******************/
        fragmentManager.beginTransaction().add(R.id.flContainer, accountFragment, "5").hide(accountFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, discoverFragment, "4").hide(discoverFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, shoppingListFragment, "3").hide(shoppingListFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flContainer, inventoryFragment, "2").hide(inventoryFragment).commit();

        /********** Initial fragment is shown *******************/
        bottomNavigationView.setSelectedItemId(R.id.action_recipes);
        fragmentManager.beginTransaction().add(R.id.flContainer, recipeFragment, "1").commit();
    }

    /***** User clicks a icon on the Bottom Navigation, display a certain fragment **************/
    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             switch (item.getItemId()) {
                 case R.id.action_inventory:
                     fragmentManager.beginTransaction().hide(active).show(inventoryFragment).commit();
                     active = inventoryFragment;
                     return true;

                 case R.id.action_recipes:
                     fragmentManager.beginTransaction().hide(active).show(recipeFragment).commit();
                     active = recipeFragment;
                     return true;

                 case R.id.action_shopping_list:
                     fragmentManager.beginTransaction().hide(active).show(shoppingListFragment).commit();
                     active = shoppingListFragment;
                     return true;

                 case R.id.action_discover:
                     fragmentManager.beginTransaction().hide(active).show(discoverFragment).commit();
                     active = discoverFragment;
                     return true;

                 case R.id.action_account:
                     fragmentManager.beginTransaction().hide(active).show(accountFragment).commit();
                     active = accountFragment;
                     return true;
             }
             return false;
         }
     };

    /****** Get inventory and pass it to RecipeFragment ********/
    public boolean passInventory(){
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
}