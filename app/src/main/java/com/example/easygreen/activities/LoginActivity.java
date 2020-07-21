package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easygreen.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    /***** Local Variables **********/
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.btnFBLogin);

        /**** Handle Facebook login responses *********/
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {}
                    @Override
                    public void onCancel() {}
                    @Override
                    public void onError(FacebookException exception) {}
                });
        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {}
            @Override
            public void onFailure() {}
            @Override
            public void onError(Exception exception) {}
        });

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

    }

    /***** User clicks on [Continue with Facebook] ****************/
    public void openFB(View view) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("public_profile"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("Gustavo", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("Gustavo", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("Gustavo", "User logged in through Facebook!");
                    goMainActivity();
                }
            }
        });

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        if (isLoggedIn){
//            goMainActivity();
//        } else {
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//            goMainActivity();
//        }
    }

    /***** User clicks on [Login with Easy Green] ****************/
    public void loginPage(View view) {
        goUserLoginActivity();
    }

    /***** User clicks on [Sign up] ****************/
    public void signUp(View view) {
        goUserSignupActivity();
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