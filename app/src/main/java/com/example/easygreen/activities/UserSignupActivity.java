package com.example.easygreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easygreen.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

public class UserSignupActivity extends AppCompatActivity {
    private TextView etUsername, etPassword, etFirstName, etLastName;
    private Button btnUserSignUp;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        displayViews();
        user = new ParseUser();
        btnUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignup();
            }
        });
    }

    private void displayViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnUserSignUp = findViewById(R.id.btnUserSignUp);
    }

    public void userSignup() {
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.put("firstName", etFirstName.getText().toString());
        user.put("lastName", etLastName.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    if(createInventory()){
                        Toast.makeText(UserSignupActivity.this, "Login into new Account", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                } else{
                    Toast.makeText(UserSignupActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean createInventory() {
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


}