package com.example.easygreen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.easygreen.R;
import com.example.easygreen.activities.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.parse.ParseUser;

public class AccountFragment extends Fragment {
    /****** Local Variables ************/
    private Button btnLogout;
    private ImageView ivProfileImage;
    private TextView tvProfileName;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayViews();

        /***** User clicks on [Log Out] ***********/
        btnLogout = getActivity().findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ParseUser.getCurrentUser() != null){
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    goLoginActivity();
                }
                if(AccessToken.getCurrentAccessToken() != null){
                    LoginManager.getInstance().logOut();
                    AccessToken.setCurrentAccessToken(null);
                    goLoginActivity();
                }
            }
        });
    }

    private void goLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    /****** Inflate Fragment view elements *******/
    private void displayViews() {
        ivProfileImage = getActivity().findViewById(R.id.ivProfileImage);
        tvProfileName = getActivity().findViewById(R.id.tvProfileName);
        if(ParseUser.getCurrentUser() != null){
            Glide.with(getActivity()).load( ParseUser.getCurrentUser().getParseFile("profileImage").getUrl()).into(ivProfileImage);
            tvProfileName.setText( ParseUser.getCurrentUser().get("firstName") + " " +  ParseUser.getCurrentUser().get("lastName"));
        }
    }

    /****** Inflate Fragment layout ************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}