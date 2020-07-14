package com.example.easygreen.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;
import com.example.easygreen.adapters.RecipeAdapter;
import com.example.easygreen.models.Recipe;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;

public class RecipeFragment extends Fragment {
    private ChipGroup cgFilterContainer;
    private Chip chip1, chip2, chip3;
    private CardView cvRecipe;

    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes = new ArrayList<>();
    private RecyclerView rvRecipes;
    public static final String API_Key = "375469b443e24f9fa1b3270fad4d7402";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        inflateViews(view);
        getRecipes();
        displayRecyclerView(view);
        return view;
    }

    private void getRecipes() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spoonacular.com")
                .addPathSegment("recipes")
                .addPathSegment("findByIngredients")
                .addQueryParameter("ingredients", "cheese,yogurt,butter,milk")
                .addQueryParameter("number", String.valueOf(6))
                .build();

        String request = url+"&apiKey="+API_Key;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    for (int i = 0; i < json.jsonArray.length() ; i++) {
                        JSONObject jsonObject = json.jsonArray.getJSONObject(i);
                        Recipe item = new Recipe();
                        item.setName(jsonObject.getString("title"));
                        item.setURL(jsonObject.getString("image"));
                        recipes.add(item);
                    }
                    recipeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("getRecipes()", "Failure");
            }
        });
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
}