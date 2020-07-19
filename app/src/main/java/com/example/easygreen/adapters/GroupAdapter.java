package com.example.easygreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.activities.MainActivity;
import com.example.easygreen.models.Ingredient;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>  {
    public List<Ingredient> ingredients;
    private Context context;
    public TextView tvIngredientName;
    public TextView btnIngredientAdd;

    public GroupAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.items_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Ingredient item = ingredients.get(position);
        tvIngredientName.setText(item.getName());

        btnIngredientAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle toMainActivity = new Bundle();
                if(item.getName() != null){
                    toMainActivity.putString("newItem", item.getName());
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtras(toMainActivity);
                    context.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            btnIngredientAdd = itemView.findViewById(R.id.btnIngredientAdd);
        }
    }

}
