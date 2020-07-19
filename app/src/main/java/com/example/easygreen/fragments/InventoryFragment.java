package com.example.easygreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    private RecyclerView rvInventory;
    private List<String> ingredients = new ArrayList<>();
    public String inventory_list = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Gustavo", "onCreate: ");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("Gustavo", "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("ingredients", (ArrayList<String>) ingredients);
        outState.putString("inventory", inventory_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("Gustavo", "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        displayToolbar(getActivity());
        getInventory();
        displayRecyclerView(getActivity());
        updateInventory();

        Button btnGetRecipes = getActivity().findViewById(R.id.btnGetRecipes);
        btnGetRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDatatoAPI();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Gustavo", "onCreateView: ");
        onRestoreInstanceState(savedInstanceState);
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("Gustavo", "onRestoreInstanceState: ");
        if(savedInstanceState != null && savedInstanceState.containsKey("ingredients")){
            Log.d("Gustavo", "onCreate: Entered2");
            ingredients = savedInstanceState.getStringArrayList("ingredients");
            inventory_list = savedInstanceState.getString("inventory");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inventory_menu, menu);
    }

    private void displayRecyclerView(FragmentActivity view) {
        rvInventory = view.findViewById(R.id.rvInventory);
        inventoryAdapter = new InventoryAdapter(ingredients);
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void displayToolbar(FragmentActivity view) {
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

    private void updateInventory() {
        Bundle fromMainActivity = this.getArguments();
        if(fromMainActivity != null){
            String prev = "";
            try{
                if(fromMainActivity.getString("newItem") != prev){
                        String item = fromMainActivity.getString("newItem");
                        addInventory(item);
                        ingredients.add(item);
                        inventoryAdapter.notifyDataSetChanged();
                        inventory_list += ", " + item;
                        Toast.makeText(getContext(), item + " Added!", Toast.LENGTH_LONG).show();
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
                    ingredients.add(item.getName());
                    inventory_list += item.getName();
                    if (i != inventory.length() - 1) {
                        inventory_list += ", ";
                    }
                    inventoryAdapter.notifyDataSetChanged();
                }
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

}