package com.example.easygreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.adapters.ShoppingAdapter;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.ShoppingList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    /******* Local Variables *************/
    private RecyclerView rvShoppingList;
    private ShoppingAdapter shoppingAdapter;
    private EditText addItem;
    private Button btnAdd;
    private List<String> items = new ArrayList<>();

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayToolbar(getActivity());
        displayViews(getActivity());
        getShoppingList();
        displayRecyclerView(getActivity());

        /******** User clicks on [Add] *************/
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = addItem.getText().toString();
                updateShoppingList(item);
                addItem.setText("");
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

    /*********** Get initial Shopping List from database ******************/
    private void getShoppingList() {
        ParseQuery<ShoppingList> query = ParseQuery.getQuery(ShoppingList.class);
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
    
    /****** Update changes to database and application ****************************/
    private void updateShoppingList(String item) {
        addShoppingList(item);
        items.add(item);
        shoppingAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), item + " Added!", Toast.LENGTH_SHORT).show();
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