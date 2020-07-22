package com.example.easygreen.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.easygreen.R;
import com.example.easygreen.activities.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {
    /****** Local Variables ************/
    private Button btnLogout, btnTakePicture;
    private ImageView ivProfileImage, ivFacebookTag;
    private TextView tvProfileName;

    /***** Local Variables *************/
    private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private final String APP_TAG = "EasyGreen";
    private File photoFile;
    private String photoFileName = "profile.jpg";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayViews();
        getProfileInfo();

        /***** User clicks on [Log Out] ***********/
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

    private void getProfileInfo() {
        if(ParseUser.getCurrentUser() != null && Profile.getCurrentProfile() == null){
            ivFacebookTag.setVisibility(View.GONE);
            Glide.with(getActivity()).load( ParseUser.getCurrentUser().getParseFile("profileImage").getUrl()).into(ivProfileImage);
            tvProfileName.setText( ParseUser.getCurrentUser().get("firstName") + " " +  ParseUser.getCurrentUser().get("lastName"));

            btnTakePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchCamera();
                    changeImage(ParseUser.getCurrentUser(), photoFile);
                    Glide.with(getActivity()).load( ParseUser.getCurrentUser().getParseFile("profileImage").getUrl()).into(ivProfileImage);
                }
            });
        }

        if(Profile.getCurrentProfile() != null){
            btnTakePicture.setVisibility(View.GONE);
            Glide.with(getActivity()).load(Profile.getCurrentProfile().getProfilePictureUri(500, 600).toString()).into(ivProfileImage);
            tvProfileName.setText( ParseUser.getCurrentUser().get("firstName") + " " +  ParseUser.getCurrentUser().get("lastName"));
        }
    }

    /***** Intent to Login Activity **********/
    private void goLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    /****** Inflate Fragment view elements *******/
    private void displayViews() {
        ivProfileImage = getActivity().findViewById(R.id.ivProfileImage);
        tvProfileName = getActivity().findViewById(R.id.tvProfileName);
        ivFacebookTag = getActivity().findViewById(R.id.ivFacebookTag);
        btnLogout = getActivity().findViewById(R.id.btnLogout);
        btnTakePicture = getActivity().findViewById(R.id.btnTakePicture);
    }

    private void changeImage(ParseUser currentUser, File photoFile) {
        currentUser.remove("profileImage");
        currentUser.put("profileImage", new ParseFile(photoFile));
        try {
            currentUser.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /****** Inflate Fragment layout ************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /***** Open Phone Camera *******/
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.EasyGreen", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /***** Get Picture Path File *******/
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    /******* Pass Picture back to Fragment **********/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}