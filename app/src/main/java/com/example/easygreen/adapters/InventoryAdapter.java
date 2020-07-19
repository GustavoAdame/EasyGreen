package com.example.easygreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.models.Inventory;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{
    private List<String> ingredients;
    private Context context;

    private TextView tvIngredientName;
    private TextView btnIngredientDelete;

    public InventoryAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tvIngredientName.setText(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            btnIngredientDelete = itemView.findViewById(R.id.btnIngredientDelete);

            btnIngredientDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemInventory(getAdapterPosition());
                    Toast.makeText(context, ingredients.get(getAdapterPosition()) + " Deleted!", Toast.LENGTH_SHORT).show();
                    ingredients.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }

    private void deleteItemInventory(final int position) {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.getInBackground("RvLPR6mg7x", new GetCallback<Inventory>() {
            @Override
            public void done(Inventory object, ParseException e) {
                JSONArray inventory = object.getInventory();
                inventory.remove(position);
                object.setInventory(inventory);
                object.saveInBackground();
            }
        });
    }
}

