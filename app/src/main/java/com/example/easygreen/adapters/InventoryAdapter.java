package com.example.easygreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.fragments.helpers.DatePickerFragment;
import com.example.easygreen.models.Inventory;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>  {
    /*** Local Variables **********************/
    private Context context;
    private Context getContext;
    private TextView tvIngredientName;
    private Button btnSetExpiration;
    private List<String> ingredients;

    /*** Constructor takes in a String List that represent list of ingredients ***************/
    public InventoryAdapter(List<String> ingredients, Context getContext) {
        this.ingredients = ingredients;
        this.getContext = getContext;
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

        btnSetExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(((AppCompatActivity) getContext).getSupportFragmentManager(), "date picker");
                btnSetExpiration.setText("Confirm");
                btnSetExpiration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Expiration: " + DatePickerFragment.currentDateString, Snackbar.LENGTH_SHORT).show();
                        setExpiration(position, DatePickerFragment.currentDateString);
                        btnSetExpiration.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }

    private void setExpiration(final int position, final String expires) {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.whereEqualTo("user", ParseUser.getCurrentUser());
        inventory.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> objects, ParseException e) {
                JSONArray inventory = objects.get(0).getInventory();
                inventory.remove(position);
                objects.get(0).setInventory(inventory);
                objects.get(0).saveInBackground();
                try {
                    JSONObject newItem = new JSONObject();
                    newItem.put("item", ingredients.get(position));
                    newItem.put("expires", expires);
                    inventory.put(newItem);
                    objects.get(0).setInventory(inventory);
                    objects.get(0).saveInBackground();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
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
            btnSetExpiration = itemView.findViewById(R.id.btnSetExpiration);
        }
    }

}

