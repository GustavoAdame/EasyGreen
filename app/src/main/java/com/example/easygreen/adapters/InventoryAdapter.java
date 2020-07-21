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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{
    /*** Local Variables **********************/
    private Context context;
    private TextView tvIngredientName;
    private TextView btnIngredientDelete;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
            btnIngredientDelete = itemView.findViewById(R.id.btnIngredientDelete);

            /****** User clicks on [Delete] *******/
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
            }
        });
    }
}

