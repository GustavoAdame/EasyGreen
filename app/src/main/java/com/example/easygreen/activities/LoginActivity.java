package com.example.easygreen.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easygreen.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoginStatusCallback;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    /***** Local Variables **********/
    private LoginButton loginButton;
    private ImageView ivLogo;
    AnimationDrawable iconAnimate;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.btnFBLogin);
        ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setBackgroundResource(R.drawable.animation);
        iconAnimate = (AnimationDrawable) ivLogo.getBackground();

        if (ParseUser.getCurrentUser() != null || AccessToken.getCurrentAccessToken() != null) {
            goMainActivity();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        iconAnimate.start();
    }

    /***** User clicks on [Continue with Facebook] ****************/
    public void openFB(View view) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("public_profile", "pages_manage_posts", "pages_read_user_content"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if(err != null){
                    Toast.makeText(LoginActivity.this, "Uh oh. Error occurred " + err.toString(), Toast.LENGTH_SHORT).show();
                }
                if (user == null) {
                } else if (user.isNew()) {
                    //User signed up and logged in through Facebook
                    storeFacebookData(user);
                    if(createInventory(user) && createShoppingList(user)){
                        goMainActivity();
                    }
                } else {
                    //User logged in through Facebook
                    goMainActivity();
                }
            }
        });
    }

    private void storeFacebookData(final ParseUser user) {
        Profile currentProfile = Profile.getCurrentProfile();
        user.put("firstName", currentProfile.getFirstName());
        user.put("lastName", currentProfile.getLastName());
    }

    /***** User clicks on [Login with Easy Green] ****************/
    public void loginPage(View view) {
        goUserLoginActivity();
    }

    /***** User clicks on [Sign up] ****************/
    public void signUp(View view) {
        goUserSignupActivity();
    }

    /****** Create a Inventory model for User ************/
    private boolean createInventory(ParseUser user) {
        ParseObject newInventory = ParseObject.create("Inventory");
        JSONArray newArray = new JSONArray();
        newInventory.put("user", user);
        newInventory.put("ingredient_list", newArray);
        try {
            newInventory.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /****** Create a Shopping List model for User ************/
    private boolean createShoppingList(ParseUser user) {
        ParseObject newShoppingList = ParseObject.create("ShoppingList");
        JSONArray newArray = new JSONArray();
        newShoppingList.put("user", user);
        newShoppingList.put("shopping_list", newArray);
        try {
            newShoppingList.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /***** Intents to another screens ****************/
    private void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goUserLoginActivity() {
        startActivity(new Intent(this, UserLoginActivity.class));
        finish();
    }

    private void goUserSignupActivity() {
        startActivity(new Intent(this, UserSignupActivity.class));
        finish();
    }
}