package com.example.easygreen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.ShoppingList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    /*** Local Variables **********************/
    private Context context;
    public CheckBox cbSelect;
    public List<String> checklist;

    /*** Constructor takes in a String List that represent a shopping list ***************/
    public ShoppingAdapter(List<String> checklist) {
        this.checklist = checklist;
    }

    /*** Inflate ViewHolder **********************/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.items_shoppinglist, parent, false);
        return new ViewHolder(view);
    }

    /*** Add the following data into ViewHolder **********************/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        cbSelect.setText(checklist.get(position));
    }

    /*** Part of the Adapter Interface but not in use **********************/
    @Override
    public int getItemCount() {
        return checklist.size();
    }

    /*** Inflate ViewHolder's view elements **********************/
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelect = itemView.findViewById(R.id.cbSelect);

            /****** User holds on checkbox item for more than 1 second *******/
            cbSelect.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteItemShopping(getAdapterPosition());
                    checklist.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });
        }
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

}
