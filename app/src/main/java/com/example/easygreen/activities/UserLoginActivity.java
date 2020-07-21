package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easygreen.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class UserLoginActivity extends AppCompatActivity {
    /***** Local Variables **********/
    private TextView etUsername, etPassword;
    private Button btnLogin;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        displayViews();

        /*** User clicks on [Log In] ******/
        user = new ParseUser();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    /***** Inflate views on screen *********/
    private void displayViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    /***** EasyGreen Login Verification *********/
    public void userLogin() {
        ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                   Intent i = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(i);
                } else {
                    Toast.makeText(UserLoginActivity.this, "Username or Password is not correct", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}