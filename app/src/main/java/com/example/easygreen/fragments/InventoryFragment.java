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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {
    private Toolbar toolbar;
    private InventoryAdapter inventoryAdapter;
    private List<Ingredient> ingredients = new ArrayList<>();
    private RecyclerView rvInventory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        displayToolbar(view);
        displayRecyclerView(view, this.ingredients);
        getIngredients(ingredients);

        return view;
    }

    private void getIngredients(List<Ingredient> list) {
        ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.findInBackground(new FindCallback<Ingredient>() {
            @Override
            public void done(List<Ingredient> items, ParseException e) {
                if(e == null){
                    ingredients.addAll(items);
                    inventoryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void displayRecyclerView(View view, List<Ingredient> list) {
        rvInventory = view.findViewById(R.id.rvInventory);
        inventoryAdapter = new InventoryAdapter(list);
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void displayToolbar(View view) {
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