package com.example.easygreen.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.adapters.ShoppingAdapter;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.ShoppingList;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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


        /******* User swipe left to delete item *********/
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    final int position = viewHolder.getAdapterPosition();

                    final String prevItem = items.get(position);
                    deleteItemShopping(position);
                    Snackbar.make(rvShoppingList, prevItem + " Deleted!", Snackbar.LENGTH_SHORT).show();
                    items.remove(position);
                    shoppingAdapter.notifyItemRemoved(position);
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