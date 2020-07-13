package com.example.easygreen.app;

import android.app.Application;

import com.example.easygreen.models.Group;
import com.example.easygreen.models.Ingredient;
import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.Recipe;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseClient extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Ingredient.class);
        ParseObject.registerSubclass(Inventory.class);
        ParseObject.registerSubclass(Recipe.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("easygreen")
                .server("https://easygreen.herokuapp.com/parse/")
                .build()
        );
    }
}
