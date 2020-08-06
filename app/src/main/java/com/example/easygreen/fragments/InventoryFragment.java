package com.example.easygreen.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;
import com.example.easygreen.activities.MainActivity;
import com.example.easygreen.adapters.InventoryAdapter;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Headers;
import okhttp3.HttpUrl;

public class InventoryFragment extends Fragment {
    /******** Local Variable ************************/
    private InventoryAdapter inventoryAdapter;
    private RecyclerView rvInventory;
    private List<String> ingredients = new ArrayList<>();
    public String inventory_list = "";
    public String API_Key;

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        API_Key = getActivity().getResources().getString(R.string.API_Key);
        displayToolbar(getActivity());
        displayRecyclerView(getActivity());
        getInventory();
        searchIngredients();
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        /******* User clicks [Barcode] to scan item *********/
        ImageView btnBarcode = getActivity().findViewById(R.id.btnBarcode);
        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forSupportFragment(InventoryFragment.this)
                        .setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
                        .setBeepEnabled(true)
                        .setPrompt("Scan an item")
                        .initiateScan();
            }
        });

        /******* User clicks [Get Recipes] *********/
        Button btnGetRecipes = getActivity().findViewById(R.id.btnGetRecipes);
        btnGetRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDatatoAPI();
            }
        });

        /******* User swipe left to delete item *********/
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    int position = viewHolder.getAdapterPosition();

                    String prevItem = ingredients.get(position);
                    deleteItemInventory(position);
                    ingredients.remove(position);
                    Snackbar.make(rvInventory, prevItem + " Deleted!", Snackbar.LENGTH_SHORT).show();
                    inventoryAdapter.notifyItemRemoved(position);
                    rvInventory.setAdapter(inventoryAdapter);
                }
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvInventory);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                searchItembyCode(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void searchItembyCode(String currentScan) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spoonacular.com")
                .addPathSegment("food")
                .addPathSegment("products")
                .addPathSegment("upc")
                .addPathSegment(currentScan)
                .build();

        final String request = url+"?apiKey=33d7a66937eb4fa89998c189c7ade6f3";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject item = json.jsonObject;
                try {
                    String newItem = item.getString("title");
                    updateInventory(newItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("searchItembyCode()", response);
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
        inventoryAdapter = new InventoryAdapter(ingredients, getContext());
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

    /*** Delete the Inventory item from server based on item's position *****************/
    private void deleteItemInventory(final int position) {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.whereEqualTo("user", ParseUser.getCurrentUser());
        inventory.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> objects, ParseException e) {
                JSONArray inventory = objects.get(0).getInventory();
                inventory.remove(position);
                objects.get(0).setInventory(inventory);
                objects.get(0).saveInBackground();

                JSONArray expirations = objects.get(0).getExpirations();
                expirations.remove(position);
                objects.get(0).setExpirations(expirations);
                objects.get(0).saveInBackground();

            }
        });
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
                actvAdd.setText("");
            }
        });;
    }

    /*********** Get initial inventory from database ******************/
    private void getInventory() {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        if(ParseUser.getCurrentUser() != null){
            inventory.whereEqualTo("user", ParseUser.getCurrentUser());
            inventory.findInBackground(new FindCallback<Inventory>() {
                @Override
                public void done(List<Inventory> objects, ParseException e) {
                    JSONArray inventory = objects.get(0).getInventory();
                    for (int i = 0; i < inventory.length(); i++) {
                        try {
                            String item = inventory.getString(i);
                            ingredients.add(item);
                            inventory_list += item;
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
    }

    /****** Update changes to database and application ****************************/
    private void updateInventory(String item) {
        addInventory(item);
        ingredients.add(item);
        Snackbar.make(rvInventory, item + " Added!", Snackbar.LENGTH_SHORT).show();
        inventoryAdapter.notifyItemInserted(ingredients.size()-1);
        inventory_list += ", " + item;
    }

    private void addInventory(final String name) {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.whereEqualTo("user", ParseUser.getCurrentUser());
        inventory.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> objects, ParseException e) {
                JSONArray inventory = objects.get(0).getInventory();
                inventory.put(name);
                objects.get(0).setInventory(inventory);
                objects.get(0).saveInBackground();
            }
        });
    }


}