package com.example.easygreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.fragments.helpers.DatePickerFragment;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.services.NotificationService;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{
    /*** Local Variables **********************/
    private Context context;
    private Context getContext;
    private TextView tvIngredientName;
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
            tvIngredientName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getExpirationDate(view, getAdapterPosition());
                }
            });


            tvIngredientName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(((AppCompatActivity) getContext).getSupportFragmentManager(), "date picker");

                    Snackbar.make(view, "Setting Expiration...", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Confirm", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setExpiration(getAdapterPosition(), DatePickerFragment.currentDateString);
                                    getOldestExpiration();
                                }
                            }).show();
                    return true;
                }
            });
        }
    }

    /*** This updates the database when user sets expiration date **************/
    private void setExpiration(final int position, final String expires) {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.whereEqualTo("user", ParseUser.getCurrentUser());
        inventory.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> objects, ParseException e) {
                JSONArray expirations = objects.get(0).getExpirations();
                try {
                    expirations.put(position, expires);
                    objects.get(0).setExpirations(expirations);
                    objects.get(0).saveInBackground();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /*** Gets current item expiration date from server **************/
    private void getExpirationDate(final View view, final int adapterPosition) {
        ParseQuery<Inventory> query = ParseQuery.getQuery(Inventory.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> objects, ParseException e) {
                JSONArray expirations = objects.get(0).getExpirations();
                if(expirations.length() != 0){
                    try {
                        Snackbar.make(view, "Expires on " +
                                expirations.getString(adapterPosition), Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /*** Have a app notification with upcoming expiration date *************/
    private void getOldestExpiration() {
        ParseQuery<Inventory> inventory = ParseQuery.getQuery(Inventory.class);
        inventory.whereEqualTo("user", ParseUser.getCurrentUser());
        inventory.findInBackground(new FindCallback<Inventory>() {
            @Override
            public void done(List<Inventory> objects, ParseException e) {
                JSONArray expirations = objects.get(0).getExpirations();
                JSONArray inventory = objects.get(0).getInventory();
                int j = 0;
                for(int i = 1; i < expirations.length(); i++){
                    try {
                        String min = expirations.getString(0);
                        String current = expirations.getString(i);
                        if(min.compareTo(current) > 0){
                            min = current;
                            j = i;
                        }
                        if(i == expirations.length()-1){
                            Intent serviceIntent = new Intent(getContext, NotificationService.class);
                            serviceIntent.putExtra("inputExtra", min);
                            serviceIntent.putExtra("inputName", inventory.getString(j));
                            ContextCompat.startForegroundService(getContext, serviceIntent);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

}

