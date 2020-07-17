package com.example.easygreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.activities.MainActivity;
import com.example.easygreen.activities.SearchActivity;
import com.example.easygreen.adapters.InventoryAdapter;
import com.example.easygreen.models.Ingredient;
import com.example.easygreen.models.Inventory;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {
    private Toolbar toolbar;
    private InventoryAdapter inventoryAdapter;
    private List<Ingredient> ingredients = new ArrayList<>();
    private RecyclerView rvInventory;
    public String inventory_list = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        displayToolbar(view);
        getInventory();
        displayRecyclerView(view);
        updateInventory();

        Button btnGetRecipes = view.findViewById(R.id.btnGetRecipes);
        btnGetRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDatatoAPI();
            }
        });

        return view;
    }

    private void updateInventory() {
        Bundle fromMainActivity = this.getArguments();
        if(fromMainActivity != null){
            String prev = "";
            Ingredient item = new Ingredient();
            try{
                if(fromMainActivity.getString("newItem") != prev){
                        item.setName(fromMainActivity.getString("newItem"));
                        addInventory(item.getName());
                        ingredients.add(item);
                        inventoryAdapter.notifyDataSetChanged();
                        inventory_list += ", " + item.getName();
                        Toast.makeText(getContext(), item.getName() + " Added!", Toast.LENGTH_LONG).show();
                }
            } catch (IllegalArgumentException e){}
            prev = fromMainActivity.getString("newItem");
        }
    }

    private void sendDatatoAPI() {
        Bundle toMainActivity = new Bundle();
        toMainActivity.putString("inventory", inventory_list);
        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtras(toMainActivity);
        startActivity(i);
    }

    private void getInventory() {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.getInBackground("RvLPR6mg7x", new GetCallback<Inventory>() {
            @Override
            public void done(Inventory object, ParseException e) {
                JSONArray inventory = object.getInventory();
                for (int i = 0; i < inventory.length(); i++) {
                    Ingredient item = new Ingredient();
                    try {
                        item.setName(inventory.getString(i));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    ingredients.add(item);
                    inventory_list += item.getName();
                    if (i != inventory.length() - 1) {
                        inventory_list += ", ";
                    }
                    inventoryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void displayRecyclerView(View view) {
        rvInventory = view.findViewById(R.id.rvInventory);
        inventoryAdapter = new InventoryAdapter(ingredients);
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void displayToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.inventory_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search) {
                    Intent i = new Intent(getContext(), SearchActivity.class);
                    startActivity(i);
                }
                return false;
            }
        });
    }

    private void addInventory(final String name) {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.getInBackground("RvLPR6mg7x", new GetCallback<Inventory>() {
            @Override
            public void done(Inventory object, ParseException e) {
                JSONArray inventory = object.getInventory();
                inventory.put(name);
                object.setInventory(inventory);
                object.saveInBackground();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inventory_menu, menu);
    }
}