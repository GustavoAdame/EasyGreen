package com.example.easygreen.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.models.Recipe;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    public List<Recipe> recipes;
    private Context context;

    public ImageView ivRecipeImage;
    public TextView tvRecipeTag;
    public TextView tvRecipeTitle;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe item = recipes.get(position);
        ParseFile image = item.getImage();
        image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                ivRecipeImage.setImageBitmap(bmp);
            }
        });
        tvRecipeTitle.setText(item.getName());
        tvRecipeTag.setText("- "+item.getTag()+" -");
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

        }
    }
}
