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
import android.widget.Button;
import android.widget.EditText;
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
import com.example.easygreen.adapters.ShoppingAdapter;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.ShoppingList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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

public class ShoppingListFragment extends Fragment {
    /******* Local Variables *************/
    private RecyclerView rvShoppingList;
    private ShoppingAdapter shoppingAdapter;
    private EditText addItem;
    private Button btnAdd;
    private List<String> items = new ArrayList<>();
    private String API_key;

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        API_key = getActivity().getResources().getString(R.string.API_key);
        displayToolbar(getActivity());
        displayViews(getActivity());
        getShoppingList();
        displayRecyclerView(getActivity());
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        ImageView btnBarcode = getActivity().findViewById(R.id.btnBarcode2);
        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forSupportFragment(ShoppingListFragment.this)
                        .setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
                        .setBeepEnabled(true)
                        .setPrompt("Scan an item")
                        .initiateScan();
            }
        });

        /******** User clicks on [Add] *************/
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = addItem.getText().toString();
                updateShoppingList(item);
                addItem.setText("");
            }
        });


        /******* User swipe left to delete item *********/
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    final int position = viewHolder.getAdapterPosition();

                    String prevItem = items.get(position);
                    deleteItemShopping(position);
                    Snackbar.make(rvShoppingList, prevItem + " Deleted!", Snackbar.LENGTH_SHORT).show();
                    items.remove(position);
                    shoppingAdapter.notifyItemRemoved(position);
                    rvShoppingList.setAdapter(shoppingAdapter);
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
        itemTouchHelper.attachToRecyclerView(rvShoppingList);
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

    private void searchItembyCode(String contents) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.upcdatabase.org")
                .addPathSegment("product")
                .addPathSegment(contents)
                .build();

        final String request = url+"?apikey=";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject item = json.jsonObject;
                try {
                    String newItem = item.getString("title");
                    updateShoppingList(newItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(getView(), "Can't find item", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("searchItembyCode()", response);
            }
        });
    }

    /***************** Inflating Fragment Layout, Views, RecyclerView *****************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    private void displayViews(FragmentActivity view) {
        addItem = view.findViewById(R.id.addItem);
        btnAdd = view.findViewById(R.id.btnAdd);
    }

    private void displayToolbar(FragmentActivity view) {
       Toolbar toolbarTitle = view.findViewById(R.id.toolbarTitle);
       Toolbar toolbarAdd = view.findViewById(R.id.toolbarAdd);
    }

    private void displayRecyclerView(FragmentActivity view) {
        rvShoppingList = view.findViewById(R.id.rvShoppingList);
        shoppingAdapter = new ShoppingAdapter(items);
        rvShoppingList.setAdapter(shoppingAdapter);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /*** Delete the Shopping List item from server based on item's position **********************/
    private void deleteItemShopping(final int position) {
        ParseQuery<ShoppingList> query = ParseQuery.getQuery(ShoppingList.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ShoppingList>() {
            @Override
            public void done(List<ShoppingList> objects, ParseException e) {
                JSONArray shoppingList = objects.get(0).getShoppingList();
                shoppingList.remove(position);
                objects.get(0).setShoppingList(shoppingList);
                objects.get(0).saveInBackground();
            }
        });
    }

    /*********** Get initial Shopping List from database ******************/
    private void getShoppingList() {
        ParseQuery<ShoppingList> query = ParseQuery.getQuery(ShoppingList.class);
        if(ParseUser.getCurrentUser() != null){
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ShoppingList>() {
                @Override
                public void done(List<ShoppingList> objects, ParseException e) {
                    JSONArray shoppingList = objects.get(0).getShoppingList();
                    for (int i = 0; i < shoppingList.length(); i++) {
                        try {
                            items.add(shoppingList.getString(i));
                            shoppingAdapter.notifyDataSetChanged();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /****** Update changes to database and application ****************************/
    private void updateShoppingList(String item) {
        addShoppingList(item);
        items.add(item);
        shoppingAdapter.notifyDataSetChanged();
        Snackbar.make(rvShoppingList, item + " Added!", Snackbar.LENGTH_SHORT).show();
    }

    private void addShoppingList(final String name) {
        ParseQuery<ShoppingList> query = ParseQuery.getQuery(ShoppingList.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ShoppingList>() {
            @Override
            public void done(List<ShoppingList> objects, ParseException e) {
                JSONArray shoppingList = objects.get(0).getShoppingList();
                shoppingList.put(name);
                objects.get(0).setShoppingList(shoppingList);
                objects.get(0).saveInBackground();
            }
        });
    }
}