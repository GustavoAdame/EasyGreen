package com.example.easygreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easygreen.R;
import com.example.easygreen.adapters.RecipeAdapter;
import com.example.easygreen.models.Recipe;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {
    private ChipGroup cgFilterContainer;
    private Chip chip1, chip2, chip3;
    private CardView cvRecipe;

    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes = new ArrayList<>();
    private RecyclerView rvRecipes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        inflateViews(view);
        getRecipes();
        displayRecyclerView(view);
        return view;
    }

    private void inflateViews(View view) {
        cgFilterContainer = view.findViewById(R.id.cgFilterContainer);
        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);
        cvRecipe = view.findViewById(R.id.cvRecipe);
    }

    private void displayRecyclerView(View view) {
        rvRecipes = view.findViewById(R.id.rvRecipes);
        recipeAdapter = new RecipeAdapter(recipes);
        rvRecipes.setAdapter(recipeAdapter);
        rvRecipes.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void getRecipes() {
        ParseQuery<Recipe> recipe = ParseQuery.getQuery(Recipe.class);
        recipe.findInBackground(new FindCallback<Recipe>() {
            @Override
            public void done(List<Recipe> recipeList, ParseException e) {
                for (Recipe item : recipeList) {
                    recipes.add(item);
                }
                recipeAdapter.notifyDataSetChanged();
            }
        });
    }
}