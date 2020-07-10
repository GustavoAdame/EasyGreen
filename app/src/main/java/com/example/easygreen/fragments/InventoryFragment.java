package com.example.easygreen.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easygreen.R;
import com.example.easygreen.adapters.InventoryAdapter;
import com.example.easygreen.models.Group;
import com.example.easygreen.models.Ingredient;
import com.example.easygreen.models.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {
    private Toolbar toolbar;
    private InventoryAdapter inventoryAdapter;
    private List<Ingredient> ingredients;
    private RecyclerView rvInventory;

    public InventoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        ingredients = new ArrayList<>();

        /* Sample Data */
        Group dairy = new Group();
        dairy.setName("Dairy");

        Ingredient cheese = new Ingredient();
        cheese.setName("Cheese");
        cheese.setGroup(dairy);

        Ingredient milk = new Ingredient();
        milk.setName("Milk");
        milk.setGroup(dairy);

        ingredients.add(milk);
        ingredients.add(cheese);

        dairy.setIngredients(ingredients);

        /* Current Dairy Ingredients */
        List<Ingredient> dairyIngredients = dairy.getIngredients();


        callToolbar(view);
        callRecyclerView(view, this.ingredients);
        return view;
    }

    private void callRecyclerView(View view, List<Ingredient> list) {
        rvInventory = view.findViewById(R.id.rvInventory);
        inventoryAdapter = new InventoryAdapter(list);
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void callToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.inventory_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search){
                    Toast.makeText(getContext(), "CLICKED ON SEARCH", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inventory_menu, menu);
    }

}