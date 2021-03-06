package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easygreen.R;

import org.json.JSONArray;
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
    public String API_Key;
    private String recipeID;

    /************* Initial State of Activity ********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        API_Key = getResources().getString(R.string.API_Key);
        displayViews();

        Intent intent = getIntent();
        if(intent != null){
            recipeID = intent.getStringExtra("id");
        }

        getRecipeDetails(recipeID);
        getRecipeInstruction(recipeID);
    }

    /***************** Inflating Views *****************/
    private void displayViews() {
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        tvPreptime = findViewById(R.id.tvPreptime);
        tvServingSize = findViewById(R.id.tvServingSize);
        tvCalorieServing = findViewById(R.id.tvCalorieServing);
    }

    /***************** Get more details for a recipe *****************/
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });
    }

    /***************** Get recipe instruction *****************/
    private void getRecipeInstruction(String recipeID) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.spoonacular.com")
                .addPathSegment("recipes")
                .addPathSegment(recipeID)
                .addQueryParameter("stepBreakdown", String.valueOf(false))
                .addPathSegment("analyzedInstructions")
                .build();

        final String request = url+"&apiKey="+API_Key;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    String step = "";
                    JSONArray steps = jsonArray.getJSONObject(0).getJSONArray("steps");
                    for(int i = 0; i < steps.length(); i++){
                        String stepNumber  = steps.getJSONObject(i).getString("number");
                        String stepInstruction = steps.getJSONObject(i).getString("step");
                        step += ("Step " + stepNumber + ": " + stepInstruction);
                        step += "\n\n";
                    }
                    tvRecipeDescription.setText(step);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvRecipeDescription.setMovementMethod(new ScrollingMovementMethod());
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
            }
        });

    }
}