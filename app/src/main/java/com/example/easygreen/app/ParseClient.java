package com.example.easygreen.app;

import android.app.Application;

import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.ShoppingList;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseClient extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /***** Use in-App data models as ParseObjects *********/
        ParseObject.registerSubclass(Inventory.class);
        ParseObject.registerSubclass(ShoppingList.class);

        /***** Connect to Server and Backend *********/
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("easygreen")
                .server("https://easygreen.herokuapp.com/parse/")
                .build()
        );
    }
}
