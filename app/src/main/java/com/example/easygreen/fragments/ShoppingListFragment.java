package com.example.easygreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.adapters.ShoppingAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {
    private RecyclerView rvShoppingList;
    private ShoppingAdapter shoppingAdapter;
    private List<String> items = new ArrayList<>();
    private List<String> discard = new ArrayList<>();
    private EditText addItem;
    private Button btnAdd;
    private String item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        displayToolbar(view);
        displayViews(view);
        displayRecyclerView(view);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = addItem.getText().toString();
                items.add(item);
                addItem.setText("");
                shoppingAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void displayViews(View view) {
        addItem = view.findViewById(R.id.addItem);
        btnAdd = view.findViewById(R.id.btnAdd);
    }

    private void displayToolbar(View view) {
       Toolbar toolbarTitle = view.findViewById(R.id.toolbarTitle);
       Toolbar toolbarAdd = view.findViewById(R.id.toolbarAdd);
    }

    private void displayRecyclerView(View view) {
        rvShoppingList = view.findViewById(R.id.rvShoppingList);
        shoppingAdapter = new ShoppingAdapter(items);
        rvShoppingList.setAdapter(shoppingAdapter);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}