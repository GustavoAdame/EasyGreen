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
import com.example.easygreen.models.Ingredient;
import com.example.easygreen.models.Inventory;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

import static com.example.easygreen.models.Inventory.KEY_inventory_list;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    public List<Ingredient> ingredients;
    private Context context;

    public TextView tvIngredientName;
    public TextView btnIngredientDelete;
    public int position;

    public InventoryAdapter(List<Ingredient> ingredients) {
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
        Ingredient item = ingredients.get(position);
        tvIngredientName.setText(item.getName());
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
                    position = getAdapterPosition();
                    deleteInventory();
                    ingredients.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), ingredients.size());
                }
            });
        }
    }

    private void deleteInventory() {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> inventories, ParseException e) {
                ParseRelation<Ingredient> ingredientRelation = inventories.get(0).getRelation(KEY_inventory_list);
                ingredientRelation.getQuery().findInBackground(new FindCallback<Ingredient>() {
                    @Override
                    public void done(final List<Ingredient> ingredientList, ParseException e) {
                      ingredientList.get(position).deleteInBackground(new DeleteCallback() {
                          @Override
                          public void done(ParseException e) {
                              Toast.makeText(context, ingredientList.get(position).getName() + " Deleted!", Toast.LENGTH_LONG).show();
                          }
                      });
                    }
                });
            }
        });
    }
}
