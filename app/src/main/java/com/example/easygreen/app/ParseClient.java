package com.example.easygreen.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.easygreen.models.Inventory;
import com.example.easygreen.models.ShoppingList;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

public class ParseClient extends Application {
    public static final String CHANNEL_ID = "easygreenServiceChannel";

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

        ParseFacebookUtils.initialize(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "EasyGreen",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}