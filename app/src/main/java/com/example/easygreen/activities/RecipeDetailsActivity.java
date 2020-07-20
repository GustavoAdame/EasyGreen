package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;
import okhttp3.HttpUrl;

public class RecipeDetailsActivity extends AppCompatActivity {
    /******** Local Variable ************************/
    private ImageView ivRecipeImage;
    private TextView tvRecipeTitle, tvRecipeDescription;
    private TextView tvPreptime, tvServingSize, tvCalorieServing;
    /******** Defaults ************************/
    public final String API_Key = "375469b443e24f9fa1b3270fad4d7402";
    private String recipeID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        displayViews();
        Intent intent = getIntent();
        if(intent != null){
            recipeID = intent.getStringExtra("id");
        }
        getRecipeDetails(recipeID);

    }

    private void displayViews() {
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        tvPreptime = findViewById(R.id.tvPreptime);
        tvServingSize = findViewById(R.id.tvServingSize);
        tvCalorieServing = findViewById(R.id.tvCalorieServing);
    }

    /***************** Inflating Views *****************/

    /***************** Getting 20 recipes from API *****************/
    public void getRecipeDetails(String recipeId) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spoonacular.com")
                .addPathSegment("recipes")
                .addPathSegment(recipeId)
                .addQueryParameter("includeNutrition", String.valueOf(true))
                .addPathSegment("information")
                .build();

        final String request = url+"&apiKey="+API_Key;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                        JSONObject jsonObject = json.jsonObject;
                        tvRecipeTitle.setText(jsonObject.getString("title"));
                        Glide.with(getApplicationContext()).load(jsonObject.getString("image")).into(ivRecipeImage);
                        tvServingSize.setText(jsonObject.getString("servings"));
                        tvPreptime.setText(jsonObject.getString("readyInMinutes"));

                    JSONObject nutrition = jsonObject.getJSONObject("nutrition");
                    JSONObject nutrients = nutrition.getJSONArray("nutrients").getJSONObject(0);
                    String calories = nutrients.getString("amount");
                    tvCalorieServing.setText(calories);
                    tvRecipeDescription.setText(jsonObject.getString("instructions").replaceAll("\\.","\n"));
                    tvRecipeDescription.setMovementMethod(new ScrollingMovementMethod());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });
    }
}