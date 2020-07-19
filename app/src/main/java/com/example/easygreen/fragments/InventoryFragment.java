package com.example.easygreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.activities.MainActivity;
import com.example.easygreen.adapters.InventoryAdapter;
import com.example.easygreen.models.Inventory;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {
    /******** Local Variable ************************/
    private InventoryAdapter inventoryAdapter;
    private RecyclerView rvInventory;
    private List<String> ingredients = new ArrayList<>();
    public String inventory_list = "";

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayToolbar(getActivity());
        displayRecyclerView(getActivity());
        getInventory();
        searchIngredients();

        Button btnGetRecipes = getActivity().findViewById(R.id.btnGetRecipes);
        btnGetRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDatatoAPI();
            }
        });
    }

    /***************** Inflating Views and RecyclerView *****************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    private void displayToolbar(FragmentActivity view) {
        Toolbar toolbarTitle = view.findViewById(R.id.toolbarTitle);
        Toolbar toolbarAdd = view.findViewById(R.id.toolbarAdd);
    }

    private void displayRecyclerView(FragmentActivity view) {
        rvInventory = view.findViewById(R.id.rvInventory);
        inventoryAdapter = new InventoryAdapter(ingredients);
        rvInventory.setAdapter(inventoryAdapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    /********** Send current list to Recipes fragment ********************/
    private void sendDatatoAPI() {
        Bundle toMainActivity = new Bundle();
        toMainActivity.putString("inventory", inventory_list);
        Intent i = new Intent(getContext(), MainActivity.class);
        i.putExtras(toMainActivity);
        startActivity(i);
    }

    /****** Autocomplete search for ingredients ****************************/
    private void searchIngredients() {
        String[] ingredients = getResources().getStringArray(R.array.Ingredient);
        final AutoCompleteTextView actvAdd = getActivity().findViewById(R.id.actvAdd);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, ingredients);
        actvAdd.setAdapter(adapter);
        actvAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                updateInventory(item);
            }
        });;
    }

    /*********** Get initial inventory from database ******************/
    private void getInventory() {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.getInBackground("RvLPR6mg7x", new GetCallback<Inventory>() {
            @Override
            public void done(Inventory object, ParseException e) {
                JSONArray inventory = object.getInventory();
                for (int i = 0; i < inventory.length(); i++) {
                    try {
                        ingredients.add(inventory.getString(i));
                        inventory_list += inventory.getString(i);
                        if (i != inventory.length() - 1) {
                            inventory_list += ", ";
                        }
                        inventoryAdapter.notifyDataSetChanged();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /****** Update changes to database and application ****************************/
    private void updateInventory(String item) {
        addInventory(item);
        ingredients.add(item);
        inventoryAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), item + " Added!", Toast.LENGTH_SHORT).show();
        inventory_list += ", " + item;
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