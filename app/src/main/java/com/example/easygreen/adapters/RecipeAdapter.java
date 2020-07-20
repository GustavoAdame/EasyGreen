package com.example.easygreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easygreen.R;
import com.example.easygreen.activities.RecipeDetailsActivity;
import com.example.easygreen.models.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    public List<Recipe> recipes;
    private Context context;
    public ImageView ivRecipeImage;
    public TextView tvRecipeTag;
    public TextView tvRecipeTitle;
    private CardView cvRecipe;

    public RecipeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.items_recipe, parent, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Recipe item = recipes.get(position);
        String url = item.getURL();
        Glide.with(context).load(url).into(ivRecipeImage);
        tvRecipeTitle.setText(item.getName());

        cvRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RecipeDetailsActivity.class);
                String a = recipes.get(position).getRecipe_id();
                i.putExtra("id", recipes.get(position).getRecipe_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvRecipeTag = itemView.findViewById(R.id.tvRecipeTag);
            ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
            cvRecipe = itemView.findViewById(R.id.cvRecipe);
        }
    }
}
