package com.example.easygreen.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;
import com.example.easygreen.adapters.RecipeAdapter;
import com.example.easygreen.models.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;

public class RecipeFragment extends Fragment {
    /******** Local Variable ************************/
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes = new ArrayList<>();
    private RecyclerView rvRecipes;
    /******** Defaults ************************/
    public final String API_Key = "375469b443e24f9fa1b3270fad4d7402";
    public String inventory_list = "Chicken Breast, Brown Rice, Black Beans, Queso Fresco";

    /************* Initial State of Fragment ********************/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle fromMainActivity = this.getArguments();
        if(fromMainActivity != null){
            inventory_list = fromMainActivity.getString("inventory");
        }
        getRecipes(inventory_list);
        displayRecyclerView(getActivity());
    }

    /***************** Inflating Views and RecyclerView *****************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    private void displayRecyclerView(FragmentActivity view) {
        rvRecipes = view.findViewById(R.id.rvRecipes);
        recipeAdapter = new RecipeAdapter(recipes);
        rvRecipes.setAdapter(recipeAdapter);
        rvRecipes.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    /***************** Getting 20 recipes from API *****************/
    public void getRecipes(String inventory_list) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spoonacular.com")
                .addPathSegment("recipes")
                .addPathSegment("findByIngredients")
                .addQueryParameter("ingredients", inventory_list)
                .addQueryParameter("number", String.valueOf(20))
                .addQueryParameter("ranking", String.valueOf(1))
                .addQueryParameter("ignorePantry", String.valueOf(true))
                .build();

        final String request = url+"&apiKey="+API_Key;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    for (int i = 0; i < json.jsonArray.length() ; i++) {
                        JSONObject jsonObject = json.jsonArray.getJSONObject(i);
                        Recipe item = new Recipe();
                        item.setRecipe_id(jsonObject.getString("id"));
                        item.setName(jsonObject.getString("title"));
                        item.setURL(jsonObject.getString("image"));
                        recipes.add(item);
                    };
                    recipeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("getRecipes()", response);
            }
        });
    }
}