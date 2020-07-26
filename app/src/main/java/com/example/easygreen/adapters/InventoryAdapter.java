package com.example.easygreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>  {
    /*** Local Variables **********************/
    private Context context;
    private TextView tvIngredientName;
    private List<String> ingredients;

    /*** Constructor takes in a String List that represent list of ingredients ***************/
    public InventoryAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    /*** Inflate ViewHolder **********************/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredients, parent, false);
        return new ViewHolder(view);
    }

    /*** Add the following data into ViewHolder **********************/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        tvIngredientName.setText(ingredients.get(position));
    }


    /*** Part of the Adapter Interface but not in use **********************/
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    /*** Inflate ViewHolder's view elements **********************/
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
        }
    }

}

